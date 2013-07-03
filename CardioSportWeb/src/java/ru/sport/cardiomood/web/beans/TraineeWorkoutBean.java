package ru.sport.cardiomood.web.beans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.managers.UserManagerLocal;
import ru.sport.cardiomood.core.managers.WorkoutManagerLocal;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
@ViewScoped
public class TraineeWorkoutBean {

    @EJB
    WorkoutManagerLocal workMan;
    @EJB
    UserManagerLocal userMan;
    private Long coachId;
    private Long traineeId;
    private Long workoutId;
    private List<Activity> workoutActivities;
    private Trainee trainee;

    private Long getLong(String paramName) {
        String s = (new JSFHelper()).getExternalContext().getRequestParameterMap().get(paramName);
        if (s == null) {
            return null;
        }
        return Long.parseLong(s);
    }

    @PostConstruct
    private void init() throws SportException {
        this.coachId = (new JSFHelper()).getUserId();
        this.traineeId = getLong("traineeId");
        this.workoutId = getLong("workoutId");

        this.trainee = userMan.getTraineeById(traineeId);

    }

    public List<Activity> getWorkoutActivities() {
        return workoutActivities;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public Trainee getTrainee() {
        return trainee;
    }
}
