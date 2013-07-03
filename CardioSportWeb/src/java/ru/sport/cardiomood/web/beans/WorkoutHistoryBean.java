package ru.sport.cardiomood.web.beans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.managers.UserManagerLocal;
import ru.sport.cardiomood.core.managers.WorkoutManagerLocal;
import ru.sport.cardiomood.json.entity.JsonWorkout;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
@ViewScoped
public class WorkoutHistoryBean {

    @EJB
    WorkoutManagerLocal workMan;
    @EJB
    UserManagerLocal userMan;
    private List<JsonWorkout> workouts;
    private Long traineeId;
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
        this.traineeId = getLong("id");
        this.trainee = userMan.getTraineeById(traineeId);
        this.workouts = workMan.getHistoryWorkouts(traineeId);
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public List<JsonWorkout> getWorkouts() {
        return workouts;
    }
}
