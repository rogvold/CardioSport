package ru.sport.cardiomood.web.beans;

import javax.faces.bean.ManagedBean;
import ru.sport.cardiomood.core.enums.ActivityStatus;
import ru.sport.cardiomood.core.enums.ActivityType;
import ru.sport.cardiomood.core.jpa.Activity;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@ManagedBean
public class UtilsBean {

    public Double round(Double r) {
        return Math.floor(r * 10) / 10;
    }

    public boolean pauseType(Activity a) {
        if (a.getType().equals(ActivityType.PAUSE)) {
            return true;
        }
        return false;
    }
    
    public boolean activityIsInProgress(Activity a){
        if (a.getStatus().equals(ActivityStatus.IN_PROGRESS)){
            return true;
        }
        return false;
    }
    
    public boolean activityIsCompleted(Activity a){
        if (a.getStatus().equals(ActivityStatus.COMPLETED)){
            return true;
        }
        return false;
    }
    
}
