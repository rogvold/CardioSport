package ru.sport.cardiomood.core.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.enums.ActivityStatus;
import ru.sport.cardiomood.core.enums.ActivityType;
import ru.sport.cardiomood.core.enums.WorkoutStatus;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.json.entity.JsonUserState;
import ru.sport.cardiomood.json.entity.JsonWorkout;
import ru.sport.cardiomood.utils.CardioUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class WorkoutManager implements WorkoutManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;
    @EJB
    ActivityManagerLocal acMan;
    @EJB
    UserManagerLocal userMan;
    @EJB
    GPSManagerLocal gpsMan;
    @EJB
    CardioSessionManagerLocal csMan;
    @EJB
    CoachManagerLocal coachMan;

    private Workout getWorkoutByName(Long coachId, String name) {
        Query q = em.createQuery("select w from Workout w where w.coachId = :cId and w.name = :name").setParameter("cId", coachId).setParameter("name", name);
        List<Workout> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Workout createWorkout(Long coachId, String name, String description) throws SportException {
        if (getWorkoutByName(coachId, name) != null) {
            throw new SportException("workout with name = '" + name + "' exists int hte system");
        }
        Workout wt = new Workout(coachId, name, description);
        wt.setStatus(WorkoutStatus.NEW);
        wt.setCreationDate(new Date());
        return em.merge(wt);
    }

    @Override
    public void fillWorkout(Long wId, List<Long> existingActivityList) throws SportException {
        if (existingActivityList == null) {
            throw new SportException("fillWorkout: existingActivityList is null");
        }
        Integer n = 0;
        for (Long acId : existingActivityList) {
            Activity a = acMan.cloneActivity(acMan.getActivityById(acId), wId, n);
            em.merge(a);
            n++;
        }
    }

    @Override
    public List<Activity> getWorkoutActivities(Long wId) throws SportException {
        System.out.println("getWorkoutActivities: wId = " + wId);
        if (wId == null) {
            throw new SportException("getWorkoutActivities: wId is null");
        }
        Query q = em.createQuery("select a from Activity a where a.workoutId = :wId order by a.orderNumber asc").setParameter("wId", wId);
        List<Activity> list = q.getResultList();
        System.out.println("getWorkoutActivities: list = " + list);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public Workout getWorkoutById(Long id) throws SportException {
        if (id == null) {
            throw new SportException("getWorkoutActivities: wId is null");
        }
        return em.find(Workout.class, id);
    }

    private Long getTotalDuration(List<Activity> list) throws SportException {
        System.out.println("getTotalDuration: list = " + list);
        CardioUtils.checkNull(list);
        Long sum = 0L;
        for (Activity a : list) {
            sum += (a.getDuration() == null) ? 0 : a.getDuration();
        }
        return sum / 60000;
    }

    @Override
    public JsonWorkout getJsonWorkout(Long workoutId) throws SportException {
        System.out.println("getJsonWorkout: workoutId = " + workoutId);
        if (workoutId == null) {
            return null;

        }
        Workout w = getWorkoutById(workoutId);
        if (w.getParentWorkoutId() != null) {
            return null;
        }
        System.out.println("after getWorkoutById: workout = " + w);
        List<Activity> as = getWorkoutActivities(workoutId);

        JsonWorkout j = new JsonWorkout(workoutId, as, w.getName(), w.getDescription(), (w.getStartDate() == null) ? null : w.getStartDate().getTime(), getTotalDuration(as));
        return j;
    }

    private List<Workout> getCoachWorkouts(Long coachId) throws SportException {
        System.out.println("getCoachWorkouts: coachId = " + coachId);
        CardioUtils.checkNull(coachId);
        System.out.println("getCoachWorkouts: coachId = " + coachId);
        Query q = em.createQuery("select w from Workout w where w.coachId = :cId and w.parentWorkoutId is null order by w.id desc").setParameter("cId", coachId);
        List<Workout> list = q.getResultList();
        System.out.println("getCoachWorkouts: list = " + list);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public boolean canAppointWorkout(Long traineeId) throws SportException {
        CardioUtils.checkNull(traineeId);
        Trainee t = userMan.getTraineeById(traineeId);
        if (t.getCurrentWorkoutId() == null) {
            return true;
        }
        Workout w = getWorkoutById(t.getCurrentWorkoutId());
        if (w.getStatus() == null) {
            return true;
        }
        if (w.getStatus().equals(WorkoutStatus.NEW) || w.getStatus().equals(WorkoutStatus.CURRENT) || w.getStatus().equals(WorkoutStatus.FINISHED)) {
            return true;
        }
        return false;
    }

    @Override
    public List<JsonWorkout> getCoachJsonWorkouts(Long coachId) throws SportException {
        List<Workout> ws = getCoachWorkouts(coachId);
        if (ws == null) {
            return null;
        }
        System.out.println("ws = " + ws);
        List<JsonWorkout> list = new ArrayList();
        for (Workout w : ws) {
            JsonWorkout jw = getJsonWorkout(w.getId());
            if (jw == null) {
                continue;
            }
            list.add(jw);
        }
        System.out.println("json workouts = " + list);
        return list;
    }

    @Override
    public void appointWorkout(Long traineeId, Long workoutId) throws SportException {
        //TODO: check rights
        if (!canAppointWorkout(traineeId)) {
            throw new SportException("can not appoint workout to trainee id=" + traineeId);
        }
        Workout w = getWorkoutById(workoutId);
        Long coachId = w.getCoachId();
        Trainee t = userMan.getTraineeById(traineeId);
        CardioUtils.checkNull(t);
        t.setCurrentWorkoutId(workoutId);
        em.merge(t);
        deleteCurrentWorkouts(traineeId);
        cloneWorkout(workoutId, coachId, traineeId, (new Date()).getTime());
    }

    @Override
    public JsonUserState getInstantState(Long traineeId) throws SportException {
        CardioUtils.checkNull(traineeId);
        Trainee t = userMan.getTraineeById(traineeId);
        Long workoutId = t.getCurrentWorkoutId();
        JsonUserState jus = new JsonUserState();
        jus.setUserId(traineeId);
        if (workoutId == null) {
            return jus;
        }
        Workout w = getWorkoutById(workoutId);
        if (w.getStatus().equals(WorkoutStatus.NEW) || w.getStatus().equals(WorkoutStatus.CURRENT) || w.getStatus().equals(WorkoutStatus.FINISHED)) {
            return jus;
        }
        jus.setDistance(gpsMan.getWorkoutDistance(workoutId));
        jus.setSpeed(gpsMan.getCurrentSpeed(workoutId));
        jus.setPulse(csMan.getCurrentPulse(workoutId));

        return jus;
    }

    @Override
    public List<JsonUserState> getAllUsersState(Long coachId) throws SportException {
        CardioUtils.checkNull(coachId);
        List<Trainee> trainees = coachMan.getTrainees(coachId);
        if (trainees == null || trainees.isEmpty()) {
            return null;
        }
        List<JsonUserState> list = new ArrayList();
        for (Trainee t : trainees) {
            JsonUserState state = getInstantState(t.getId());
            list.add(state);
        }
        return list;
    }

    private void deleteCurrentWorkouts(Long traineeId) {
        Query q = em.createQuery("select w from Workout w where w.traineeId = :tId and w.status = :st").setParameter("tId", traineeId).setParameter("st", WorkoutStatus.CURRENT);
        List<Workout> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return;
        }
        for (Workout w : list) {
            em.remove(w);
        }
    }

    @Override
    public Workout getChildCurrentWorkout(Long workoutId, Long traineeId) throws SportException {
        CardioUtils.checkNull(workoutId);
        Query q = em.createQuery("select w from Workout w where w.parentWorkoutId = :wId "
                + "and w.traineeId = :tId "
                + "and ( w.status = :st1 "
                + " or w.status = :st2 "
                + " or w.status = :st3 )").setParameter("st1", WorkoutStatus.CURRENT).setParameter("st2", WorkoutStatus.IN_PROGRESS).setParameter("st3", WorkoutStatus.PAUSED).setParameter("wId", workoutId).setParameter("tId", traineeId);
        return (Workout) CardioUtils.getSingleResult(q);
    }

    @Override
    public Workout cloneWorkout(Long workoutId, Long coachId, Long traineeId, Long startDateTimestamp) throws SportException {
        CardioUtils.checkNulls(coachId, traineeId, workoutId);
        Workout oldW = getWorkoutById(workoutId);
        Workout newW = new Workout(coachId, oldW.getName(), oldW.getDescription());
        newW.setTraineeId(traineeId);
        newW.setStartDate(new Date(startDateTimestamp));
        newW.setParentWorkoutId(workoutId);
        newW.setStatus(WorkoutStatus.CURRENT);
        newW.setCreationDate(new Date());
        return em.merge(newW);
    }

    @Override
    public Workout startWorkout(Long workoutId, Long traineeId) throws SportException {
        if (workoutId == null) {
            throw new SportException("startWorkout: workoutId = null");
        }
        if (traineeId == null) {
            throw new SportException("startWorkout: traineeId = null");
        }
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);
        realWorkout.setStartDate(new Date());
        realWorkout.setStatus(WorkoutStatus.IN_PROGRESS);

        return em.merge(realWorkout);
    }

    private Activity getLastPause(Long workoutId, Long traineeId) throws SportException {
        Query q = em.createQuery("select a from Activity a "
                + "where a.workoutId = :wId "
                + "  "
                + "and a.type = :tp "
                + "and a.status = :st").setParameter("wId", workoutId).setParameter("tp", ActivityType.PAUSE).setParameter("st", ActivityStatus.IN_PROGRESS);
        return (Activity) CardioUtils.getSingleResult(q);
    }

    private Activity getLastRealActivity(Long workoutId, Long traineeId, ActivityStatus status) throws SportException {
        Query q = em.createQuery("select a from Activity a "
                + "where a.workoutId = :wId "
                + " "
                + " "
                + "and a.status = :st").setParameter("wId", workoutId).setParameter("st", status);
        return (Activity) CardioUtils.getSingleResult(q);
    }

    private List<Activity> getAllUsualInProgressRealChildren(Long activityId, Long traineeId, Long workoutId) {
        Query q = em.createQuery("select a from Activity a "
                + "where a.parentActivityId = :pId "
                + " "
                + "and a.workoutId = :wId "
                + "and a.status = :st "
                + "and a.type = :tp "
                + "order by a.orderNumber desc").setParameter("tp", ActivityType.USUAL).setParameter("st", ActivityStatus.IN_PROGRESS).setParameter("wId", workoutId).setParameter("pId", activityId);
        List<Activity> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public Activity startActivity(Long workoutId, Long activityId, Long traineeId) throws SportException {
        Activity old = acMan.getActivityById(activityId);
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);

        List<Activity> activities = getWorkoutActivities(realWorkout.getId());

        Activity a = acMan.cloneActivity(old, realWorkout.getId(), (activities == null) ? 0 : activities.size());
        a.setType(ActivityType.USUAL);
        a.setStatus(ActivityStatus.IN_PROGRESS);
        a.setParentActivityId(activityId);

//        System.out.println("starting activity... setting orderNumber = " + getRealActivitiesAmount(workoutId, traineeId) );
        a.setOrderNumber(getRealActivitiesAmount(workoutId, traineeId) - 1);

        return em.merge(a);
    }

    private Integer getRealActivitiesAmount(Long workoutId, Long traineeId) throws SportException {
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);
        List<Activity> realActivities = getWorkoutActivities(realWorkout.getId());
        return (realActivities == null) ? 0 : realActivities.size();
    }

    @Override
    public Activity pauseActivity(Long workoutId, Long activityId, Long traineeId, Long duration) throws SportException {
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);
        realWorkout.setStatus(WorkoutStatus.PAUSED);
        realWorkout = em.merge(realWorkout);
        List<Activity> realChildren = getAllUsualInProgressRealChildren(activityId, traineeId, realWorkout.getId());
        if (realChildren == null) {
            throw new SportException("pauseActivity: nothing to pause");
        }
        Activity currentReal = realChildren.get(0);
        List<Activity> realActivities = getWorkoutActivities(realWorkout.getId());

        //stopping current activity
        currentReal.setDuration(duration);
        currentReal.setStatus(ActivityStatus.COMPLETED);
        currentReal = em.merge(currentReal);

        //creating pause activity
        Activity pa = new Activity();
        pa.setParentActivityId(currentReal.getId());
        pa.setCoachId(realChildren.get(0).getCoachId());
        pa.setType(ActivityType.PAUSE);
        pa.setWorkoutId(realWorkout.getId());
        pa.setOrderNumber(realActivities.size());
        pa.setStatus(ActivityStatus.IN_PROGRESS);

        return em.merge(pa);
    }

    @Override
    public Activity unpauseActivity(Long workoutId, Long traineeId, Long duration) throws SportException {
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);
        realWorkout.setStatus(WorkoutStatus.IN_PROGRESS);
        realWorkout = em.merge(realWorkout);
        Activity currentPause = getLastPause(realWorkout.getId(), traineeId);

        //completing paused activity
        currentPause.setDuration(duration);
        currentPause.setName("пауза");
        currentPause.setStatus(ActivityStatus.COMPLETED);
        currentPause = em.merge(currentPause);

        //resuming
        Activity iActivity = acMan.getActivityById(currentPause.getParentActivityId());
        Activity resumedActivity = acMan.cloneActivity(iActivity, currentPause.getWorkoutId(), getRealActivitiesAmount(workoutId, traineeId));
        resumedActivity.setStatus(ActivityStatus.IN_PROGRESS);
        resumedActivity.setParentActivityId(iActivity.getParentActivityId());
        return em.merge(resumedActivity);
    }

    @Override
    public void stopActivity(Long workoutId, Long traineeId, Long activityId, Long duration) throws SportException {
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);
        Activity lastInProgress = getLastRealActivity(realWorkout.getId(), traineeId, ActivityStatus.IN_PROGRESS);

        Activity parentActivity = acMan.getActivityById(lastInProgress.getParentActivityId());

        lastInProgress.setDuration((duration == null || duration.equals(0L)) ? ((parentActivity.getDuration() == null) ? 0 : parentActivity.getDuration()) : duration);


        lastInProgress.setStatus(ActivityStatus.COMPLETED);
        em.merge(lastInProgress);
    }

    @Override
    public void stopWorkout(Long workoutId, Long traineeId) throws SportException {
        Workout realWorkout = getChildCurrentWorkout(workoutId, traineeId);
        realWorkout.setStopDate(new Date());
        realWorkout.setStatus(WorkoutStatus.FINISHED);
        em.merge(realWorkout);
        Trainee t = userMan.getTraineeById(traineeId);
        t.setCurrentWorkoutId(null);
        em.merge(t);
    }

    @Override
    public List<Activity> getCurrentRealActivities(Long traineeId) throws SportException {
        Trainee t = userMan.getTraineeById(traineeId);
        Workout realWorkout = getChildCurrentWorkout(t.getCurrentWorkoutId(), traineeId);

//        Query q = em.createQuery("select a from Activity a "
//                + "where a.workoutId = :wId "
//                + "and a.status != :st1 "
//                + " and a.status != :st2").setParameter("wId", realWorkout.getId()).setParameter("st1", WorkoutStatus.FINISHED).setParameter("st2", WorkoutStatus.CURRENT);
        List<Activity> list = getWorkoutActivities(realWorkout.getId());
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public List<JsonWorkout> getHistoryWorkouts(Long traineeId) throws SportException {
        System.out.println("trying to get history workouts for traineeId = " + traineeId);
        Query q = em.createQuery("select w from Workout w where w.traineeId = :tId"
                + " and ( w.status  = :st1 "
                + " or w.status = :st2 "
                + " or w.status = :st3 ) order by w.id desc ").setParameter("tId", traineeId).setParameter("st1", WorkoutStatus.FINISHED).setParameter("st2", WorkoutStatus.IN_PROGRESS).setParameter("st3", WorkoutStatus.PAUSED);
        List<Workout> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<JsonWorkout> retList = new ArrayList();
        for (Workout w : list) {
            List<Activity> as = getWorkoutActivities(w.getId());
            JsonWorkout jw = new JsonWorkout(w.getId(), as, w.getName(), w.getDescription(), w.getStartDate().getTime(), getTotalDuration(as));
            retList.add(jw);
        }

        return retList;
    }
}
