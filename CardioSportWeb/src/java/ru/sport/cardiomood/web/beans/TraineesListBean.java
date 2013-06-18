package ru.sport.cardiomood.web.beans;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.managers.CoachManagerLocal;
import ru.sport.cardiomood.core.managers.UserManagerLocal;
import ru.sport.cardiomood.web.utils.JSFHelper;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
//@ViewScoped
public class TraineesListBean implements Serializable{

    @EJB
    UserManagerLocal userMan;
    @EJB
    CoachManagerLocal coachMan;
    private Long coachId;
    private List<Trainee> trainees;

    @PostConstruct
    private void init() throws SportException {
        this.coachId = (new JSFHelper()).getUserId();
        if (this.coachId == null) {
            (new JSFHelper()).redirect("login");
        } else {
            this.trainees = coachMan.getTrainees(coachId);
        }
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public Long getCoachId() {
        return coachId;
    }
}
