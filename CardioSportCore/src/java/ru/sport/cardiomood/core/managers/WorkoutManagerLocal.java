package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Workout;

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
    
}
