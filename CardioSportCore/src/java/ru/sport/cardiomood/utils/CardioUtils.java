package ru.sport.cardiomood.utils;

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
            throw new SportException(CardioUtils.getCallingMethod() + " object is null");
        }
    }
}
