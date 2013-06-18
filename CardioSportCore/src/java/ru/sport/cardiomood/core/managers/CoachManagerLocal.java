package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Coach;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.utils.CardioUtils;

/**
 *
 * @author rogvold
 */
@Local
public interface CoachManagerLocal {

//    private 
    public List<Trainee> getTrainees(Long coachId) throws SportException;

    public Trainee createTrainee(Long coachId, String email, String password) throws SportException;
    public Trainee createTrainee(Long coachId, String email, String password, String firstName, String lastName) throws SportException;

    public boolean areConnected(Long coachId, Long traineeId) throws SportException;

    public void deleteTrainee(Long coachId, Long traineeId) throws SportException;

    public Trainee updateTrainee(Long coachId, Trainee t) throws SportException;

    public Coach updateCoach(Coach coach) throws SportException;
}
