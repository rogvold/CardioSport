package ru.sport.cardiomood.utils;

import java.util.List;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CardioUtils {

    private static String getCallingMethod() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        StackTraceElement ste = null;
        if (stack.length > 1) {
            ste = stack[1];
        }
        return ste == null ? "" : ste.getMethodName();
    }

    public static void checkNull(Object o) throws SportException {
        if (o == null) {
            SportException ex = new SportException(CardioUtils.getCallingMethod() + " object is null");
            ex.printStackTrace();
            throw ex;
        }
    }
    
    public static void checkNulls(Object... obs) throws SportException {
        for (Object o : obs){
            checkNull(o);
        }
    }
    
    

    public static Object getSingleResult(Query q) throws SportException {
        List l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }
}
