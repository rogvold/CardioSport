package ru.sport.cardiomood.web.beans;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.managers.ActivityManagerLocal;
import ru.sport.cardiomood.core.managers.UserManagerLocal;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
@RequestScoped
public class ActivitiesListBean {

    @EJB
    ActivityManagerLocal actMan;
    private Long coachId;
    private List<Activity> activities;

    @PostConstruct
    private void init() throws SportException {
        this.coachId = (new JSFHelper()).getUserId();
        if (this.coachId == null) {
            (new JSFHelper()).redirect("login");
            return;
        }
        try {
            System.out.println("coachId = " + coachId);
            this.activities = actMan.getCoachActivities(this.coachId);

        } catch (Exception e) {
            System.out.println("ActivitiesListBean: exception = " + e.getMessage());
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }
}
