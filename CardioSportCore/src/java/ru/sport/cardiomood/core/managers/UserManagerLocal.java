package ru.sport.cardiomood.core.managers;

import javax.ejb.Local;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Coach;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.jpa.User;

/**
 *
 * @author rogvold
 */
@Local
public interface UserManagerLocal {

    public User getUserById(Long userId) throws SportException;
    
    public Trainee getTraineeById(Long userId) throws SportException;
    public Trainee getTraineeByEmail(String email) throws SportException;
    
    public Coach getCoachById(Long userId) throws SportException;
    
    public Long getUserIdByEmai(String email) throws SportException;

    public void registerCoach(String email, String password) throws SportException;

    public Trainee registerTrainee(String email, String password) throws SportException;

    public void checkEmailThrowingException(String email) throws SportException;

    public boolean userExists(String email) throws SportException;

    public boolean checkLoginInfo(String email, String password) throws SportException;

    public Coach updateCoach(Coach c) throws SportException;
}
