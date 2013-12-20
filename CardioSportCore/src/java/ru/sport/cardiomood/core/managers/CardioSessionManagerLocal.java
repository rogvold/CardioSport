package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.CardioSession;
import ru.sport.cardiomood.json.entity.JsonSession;

/**
 *
 * @author rogvold
 */
@Local
public interface CardioSessionManagerLocal {

    public CardioSession getCardioSessionByWorkoutId(Long workoutId) throws SportException;

    public void addRates(Long workoutId, Long timestamp, List<Integer> rates) throws SportException;

    public Integer getCurrentPulse(Long workoutId) throws SportException;

    public List<JsonSession> getWorkoutActivitiesSessions(Long workoutId) throws SportException;
    
    public List<Integer> getLastIntervals(Long workoutId, Long span) throws SportException;
}
