package ru.sport.cardiomood.json.entity;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class JsonUserState {

    private Long userId;
    private Double speed;
    private Integer pulse;
    private Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JsonUserState{" + "userId=" + userId + ", speed=" + speed + ", pulse=" + pulse + ", distance=" + distance + '}';
    }
}
