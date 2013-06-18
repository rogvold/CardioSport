package ru.sport.cardiomood.json.entity;

import java.util.List;
import ru.sport.cardiomood.core.jpa.Activity;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class JsonWorkout {

    private List<Activity> activities;
    private String name;
    private String description;
    private Long startDate;
    private Long id;
    private Long duration;

    public JsonWorkout() {
    }

    public JsonWorkout(Long id, List<Activity> activities, String name, String description, Long startDate, Long duration) {
        this.activities = activities;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.id = id;
        this.duration = duration;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
