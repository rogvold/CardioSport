package ru.sport.cardiomood.web.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.managers.ActivityManagerLocal;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
@RequestScoped
public class ActivityCreationBean {

    @EJB
    ActivityManagerLocal actMan;
    private Long coachId;
    private Activity activity;

    @PostConstruct
    private void init() throws SportException {
        this.coachId = (new JSFHelper()).getUserId();
        if (this.coachId == null) {
            (new JSFHelper()).redirect("login");
            return;
        }
        this.activity = new Activity();
    }

    public void createActivity() throws SportException {
        try {

            actMan.createActivity(coachId, activity.getMinHeartRate(), activity.getMaxHeartRate(), activity.getMinTension(), activity.getMaxTension(), activity.getDuration(), activity.getName(), activity.getDescription(), null, activity.getMinSpeed(), activity.getMaxSpeed());
        } catch (Exception e) {
            System.out.println("createActivity: e = " + e.getMessage());
        }

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
