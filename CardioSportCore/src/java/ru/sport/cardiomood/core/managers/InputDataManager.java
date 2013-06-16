package ru.sport.cardiomood.core.managers;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import ru.sport.cardiomood.core.exceptions.SportException;
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

    @Override
    public void processInputData(JsonInput input) throws SportException {
        if (input == null) {
            throw new SportException("input is null");
        }
        cardMan.addRates(input.getWorkoutId(), input.getTimestamp(), input.getRr());
        gpsMan.saveGPS(input.getGeo());
    }
}
