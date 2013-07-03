package ru.sport.cardiomood.web.webservices;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import ru.sport.cardiomood.core.exceptions.SportException;
import ru.sport.cardiomood.core.jpa.GPSEntity;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.core.managers.GPSManagerLocal;
import ru.sport.cardiomood.core.managers.WorkoutManagerLocal;
import ru.sport.cardiomood.json.entity.JsonResponse;
import ru.sport.cardiomood.json.entity.JsonUserState;
import ru.sport.cardiomood.web.json.SecureCardioExceptionWrapper;
import ru.sport.cardiomood.web.json.SecureResponseWrapper;
import ru.sport.cardiomood.web.utils.SessionUtils;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("gps")
@Stateless
public class GPSResource {

    @Context
    private UriInfo context;
    @EJB
    GPSManagerLocal gpsMan;
    @EJB
    WorkoutManagerLocal workMan;

    /**
     * Creates a new instance of GPSResource
     */
    public GPSResource() {
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("gps")
    public String getGPS(@Context HttpServletRequest req, @FormParam("workoutId") Long workoutId) {
        try {
            Workout w = workMan.getWorkoutById(workoutId);
            List<GPSEntity> gps = gpsMan.getWorkoutGPS(w.getId());
            JsonResponse< List<GPSEntity>> jr = new JsonResponse< List<GPSEntity>>(gps);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }
}
