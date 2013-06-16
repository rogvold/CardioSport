package ru.sport.cardiomood.core.jpa;

import java.io.Serializable;
import javax.persistence.*;
import ru.sport.cardiomood.core.enums.FriendLinkRelation;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
public class FriendLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long coachId;
    private Long traineeId;
    @Enumerated(EnumType.STRING)
    private FriendLinkRelation relation;

    public FriendLink(Long coachId, Long traineeId, FriendLinkRelation relation) {
        this.coachId = coachId;
        this.traineeId = traineeId;
        this.relation = relation;
    }

    public FriendLink() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public FriendLinkRelation getRelation() {
        return relation;
    }

    public void setRelation(FriendLinkRelation relation) {
        this.relation = relation;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FriendLink)) {
            return false;
        }
        FriendLink other = (FriendLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.sport.cardiomood.core.jpa.FriendLink[ id=" + id + " ]";
    }
}
