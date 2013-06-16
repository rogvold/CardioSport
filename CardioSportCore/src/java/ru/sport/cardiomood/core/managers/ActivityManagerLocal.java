package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;

/**
 *
 * @author rogvold
 */
@Local
public interface ActivityManagerLocal {

    public Activity getActivityById(Long activityId) throws SportException;

    public Activity createActivity(Long userId, Integer minHeartRate, Integer maxHeartRate, Double minTension, Double maxTension, Long duration, String name, String description, Long workoutId, Double minSpeed, Double maxSpeed) throws SportException;

    public Activity updateActivity(Activity uActivity) throws SportException;

    public void deleteActivity(Long activityId) throws SportException;

    public Activity cloneActivity(Activity oldActivity, Long workoutId, Integer orderNumber) throws SportException;

    public List<Activity> getCoachActivities(Long coachId) throws SportException;

    public List<Activity> addActivitiesToWorkout(Long workoutId, List<Long> activities) throws SportException;
}
