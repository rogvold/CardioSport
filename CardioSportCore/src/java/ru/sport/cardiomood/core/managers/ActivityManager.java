package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;

/**
 *
 * @author rogvold
 */
@Stateless
public class ActivityManager implements ActivityManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;

    @Override
    public Activity getActivityById(Long activityId) throws SportException {
        if (activityId == null) {
            throw new SportException("getActivityById: activityId is null");
        }
        return em.find(Activity.class, activityId);
    }

    private Activity getActivityByName(String name, Long wId) {
        if (name == null) {
            System.out.println("getActivityByName: name is null");
            return null;
        }
        Query q = em.createQuery("select a from Activity a where (a.name = :name) and (a.workoutId = :wId)").setParameter("name", name).setParameter("wId", wId);
        List<Activity> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public Activity createActivity(Integer minHeartRate, Integer maxHeartRate, Double minTension, Double maxTension, Long duration, String name, String description, Long workoutId, Double minSpeed, Double maxSpeed) throws SportException {
        if (getActivityByName(name, workoutId) != null) {
            throw new SportException("activity with name='" + name + "' already exists in the system");
        }
        Activity a = new Activity(minHeartRate, maxHeartRate, minTension, maxTension, duration, name, description, workoutId, minSpeed, maxSpeed);
        return em.merge(a);
    }

    @Override
    public Activity updateActivity(Activity uActivity) throws SportException {
        if (uActivity == null) {
            return null;
        }
        return em.merge(uActivity);
    }

    @Override
    public void deleteActivity(Long activityId) throws SportException {
        Activity activity = getActivityById(activityId);
        if (activity == null) {
            throw new SportException("deleteActivity: there is no activity with id=" + activityId + " in system");
        }
        em.remove(activity);
    }

    @Override
    public  Activity cloneActivity(Activity oldActivity, Long workoutId, Integer orderNumber) {
        Activity a = new Activity(oldActivity.getMinHeartRate(), oldActivity.getMaxHeartRate(), oldActivity.getMinTension(), oldActivity.getMaxTension(), oldActivity.getDuration(), oldActivity.getName(), oldActivity.getDescription(), workoutId, oldActivity.getMinSpeed(), oldActivity.getMaxSpeed());
        a.setOrderNumber(orderNumber);
        return em.merge(a);
    }
}
