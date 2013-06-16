package ru.sport.cardiomood.utils;

import java.util.ArrayList;
import java.util.List;
import ru.sport.cardiomood.core.jpa.Activity;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class ActivityUtils {
    
    private static Double getPrettyDouble(Double d) {
        return Math.round(d * 100) / 100.0;
    }
    
    public static List<Activity> prepareActivities(List<Activity> list) {
        if (list == null) {
            return null;
        }
        List<Activity> nList = new ArrayList();
        for (Activity a : list) {
            a.setDuration((a.getDuration() == null) ? null : (a.getDuration() / 60000));
            a.setMaxSpeed((a.getMaxSpeed() == null) ? null : getPrettyDouble(a.getMaxSpeed() * 3.6));
            a.setMinSpeed((a.getMinSpeed() == null) ? null : getPrettyDouble(a.getMinSpeed() * 3.6));
            nList.add(a);
        }
        return nList;
    }
    
    public static List<Activity> cloneActivitiesList(List<Activity> list) {
        if (list == null) {
            return null;
        }
        List<Activity> nList = new ArrayList();
        for (Activity a : list) {
            Activity ac = new Activity(a.getCoachId(), a.getMinHeartRate(), a.getMaxHeartRate(), a.getMinTension(), a.getMaxTension(), a.getDuration(), a.getName(), a.getDescription(), a.getWorkoutId(), a.getMinSpeed(), a.getMaxSpeed());
            ac.setId(a.getId());
            nList.add(ac);
            
        }
        return nList;
    }
}
