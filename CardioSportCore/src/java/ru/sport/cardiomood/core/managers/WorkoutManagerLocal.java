package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.json.entity.JsonUserState;
import ru.sport.cardiomood.json.entity.JsonWorkout;

/**
 *
 * @author rogvold
 */
@Local
public interface WorkoutManagerLocal {

    public Workout createWorkout(Long coachId, String name, String description) throws SportException;

    public void fillWorkout(Long wId, List<Long> existingActivityList) throws SportException;

    public List<Activity> getWorkoutActivities(Long wId) throws SportException;

    public Workout getWorkoutById(Long id) throws SportException;

    public JsonWorkout getJsonWorkout(Long workoutId) throws SportException;

    public List<JsonWorkout> getCoachJsonWorkouts(Long coachId) throws SportException;

    public void appointWorkout(Long traineeId, Long workoutId) throws SportException;

    public boolean canAppointWorkout(Long traineeId) throws SportException;

    public JsonUserState getInstantState(Long traineeId) throws SportException;

    public List<JsonUserState> getAllUsersState(Long coachId) throws SportException;

    public Workout getChildCurrentWorkout(Long workoutId, Long traineeId) throws SportException;

    public Workout cloneWorkout(Long workoutId, Long coachId, Long traineeId, Long startDateTimestamp) throws SportException;
    
    public Workout startWorkout(Long workoutId, Long traineeId) throws SportException;
    
    public Activity startActivity(Long workoutId, Long activityId, Long traineeId) throws SportException;

    public Activity pauseActivity(Long workoutId, Long activityId, Long traineeId, Long duration) throws SportException;
    
    public Activity unpauseActivity(Long workoutId, Long traineeId, Long duration) throws SportException;
    
    public void stopActivity(Long workoutId, Long traineeId, Long activityId, Long duration) throws SportException;
    
    public void stopWorkout(Long workoutId, Long traineeId) throws SportException;
    
    public List<Activity> getCurrentRealActivities(Long traineeId) throws SportException;



}
