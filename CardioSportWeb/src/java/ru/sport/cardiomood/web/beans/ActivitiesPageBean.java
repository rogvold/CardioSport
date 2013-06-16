package ru.sport.cardiomood.web.beans;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.managers.ActivityManagerLocal;
import ru.sport.cardiomood.utils.ActivityUtils;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
@ViewScoped
public class ActivitiesPageBean implements Serializable {

    @EJB
    ActivityManagerLocal actMan;
    private Long coachId;
    private Activity activity;
    private List<Activity> activities;

    @PostConstruct
    private void init() throws SportException {
        this.coachId = (new JSFHelper()).getUserId();
        if (this.coachId == null) {
            (new JSFHelper()).redirect("login");
            return;
        }
        this.activity = new Activity();
        updateList();
    }

    private void updateList() throws SportException {
        System.out.println("updateList: coachId = " + coachId);
        try {
            this.activities = ActivityUtils.cloneActivitiesList(actMan.getCoachActivities(this.coachId));
            prepareActivities(activities);

        } catch (Exception e) {
            System.out.println("updateList: exc = " + e.getMessage());
        }
    }

    private Double getPrettyDouble(Double d) {
        return Math.round(d * 100) / 100.0;
    }

    private void prepareActivities(List<Activity> list) {
        if (list == null) {
            return;
        }
        for (Activity a : list) {
            a.setDuration((a.getDuration() == null) ? null : (a.getDuration() / 60000));
            a.setMaxSpeed((a.getMaxSpeed() == null) ? null : getPrettyDouble(a.getMaxSpeed() * 3.6));
            a.setMinSpeed((a.getMinSpeed() == null) ? null : getPrettyDouble(a.getMinSpeed() * 3.6));
        }
    }

    public void createActivity() throws SportException {
        try {
            Activity a = new Activity();

            a.setDuration((activity.getDuration() == null) ? 0 : activity.getDuration() * 60 * 1000);
            a.setMaxSpeed((activity.getMaxSpeed() == null) ? 0 : getPrettyDouble(activity.getMaxSpeed() / 3.6));
            a.setMinSpeed((activity.getMinSpeed() == null) ? 0 : getPrettyDouble(activity.getMinSpeed() / 3.6));

            a.setMinTension((activity.getMinSpeed() == null) ? 0 : activity.getMinTension());
            a.setMaxTension((activity.getMaxTension() == null) ? 0 : activity.getMaxTension());

            a.setMinHeartRate((activity.getMinHeartRate() == null) ? 0 : activity.getMinHeartRate());
            a.setMaxHeartRate((activity.getMaxHeartRate() == null) ? 0 : activity.getMaxHeartRate());

            actMan.createActivity(coachId, a.getMinHeartRate(), a.getMaxHeartRate(), a.getMinTension(), a.getMaxTension(), a.getDuration(), activity.getName(), activity.getDescription(), null, a.getMinSpeed(), a.getMaxSpeed());
            updateList();
        } catch (Exception e) {
            System.out.println("createActivity: e = " + e.getMessage());
        }

    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }
}
