package ru.sport.cardiomood.web.webservices;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
    @Produces("application/json")
    @Path("check_existence")
    public String checkEmailExistence(@FormParam("email") String email) {
        try {
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, userMan.userExists(email) ? ResponseConstants.YES : ResponseConstants.NO);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("check_data")
    public String checkUserAuthorisationData(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            Trainee t = null;
            if (userMan.checkLoginInfo(email, password)) {
                t = userMan.getTraineeByEmail(email);
                t.setEmail(null);
                t.setPassword(null);
            }
            JsonResponse<Trainee> jr = new JsonResponse<Trainee>(ResponseConstants.OK, null, t);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("register/coach")
    public String registerUser(@Context HttpServletRequest req, @QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            System.out.println("email = " + email + " password = " + password);
            userMan.registerCoach(email, password);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("register/trainee")
    public String registerTrainee(@Context HttpServletRequest req,
            @QueryParam("email") String email,
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("password") String password) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            coachMan.createTrainee(coachId, email, password, firstName, lastName);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("login")
    public String login(@Context HttpServletRequest req, @QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            JsonResponse<String> jr = null;
            if (userMan.checkLoginInfo(email, password)) {
                SessionUtils.setSessionAttribute(req.getSession(true), SessionUtils.USER_ID_SESSION_ATTR, userMan.getUserIdByEmai(email));
                jr = new JsonResponse<String>(ResponseConstants.OK, null, INDEX);
            } else {
                throw new SportException("Incorrect pair email/pasword");
            }
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("logout")
    public String logout(@Context HttpServletRequest req) {
        SessionUtils.setSessionAttribute(req.getSession(false), SessionUtils.USER_ID_SESSION_ATTR, null);
        JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, GUEST);
        return SecureResponseWrapper.getJsonResponse(jr);
    }

    @POST
    @Produces("application/json")
    @Path("logged_in")
    public String isLoggedIn(@Context HttpServletRequest req) {
        String answer = "";
        if (SessionUtils.getUserId(req.getSession(false)) != null) {
            answer = "1";
        } else {
            answer = "0";
        }
        JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, answer);
        return SecureResponseWrapper.getJsonResponse(jr);
    }

    @POST
    @Produces("application/json")
    @Path("sustain")
    public String sustainSession(@Context HttpServletRequest req) {

        try {
            String answer = "1";
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, answer);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (Exception e) {
        }
        return null;
    }
}
