package ru.sport.cardiomood.web.webservices;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import ru.sport.cardiomood.core.constants.ResponseConstants;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.managers.CoachManagerLocal;
import ru.sport.cardiomood.core.managers.UserManagerLocal;
import ru.sport.cardiomood.json.entity.JsonResponse;
import ru.sport.cardiomood.web.json.SecureCardioExceptionWrapper;
import ru.sport.cardiomood.web.json.SecureResponseWrapper;
import ru.sport.cardiomood.web.utils.SessionUtils;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("auth")
@Stateless
public class AuthResource {

    @Context
    private UriInfo context;
    @EJB
    UserManagerLocal userMan;
    @EJB
    CoachManagerLocal coachMan;
    public static final String INDEX = "index.xhtml";
    public static final String GUEST = "guest.xhtml";

    /**
     * Creates a new instance of AuthResource
     */
    public AuthResource() {
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("check_existence")
    public String checkEmailExistence(@FormParam("email") String email) {
        try {
            JsonResponse<String> jr = new JsonResponse<String>(userMan.userExists(email) ? ResponseConstants.YES : ResponseConstants.NO);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("check_data")
    public String checkUserAuthorisationData(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            Trainee t = null;
            if (userMan.checkLoginInfo(email, password)) {
                Trainee tr = userMan.getTraineeByEmail(email);
                t = new Trainee(email, null, tr.getFirstName(), tr.getLastName(), tr.getPhone());
                t.setId(tr.getId());
            }
            JsonResponse<Trainee> jr = new JsonResponse<Trainee>(t);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("register/coach")
    public String registerUser(@Context HttpServletRequest req, @QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            System.out.println("email = " + email + " password = " + password);
            userMan.registerCoach(email, password);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("register/trainee")
    public String registerTrainee(@Context HttpServletRequest req,
            @QueryParam("email") String email,
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("password") String password) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            coachMan.createTrainee(coachId, email, password, firstName, lastName);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("login")
    public String login(@Context HttpServletRequest req, @QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            JsonResponse<String> jr = null;
            if (userMan.checkLoginInfo(email, password)) {
                SessionUtils.setSessionAttribute(req.getSession(true), SessionUtils.USER_ID_SESSION_ATTR, userMan.getUserIdByEmai(email));
                jr = new JsonResponse<String>(INDEX);
            } else {
                throw new SportException("Incorrect pair email/pasword");
            }
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("logout")
    public String logout(@Context HttpServletRequest req) {
        SessionUtils.setSessionAttribute(req.getSession(false), SessionUtils.USER_ID_SESSION_ATTR, null);
        JsonResponse<String> jr = new JsonResponse<String>(GUEST);
        return SecureResponseWrapper.getJsonResponse(jr);
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("logged_in")
    public String isLoggedIn(@Context HttpServletRequest req) {
        String answer = "";
        if (SessionUtils.getUserId(req.getSession(false)) != null) {
            answer = "1";
        } else {
            answer = "0";
        }
        JsonResponse<String> jr = new JsonResponse<String>(answer);
        return SecureResponseWrapper.getJsonResponse(jr);
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("sustain")
    public String sustainSession(@Context HttpServletRequest req) {

        try {
            String answer = "1";
            JsonResponse<String> jr = new JsonResponse<String>(answer);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (Exception e) {
        }
        return null;
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("coach_trainees")
    public String getCoachTrainees(@Context HttpServletRequest req) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            List<Trainee> list = coachMan.getTrainees(coachId);
            JsonResponse<List<Trainee>> jr = new JsonResponse<List<Trainee>>(list);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }
}
