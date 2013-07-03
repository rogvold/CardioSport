package ru.sport.cardiomood.core.managers;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.GPSEntity;
import ru.sport.cardiomood.utils.CardioUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class GPSManager implements GPSManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;

    @Override
    public List<GPSEntity> getWorkoutGPS(Long workoutId) throws SportException {
        CardioUtils.checkNull(workoutId);
        Query q = em.createQuery("select g from GPSEntity g where g.workoutId = :wId").setParameter("wId", workoutId);
        List<GPSEntity> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return new ArrayList();
        }
        return list;
    }

    private GPSEntity saveGPSEntity(GPSEntity gps) throws SportException {
        if (gps == null) {
            throw new SportException("gps entity is null");
        }
        return em.merge(gps);
    }

    @Override
    public void saveGPS(List<GPSEntity> gps, Long workoutId) throws SportException {
        for (GPSEntity e : gps) {
            e.setWorkoutId(workoutId);
            saveGPSEntity(e);
        }
    }

    /**
     * @param lat1 Latitude which was given by the device's internal GPS or
     * Network location provider of the users location
     * @param lng1 Longitude which was given by the device's internal GPS or
     * Network location provider of the users location
     * @param lat2 Latitude of the object in which the user wants to know the
     * distance they are from
     * @param lng2 Longitude of the object in which the user wants to know the
     * distance they are from
     * @return Distance from which the user is located from the specified target
     */
    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }

    @Override
    public Double getWorkoutDistance(Long workoutId) throws SportException {
        List<GPSEntity> ge = getWorkoutGPS(workoutId);
        double d = 0;
        if (ge.isEmpty()) {
            return 0.0;
        }
        GPSEntity prev = ge.get(0);
        for (GPSEntity g : ge) {
            d += distFrom(prev.getLatitude(), prev.getLongitude(), g.getLatitude(), g.getLongitude());
            prev = g;
        }
        return d;
    }

    @Override
    public Double getCurrentSpeed(Long workoutId) throws SportException {
        List<GPSEntity> ge = getWorkoutGPS(workoutId);
        if (ge.isEmpty()) {
            return 0.0;
        }
        return (ge.get(ge.size() - 1).getSpeed() == null) ? 0.0 : ge.get(ge.size() - 1).getSpeed();
    }
}
