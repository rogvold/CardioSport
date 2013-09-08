package ru.sport.cardiomood.core.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
@DiscriminatorValue("T")
public class Trainee extends User implements Serializable {

    private Long currentWorkoutId;
    private Double metronomeRate;

    public Trainee() {
    }

    public Trainee(String email, String password, String firstName, String lastName, String phone) {
        super(email, password, firstName, lastName, phone);
    }

    public Long getCurrentWorkoutId() {
        return currentWorkoutId;
    }

    public void setCurrentWorkoutId(Long currentWorkoutId) {
        this.currentWorkoutId = currentWorkoutId;
    }

    public Double getMetronomeRate() {
        return metronomeRate;
    }

    public void setMetronomeRate(Double metronomeRate) {
        this.metronomeRate = metronomeRate;
    }

    @Override
    public String toString() {
        return "ru.sport.cardiomood.core.jpa.Trainee[ id=" + id + " ]";
    }
}
