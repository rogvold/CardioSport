package ru.sport.cardiomood.core.managers;

import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.json.entity.JsonInput;

/**
 *
 * @author rogvold
 */
@Local
public interface InputDataManagerLocal {

    public void processInputData(JsonInput input) throws SportException;
}
