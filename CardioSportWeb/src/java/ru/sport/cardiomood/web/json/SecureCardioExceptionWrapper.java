

package ru.sport.cardiomood.web.json;

import com.google.gson.Gson;
import ru.sport.cardiomood.core.constants.ResponseConstants;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.json.entity.JsonError;
import ru.sport.cardiomood.json.entity.JsonResponse;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class SecureCardioExceptionWrapper {
    
    public static String wrapException(SportException exc){
        return (new Gson()).toJson( new JsonResponse(ResponseConstants.ERROR, new JsonError(exc.getMessage(), exc.getErrorCode()), null));
    }

}
