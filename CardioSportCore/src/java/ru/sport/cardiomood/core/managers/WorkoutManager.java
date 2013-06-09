package ru.sport.cardiomood.core.managers;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.enums.WorkoutStatus;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Workout;

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
        if (wId == null) {
            throw new SportException("getWorkoutActivities: wId is null");
        }
        Query q = em.createQuery("select a from Activity a where a.workoutId = :wId order by a.orderNumber asc");
        List<Activity> list = q.getResultList();
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
}
