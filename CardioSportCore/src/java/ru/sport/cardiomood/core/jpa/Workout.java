package ru.sport.cardiomood.core.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import ru.sport.cardiomood.core.enums.WorkoutStatus;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
public class Workout implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long coachId;
    @Column(length = 10000)
    private String description;
    @Column(length = 1000)
    private String name;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModified;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date stopDate;
    @Enumerated(EnumType.STRING)
    private WorkoutStatus status;

    public Workout(Long coachId, String name, String description) {
        this.coachId = coachId;
        this.description = description;
        this.name = name;
    }

    public Workout() {
    }

    public WorkoutStatus getStatus() {
        return status;
    }

    public void setStatus(WorkoutStatus status) {
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
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
        if (!(object instanceof Workout)) {
            return false;
        }
        Workout other = (Workout) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.sport.cardiomood.core.jpa.Workout[ id=" + id + " ]";
    }
}
