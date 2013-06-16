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
        Query q = em.createQuery("select g from GPSEntity g where g.workoutId = : wId").setParameter("wId", workoutId);
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
    public void saveGPS(List<GPSEntity> gps) throws SportException {
        for (GPSEntity e : gps) {
            saveGPSEntity(e);
        }
    }
}
