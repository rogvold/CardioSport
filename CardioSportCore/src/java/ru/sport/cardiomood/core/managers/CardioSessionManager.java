package ru.sport.cardiomood.core.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.CardioSession;
import ru.sport.cardiomood.json.entity.JsonSession;
import ru.sport.cardiomood.utils.CardioUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class CardioSessionManager implements CardioSessionManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;
    @EJB
    WorkoutManagerLocal workMan;

    private CardioSession getCardioSessionByWorkoutIdNotCreatingNewCardioSessionIfCardioSessionWithGivenWorkoutIdDoesNotExist(Long workoutId) throws SportException {
        Query q = em.createQuery("select c from CardioSession c where c.workoutId = :wId").setParameter("wId", workoutId);
        List<CardioSession> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public CardioSession getCardioSessionByWorkoutId(Long workoutId) throws SportException {
        CardioSession c = getCardioSessionByWorkoutIdNotCreatingNewCardioSessionIfCardioSessionWithGivenWorkoutIdDoesNotExist(workoutId);
        if (c == null) {
            c = new CardioSession();
            c.setWorkoutId(workoutId);
            return em.merge(c);
        }
        return c;
    }

    @Override
    public void addRates(Long workoutId, Long timestamp, List<Integer> rates) throws SportException {
        CardioSession c = getCardioSessionByWorkoutId(workoutId);
        if (c.getStartDate() == null) {
            c.setStartDate(timestamp);
        }

        //FFUUUCCCKKK
        if (c.getStartDate() == null) {
            c.setStartDate(new Date().getTime());
        }


        List<Integer> list = c.getRates();
        list.addAll(rates);
        c.setRates(list);
        em.merge(c);
    }

    private List<Integer> getRatesInRange(CardioSession cs, Long start, Long duration) {
        if (start == null || duration == null) {
            return null;
        }
        Long c = cs.getStartDate();
        Long end = start + duration;
        List<Integer> list = new ArrayList();
        for (Integer rate : cs.getRates()) {
            if ((c > start) && (c < end)) {
                list.add(rate);
            }
            c += rate;
        }
        return list;
    }

    private List<JsonSession> getTraineeActivitiesSessions(CardioSession cs, List<Activity> as) {
        if (as == null || as.isEmpty() || cs.getStartDate() == null) {
            return null;
        }
        Long c = cs.getStartDate();
        System.out.println("cs start date = " + c);
        List<JsonSession> retList = new ArrayList();
        for (Activity a : as) {
            a.setDuration((a.getDuration() == null) ? 0 : a.getDuration());
            JsonSession newCs = new JsonSession(getRatesInRange(cs, c, a.getDuration()), a.getId(), cs.getWorkoutId(), c, a.getMinHeartRate(), a.getMaxHeartRate(), a.getMinTension(), a.getMaxTension());
            System.out.println("cs for activity aId = " + a.getId() + "/ cs = " + newCs);
            retList.add(newCs);
            c += a.getDuration();
        }
        return retList;
    }

    @Override
    public Integer getCurrentPulse(Long workoutId) throws SportException {
        CardioSession cs = getCardioSessionByWorkoutId(workoutId);
        if (cs.getRates() == null || cs.getRates().isEmpty()) {
            return 0;
        } else {
            List<Integer> rates = cs.getRates();
            return (int) Math.floor(60000 / rates.get(rates.size() - 1));
        }
    }

    @Override
    public List<JsonSession> getWorkoutActivitiesSessions(Long workoutId) throws SportException {
        System.out.println("getWorkoutActivitiesSessions: workoutId = " + workoutId);
        CardioUtils.checkNull(workoutId);
        CardioSession cs = getCardioSessionByWorkoutId(workoutId);
        System.out.println("cs = " + cs);
        List<Activity> activities = workMan.getWorkoutActivities(workoutId);
        System.out.println("getWorkoutActivitiesSessions: activities = " + activities);
        return getTraineeActivitiesSessions(cs, activities);
    }

    @Override
    public List<Integer> getLastIntervals(Long workoutId, Long span) throws SportException {
        CardioSession cs = getCardioSessionByWorkoutId(workoutId);
        Long s = 0l;
        if (cs.getRates() == null || cs.getRates().isEmpty()) {
            return null;
        } else {
            List<Integer> rates = cs.getRates();
            List<Integer> list = new ArrayList();
            for (Integer l : rates){
                if (s >= span){
                    break;
                }
                list.add(l);
                s+=l;
            }
            return list;
        }
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
