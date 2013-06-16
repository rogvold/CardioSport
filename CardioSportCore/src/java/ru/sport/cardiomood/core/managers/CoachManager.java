package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.enums.FriendLinkRelation;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Coach;
import ru.sport.cardiomood.core.jpa.FriendLink;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.utils.CardioUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class CoachManager implements CoachManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;
    @EJB
    UserManagerLocal userMan;

    @Override
    public List<Trainee> getTrainees(Long coachId) throws SportException {
        CardioUtils.checkNull(coachId);
        Query q = em.createQuery("select t from Trainee t, FriendLink f where (f.coachId = :coachId) and (f.traineeId = t.id) order by t.id desc").setParameter("coachId", coachId);
        return q.getResultList();
    }

    @Override
    public Trainee createTrainee(Long coachId, String email, String password) throws SportException {
        Trainee t = userMan.registerTrainee(email, password);
        FriendLink f = new FriendLink(coachId, t.getId(), FriendLinkRelation.CREATOR);
        em.merge(f);
        return t;
    }

    @Override
    public boolean areConnected(Long coachId, Long traineeId) throws SportException {
        CardioUtils.checkNull(coachId);
        CardioUtils.checkNull(traineeId);

        Query q = em.createQuery("select f from Trainee t, FriendLink f, Coach c where (f.coachId = c.id) and (f.traineeId = t.id)");
        List<FriendLink> fl = q.getResultList();
        if (fl == null || fl.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteTrainee(Long coachId, Long traineeId) throws SportException {
        if (!areConnected(coachId, traineeId)) {
            throw new SportException("coach and trainee are not connected");
        }
        Trainee t = userMan.getTraineeById(traineeId);
        em.remove(t);
    }

    @Override
    public Trainee updateTrainee(Long coachId, Trainee t) throws SportException {
        CardioUtils.checkNull(t);
        if (t.getId() == null) {
            throw new SportException("can not update trainee: id is null");
        }
        return em.merge(t);
    }

    @Override
    public Coach updateCoach(Coach coach) throws SportException {
        CardioUtils.checkNull(coach);
        if (coach.getId() == null) {
            throw new SportException("can not update coach: id is null");
        }
        return em.merge(coach);
    }

    @Override
    public Trainee createTrainee(Long coachId, String email, String password, String firstName, String lastName) throws SportException {
        Trainee t = createTrainee(coachId, email, password);
        t.setFirstName(firstName);
        t.setLastName(lastName);
        return updateTrainee(coachId, t);
    }
}
