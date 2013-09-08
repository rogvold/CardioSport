package ru.sport.cardiomood.core.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
@DiscriminatorValue("C")
public class Coach extends User implements Serializable {

    public Coach() {
    }

   

    public Coach(String email, String password, String firstName, String lastName, String phone) {
        super(email, password, firstName, lastName, phone);
    }

    @Override
    public String toString() {
        return "ru.sport.cardiomood.core.jpa.Coach[ id=" + id + " ]";
    }
}
