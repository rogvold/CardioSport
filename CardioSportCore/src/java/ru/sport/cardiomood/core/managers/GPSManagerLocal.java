package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.GPSEntity;

/**
 *
 * @author rogvold
 */
@Local
public interface GPSManagerLocal {

    public List<GPSEntity> getWorkoutGPS(Long workoutId) throws SportException;
    
    public void saveGPS(List<GPSEntity> gps, Long workoutId) throws SportException;
    
    public Double getWorkoutDistance(Long workoutId) throws SportException;

    public Double getCurrentSpeed(Long workoutId) throws SportException;

}
