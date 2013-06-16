package ru.sport.cardiomood.web.webservices;

import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import ru.sport.cardiomood.core.constants.ResponseConstants;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.core.managers.ActivityManagerLocal;
import ru.sport.cardiomood.core.managers.CoachManagerLocal;
import ru.sport.cardiomood.core.managers.WorkoutManagerLocal;
import ru.sport.cardiomood.json.entity.JsonResponse;
import ru.sport.cardiomood.web.json.SecureCardioExceptionWrapper;
import ru.sport.cardiomood.web.json.SecureResponseWrapper;
import ru.sport.cardiomood.web.utils.SessionUtils;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("workout")
@Stateless
public class WorkoutResource {

    @Context
    private UriInfo context;
    @EJB
    ActivityManagerLocal actMan;
    @EJB
    WorkoutManagerLocal workMan;
    @EJB
    CoachManagerLocal coachMan;

    private class JsonWorkout {

        private List<Long> activities;
        private String name;
        private String description;

        public List<Long> getActivities() {
            return activities;
        }

        public void setActivities(List<Long> activities) {
            this.activities = activities;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Creates a new instance of WorkoutResource
     */
    public WorkoutResource() {
    }

    @POST
    @Produces("application/json")
    @Path("create_workout")
    public String createActivity(@Context HttpServletRequest req, @FormParam("json") String json) {
        try {
            Gson gson = new Gson();
            JsonWorkout j = gson.fromJson(json, JsonWorkout.class);
//            System.out.println("from request: name = " + req.getParameter("name"));
            System.out.println("create_workout: name = " + j.getName() + "; description=" + j.getDescription() + "; activities = " + j.getActivities());
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            if (j.getActivities() == null || j.getName() == null) {
                throw new SportException("activities are null");
            }
            Workout w = workMan.createWorkout(coachId, j.getName(), j.getDescription());
            actMan.addActivitiesToWorkout(w.getId(), j.getActivities());
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, null, ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }
}
