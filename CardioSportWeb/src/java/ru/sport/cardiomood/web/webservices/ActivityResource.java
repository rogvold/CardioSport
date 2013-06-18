package ru.sport.cardiomood.web.webservices;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import ru.sport.cardiomood.core.constants.ResponseConstants;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Activity;
import ru.sport.cardiomood.core.managers.ActivityManagerLocal;
import ru.sport.cardiomood.json.entity.JsonResponse;
import ru.sport.cardiomood.utils.ActivityUtils;
import ru.sport.cardiomood.web.json.SecureCardioExceptionWrapper;
import ru.sport.cardiomood.web.json.SecureResponseWrapper;
import ru.sport.cardiomood.web.utils.SessionUtils;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("activity")
@Stateless
public class ActivityResource {

    @Context
    private UriInfo context;
    @EJB
    ActivityManagerLocal actMan;

    /**
     * Creates a new instance of ActivityResource
     */
    public ActivityResource() {
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("create_activity")
    public String createActivity(@Context HttpServletRequest req, @QueryParam("minHeartRate") Integer minHeartRate,
            @QueryParam("maxHeartRate") Integer maxHeartRate,
            @QueryParam("minTension") Double minTension,
            @QueryParam("maxTension") Double maxTension,
            @QueryParam("duration") Long duration,
            @QueryParam("name") String name,
            @QueryParam("description") String description,
            @QueryParam("minSpeed") Double minSpeed,
            @QueryParam("maxSpeed") Double maxSpeed) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            actMan.createActivity(coachId, minHeartRate, maxHeartRate, minTension, maxTension, duration, name, description, null, minSpeed, maxSpeed);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("activities_list")
    public String initAllActivities(@Context HttpServletRequest req) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            List<Activity> list = ActivityUtils.cloneActivitiesList(actMan.getCoachActivities(coachId));
            list = ActivityUtils.prepareActivities(list);
            JsonResponse<List<Activity>> jr = new JsonResponse<List<Activity>>(ResponseConstants.OK, null, list);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("all_activities_list")
    public String getAllCoachActivities(@Context HttpServletRequest req) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            List<Activity> list = ActivityUtils.cloneActivitiesList(actMan.getAllCoachActivities(coachId));
            list = ActivityUtils.prepareActivities(list);
            JsonResponse<List<Activity>> jr = new JsonResponse<List<Activity>>(ResponseConstants.OK, null, list);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }
}
