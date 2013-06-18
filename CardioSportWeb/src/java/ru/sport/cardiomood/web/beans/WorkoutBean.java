package ru.sport.cardiomood.web.beans;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.core.managers.WorkoutManagerLocal;
import ru.sport.cardiomood.json.entity.JsonWorkout;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
@ViewScoped
public class WorkoutBean {

    @EJB
    WorkoutManagerLocal workMan;
    private Long coachId;
    private List<JsonWorkout> workouts;

    @PostConstruct
    private void init() {
        this.coachId = (new JSFHelper()).getUserId();
        System.out.println("WorkoutBean: init: coachId = " + this.getCoachId());
        if (this.coachId == null) {
//            (new JSFHelper()).redirect("login");
            return;
        }
        try {
            this.workouts = workMan.getCoachJsonWorkouts(coachId);
        } catch (SportException ex) {
            System.out.println("WorkoutBean init: exc = " + ex.getMessage());
        }
    }

    public Workout workoutById(Long workoutId) throws SportException {
        return workMan.getWorkoutById(workoutId);
    }

    public String shortWorkoutInfo(Long workoutId) throws SportException {
        String s = "";
        List<Activity> as = workMan.getWorkoutActivities(workoutId);
        int n = 1;
        for (Activity a : as) {
            s += n + ")" + a.getName() + "(" + a.getDuration() / 60000 + " мин.); ";
            n++;
        }
        return s.substring(0, s.length() - 2);
    }

    public boolean canAppointWorkout(Long traineeId) throws SportException {
        return workMan.canAppointWorkout(traineeId);
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public List<JsonWorkout> getWorkouts() {
        return workouts;
    }
}
