package ru.sport.cardiomood.web.beans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.managers.CoachManagerLocal;
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
public class SportsmanBean {

    @EJB
    UserManagerLocal userMan;
    @EJB
    WorkoutManagerLocal workMan;
    @EJB
    CoachManagerLocal coachMan;
    private Long traineeId;
    private Trainee trainee;
    private Long coachId;
    private JsonWorkout planedWorkout;
    private List<Activity> realActivities;

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
        this.coachId = (new JSFHelper()).getUserId();
        if (coachId == null) {
            (new JSFHelper()).redirect("login");
            return;

        }
        if (this.traineeId == null || !coachMan.areConnected(coachId, traineeId)) {
            (new JSFHelper()).redirect("forbidden");
            return;
        }
        this.trainee = userMan.getTraineeById(traineeId);
        this.planedWorkout = (this.trainee.getCurrentWorkoutId() == null) ? null : workMan.getJsonWorkout(this.trainee.getCurrentWorkoutId());
        this.realActivities = workMan.getCurrentRealActivities(traineeId);
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public JsonWorkout getPlanedWorkout() {
        return planedWorkout;
    }

    public void setPlanedWorkout(JsonWorkout planedWorkout) {
        this.planedWorkout = planedWorkout;
    }

    public List<Activity> getRealActivities() {
        return realActivities;
    }

    public void setRealActivities(List<Activity> realActivities) {
        this.realActivities = realActivities;
    }

    public Double round(Double r) {
        return Math.floor(r * 10) / 10;
    }
}
