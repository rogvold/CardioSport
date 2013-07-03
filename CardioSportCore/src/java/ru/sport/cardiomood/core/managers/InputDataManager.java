package ru.sport.cardiomood.core.managers;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.json.entity.JsonInput;

/**
 *
 * @author rogvold
 */
@Stateless
public class InputDataManager implements InputDataManagerLocal {

    @EJB
    CardioSessionManagerLocal cardMan;
    @EJB
    GPSManagerLocal gpsMan;
    @EJB
    WorkoutManagerLocal workMan;

    @Override
    public void processInputData(JsonInput input, Long traineeId) throws SportException {
        if (input == null) {
            throw new SportException("input is null");
        }
        Workout real = workMan.getChildCurrentWorkout(input.getWorkoutId(), traineeId);
        //TODO
        System.out.println("processInputData: traineeId = " + traineeId + " ; wId = ");
        cardMan.addRates(real.getId(), input.getTimestamp(), input.getRr());
        gpsMan.saveGPS(input.getGeo(), real.getId());
    }
}
