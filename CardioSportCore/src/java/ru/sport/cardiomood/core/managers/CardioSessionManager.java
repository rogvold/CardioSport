package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.CardioSession;

/**
 *
 * @author rogvold
 */
@Stateless
public class CardioSessionManager implements CardioSessionManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;

    private CardioSession getCardioSessionByWorkoutIdNotCreatingNewCardioSessionIfCardioSessionWithGivenWorkoutIdDoesNotExist(Long workoutId) throws SportException {
        Query q = em.createQuery("select c from CardioSession c where c.workoutId = :wId").setParameter("wId", workoutId);
        List<CardioSession> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public CardioSession getCardioSessionByWorkoutId(Long workoutId) throws SportException {
        CardioSession c = getCardioSessionByWorkoutIdNotCreatingNewCardioSessionIfCardioSessionWithGivenWorkoutIdDoesNotExist(workoutId);
        if (c == null) {
            c = new CardioSession();
            c.setWorkoutId(workoutId);
            return em.merge(c);
        }
        return c;
    }

    @Override
    public void addRates(Long workoutId, Long timestamp, List<Integer> rates) throws SportException {
        CardioSession c = getCardioSessionByWorkoutId(workoutId);
        if (c.getStartDate() == null) {
            c.setStartDate(timestamp);
        }
        List<Integer> list = c.getRates();
        list.addAll(rates);
        c.setRates(list);
        em.merge(c);
    }
}
