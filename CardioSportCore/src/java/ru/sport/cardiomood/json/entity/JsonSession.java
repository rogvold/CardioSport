package ru.sport.cardiomood.json.entity;

import java.util.List;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class JsonSession {

    private List<Integer> rates;
    private Long activityId;
    private Long workoutId;
    private Long start;
    private Integer minPulse;
    private Integer maxPulse;
    private Double minTension;
    private Double maxTension;

    public JsonSession(List<Integer> rates, Long activityId, Long workoutId, Long start) {
        this.rates = rates;
        this.activityId = activityId;
        this.workoutId = workoutId;
        this.start = start;
    }

    public JsonSession(List<Integer> rates, Long activityId, Long workoutId, Long start, Integer minPulse, Integer maxPulse, Double minTension, Double maxTension) {
        this.rates = rates;
        this.activityId = activityId;
        this.workoutId = workoutId;
        this.start = start;
        this.minPulse = minPulse;
        this.maxPulse = maxPulse;
        this.minTension = minTension;
        this.maxTension = maxTension;
    }

    public JsonSession() {
    }

    public List<Integer> getRates() {
        return rates;
    }

    public void setRates(List<Integer> rates) {
        this.rates = rates;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public Integer getMaxPulse() {
        return maxPulse;
    }

    public void setMaxPulse(Integer maxPulse) {
        this.maxPulse = maxPulse;
    }

    public Double getMaxTension() {
        return maxTension;
    }

    public void setMaxTension(Double maxTension) {
        this.maxTension = maxTension;
    }

    public Integer getMinPulse() {
        return minPulse;
    }

    public void setMinPulse(Integer minPulse) {
        this.minPulse = minPulse;
    }

    public Double getMinTension() {
        return minTension;
    }

    public void setMinTension(Double minTension) {
        this.minTension = minTension;
    }

    @Override
    public String toString() {
        return "JsonSession{" + "rates=" + rates + ", activityId=" + activityId + ", workoutId=" + workoutId + ", start=" + start + ", minPulse=" + minPulse + ", maxPulse=" + maxPulse + ", minTension=" + minTension + ", maxTension=" + maxTension + '}';
    }
}
