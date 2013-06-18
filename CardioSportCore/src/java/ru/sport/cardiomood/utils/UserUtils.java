package ru.sport.cardiomood.utils;

import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.managers.UserManagerLocal;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class UserUtils {

    public static Trainee getTraineeByEmailAndPassword(UserManagerLocal userMan, String email, String password) throws SportException {
        if (!userMan.checkLoginInfo(email, password)) {
            throw new SportException("incorrect pair email/password");
        };
        return userMan.getTraineeByEmail(email);
    }
}
