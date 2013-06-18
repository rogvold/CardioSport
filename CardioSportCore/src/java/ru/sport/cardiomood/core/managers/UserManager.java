package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Coach;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.jpa.User;
import ru.sport.cardiomood.utils.CardioUtils;
import ru.sport.cardiomood.utils.StringUtils;

/**
 *
 * @author rogvold
 */
@Stateless
public class UserManager implements UserManagerLocal {

    @PersistenceContext(unitName = "CardioSportCorePU")
    EntityManager em;

    @Override
    public User getUserById(Long userId) throws SportException {
        if (userId == null) {
            throw new SportException("getUserById: userId is null");
        }
        return em.find(User.class, userId);
    }

    @Override
    public Trainee getTraineeById(Long userId) throws SportException {
        CardioUtils.checkNull(userId);
        return em.find(Trainee.class, userId);
    }

    @Override
    public Trainee getTraineeByEmail(String email) throws SportException {
        checkEmail(email);
        Query q = em.createQuery("select t from Trainee t where t.email = :email").setParameter("email", email);
        List<Trainee> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Coach getCoachById(Long userId) throws SportException {
        CardioUtils.checkNull(userId);
        return em.find(Coach.class, userId);
    }

    @Override
    public void registerCoach(String email, String password) throws SportException {
        checkEmailThrowingException(email);
        checkPassword(password);
        Coach c = new Coach(email, password, null, null, null);
        em.merge(c);
    }

    @Override
    public Trainee registerTrainee(String email, String password) throws SportException {
        checkEmailThrowingException(email);
        checkPassword(password);
        Trainee t = new Trainee(email, password, null, null, null);
        return em.merge(t);
    }

    @Override
    public void checkEmailThrowingException(String email) throws SportException {
        if (userExists(email)) {
            throw new SportException("user with email=" + email + " exists in the system");
        }
    }

    @Override
    public boolean userExists(String email) throws SportException {
        checkEmail(email);
        Query q = em.createQuery("select u from User u where u.email = :email").setParameter("email", email);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    private void checkEmail(String email) throws SportException {
        if (email == null) {
            throw new SportException("email is null");
        }
        if (!StringUtils.isValidEmail(email)) {
            throw new SportException("email is invalid");
        }
    }

    private void checkPassword(String password) throws SportException {
        if (password == null) {
            throw new SportException("password is null");
        }
    }

    @Override
    public boolean checkLoginInfo(String email, String password) throws SportException {
        System.out.println("checkLoginInfo: email/password = " + email+"/" + password);
        checkPassword(password);
        checkEmail(email);
        Query q = em.createQuery("select u from User u where u.email = :email and u.password = :password").setParameter("email", email).setParameter("password", password);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public Coach updateCoach(Coach c) throws SportException {
        if (c != null && c.getId() != null) {
            return em.merge(c);
        }
        return null;
    }

    @Override
    public Long getUserIdByEmai(String email) throws SportException {
        if (!userExists(email)) {
            throw new SportException("user with email = " + email + " does not exist");
        }
        Query q = em.createQuery("select u from User u where u.email = :email").setParameter("email", email);
        List<User> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0).getId();
    }
}
