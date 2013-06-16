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
@ViewScoped
public class TraineeCreationBean implements Serializable{

    @EJB
    UserManagerLocal userMan;
    @EJB
    CoachManagerLocal coachMan;
    private Long coachId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @PostConstruct
    private void init() throws SportException {
        this.coachId = (new JSFHelper()).getUserId();
        if (this.coachId == null) {
            (new JSFHelper()).redirect("login");
        }
    }

    public void createTrainee() throws SportException {
        System.out.println("creating trainee: coachId = " + coachId);
        coachMan.createTrainee(coachId, email, password, firstName, lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCoachId() {
        return coachId;
    }
}
