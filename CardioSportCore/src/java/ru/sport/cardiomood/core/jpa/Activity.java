package ru.sport.cardiomood.core.jpa;

import java.io.Serializable;
import javax.persistence.*;
import ru.sport.cardiomood.core.enums.ActivityStatus;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer minHeartRate;
    private Integer maxHeartRate;
    private Double minTension;
    private Double maxTension;
    private Long duration;
    @Column(length = 1000)
    private String name;
    @Column(length = 10000)
    private String description;
    private Long workoutId;
    private Double minSpeed;
    private Double maxSpeed;
    private Integer orderNumber;
    private ActivityStatus status;
    private Long coachId;

    public Activity() {
    }

    public Activity(Long coachId, Integer minHeartRate, Integer maxHeartRate, Double minTension, Double maxTension, Long duration, String name, String description, Long workoutId, Double minSpeed, Double maxSpeed) {
        this.minHeartRate = minHeartRate;
        this.maxHeartRate = maxHeartRate;
        this.minTension = minTension;
        this.maxTension = maxTension;
        this.duration = duration;
        this.name = name;
        this.description = description;
        this.workoutId = workoutId;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.coachId = coachId;
        this.status = ActivityStatus.NEW;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(Integer maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public Double getMaxTension() {
        return maxTension;
    }

    public void setMaxTension(Double maxTension) {
        this.maxTension = maxTension;
    }

    public Integer getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(Integer minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public Double getMinTension() {
        return minTension;
    }

    public void setMinTension(Double minTension) {
        this.minTension = minTension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.sport.cardiomood.core.jpa.Activity[ id=" + id + " ]";
    }
}
