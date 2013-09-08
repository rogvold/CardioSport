package ru.sport.cardiomood.web.webservices;

import com.google.gson.Gson;
import java.util.ArrayList;
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
import ru.sport.cardiomood.core.jpa.Trainee;
import ru.sport.cardiomood.core.jpa.Workout;
import ru.sport.cardiomood.core.managers.*;
import ru.sport.cardiomood.json.entity.*;
import ru.sport.cardiomood.utils.CardioUtils;
import ru.sport.cardiomood.utils.UserUtils;
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
    @EJB
    UserManagerLocal userMan;
    @EJB
    InputDataManagerLocal inMan;
    @EJB
    CardioSessionManagerLocal cardMan;

    private class JWorkout {

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
    @Produces("application/json;charset=utf-8")
    @Path("create_workout")
    public String createActivity(@Context HttpServletRequest req, @FormParam("json") String json) {
        try {
            Gson gson = new Gson();
            JWorkout j = gson.fromJson(json, JWorkout.class);
//            System.out.println("from request: name = " + req.getParameter("name"));
            System.out.println("create_workout: name = " + j.getName() + "; description=" + j.getDescription() + "; activities = " + j.getActivities());
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            if (j.getActivities() == null || j.getName() == null) {
                throw new SportException("activities are null");
            }
            Workout w = workMan.createWorkout(coachId, j.getName(), j.getDescription());
            actMan.addActivitiesToWorkout(w.getId(), j.getActivities());
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("appoint_workout")
    public String createActivity(@Context HttpServletRequest req, @FormParam("traineeId") Long traineeId, @FormParam("workoutId") Long workoutId) {
        try {
            CardioUtils.checkNull(workoutId);
            CardioUtils.checkNull(traineeId);
            workMan.appointWorkout(traineeId, workoutId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.YES);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("activity_sessions")
    public String getActivityCardioSession(@Context HttpServletRequest req, @FormParam("workoutId") Long workoutId) {
        try {
            CardioUtils.checkNull(workoutId);
            List<JsonSession> sessions = cardMan.getWorkoutActivitiesSessions(workoutId);
            JsonResponse<List<JsonSession>> jr = new JsonResponse<List<JsonSession>>(sessions);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("get_all")
    public String getAllWorkouts(@Context HttpServletRequest req) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            List<JsonWorkout> ws = workMan.getCoachJsonWorkouts(coachId);
            JsonResponse<List<JsonWorkout>> jr = new JsonResponse<List<JsonWorkout>>(ws);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("info")
    public String getWorkoutById(@Context HttpServletRequest req, @FormParam("id") Long workoutId) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            JsonWorkout jw = workMan.getJsonWorkout(workoutId);
            JsonResponse<JsonWorkout> jr = new JsonResponse<JsonWorkout>(jw);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("activities_list")
    public String getActivitiesList(@Context HttpServletRequest req, @FormParam("id") Long workoutId) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            JsonWorkout jw = workMan.getJsonWorkout(workoutId);
            List<Long> list = new ArrayList();
            for (Activity a : jw.getActivities()) {
                list.add(a.getId());
            }
            JsonResponse<List<Long>> jr = new JsonResponse<List<Long>>(list);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("trainees_state")
    public String getAllTraineesState(@Context HttpServletRequest req) {
        try {
            Long coachId = SessionUtils.getUserId(req.getSession(false));
            List<JsonUserState> list = workMan.getAllUsersState(coachId);
            JsonResponse< List<JsonUserState>> jr = new JsonResponse< List<JsonUserState>>(list);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    //
    //works
    @POST
    @Produces("application/json;charset=utf-8")
    @Path("get_current")
    public String getCurrentTraineeWorkout(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            JsonResponse<JsonWorkout> jr = null;
            if (t.getCurrentWorkoutId() == null) {
                jr = new JsonResponse<JsonWorkout>(null);
                return SecureResponseWrapper.getJsonResponse(jr);
            }
            Workout clonedWorkout = workMan.getChildCurrentWorkout(t.getCurrentWorkoutId(), t.getId());
            if (clonedWorkout == null) {
                throw new SportException("you have not got cloned workout");
            }

            JsonWorkout jw = workMan.getJsonWorkout(t.getCurrentWorkoutId());
            jw.setStartDate(clonedWorkout.getStartDate().getTime());
            jr = new JsonResponse<JsonWorkout>(jw);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("send_data")
    public String sendData(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId, @FormParam("data") String data) {
        try {
            System.out.println("sendData: workoutId = " + workoutId);
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            JsonInput input = (new Gson()).fromJson(data, JsonInput.class);

            System.out.println("traineeId = " + t.getId());

//            Workout realW = workMan.getChildCurrentWorkout(workoutId, t.getId());
//            System.out.println("realW = " + realW);

            System.out.println("no, wId = " + workMan.getChildCurrentWorkout(workoutId, t.getId()).getId());
            input.setWorkoutId(workoutId);
            inMan.processInputData(input, t.getId());
            JsonResponse jr = new JsonResponse(null);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("start_workout")
    public String startWorkout(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            Workout realWorkout = workMan.startWorkout(workoutId, t.getId());
            JsonResponse jr = new JsonResponse<Long>(realWorkout.getId());
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("start_activity")
    public String startActivity(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId, @FormParam("activityId") Long activityId) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            Activity rAct = workMan.startActivity(workoutId, activityId, t.getId());
            JsonResponse jr = new JsonResponse<Long>(rAct.getId());
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("stop_activity")
    public String stopActivity(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId, @FormParam("activityId") Long activityId, @FormParam("duration") Long duration) {
        try {
            System.out.println("stop_activity: duration = " + duration);
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            workMan.stopActivity(workoutId, t.getId(), activityId, duration);
            JsonResponse jr = new JsonResponse<Long>(null);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("switch_activity")
    public String switchActivity(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId, @FormParam("firstActivityId") Long firstActivityId, @FormParam("secondActivityId") Long secondActivityId) {
        try {
            throw new SportException("");
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("stop_workout")
    public String stopWorkout(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            workMan.stopWorkout(workoutId, t.getId());
            JsonResponse jr = new JsonResponse<Long>(null);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("pause_activity")
    public String pause(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId, @FormParam("activityId") Long activityId, @FormParam("duration") Long duration) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            workMan.pauseActivity(workoutId, activityId, t.getId(), duration);
            JsonResponse jr = new JsonResponse<Long>(null);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("resume_activity")
    public String unpause(@FormParam("email") String email, @FormParam("password") String password, @FormParam("workoutId") Long workoutId, @FormParam("duration") Long duration) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);
            workMan.unpauseActivity(workoutId, t.getId(), duration);
            JsonResponse jr = new JsonResponse<Long>(null);
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("get_metronome_rate")
    public String getMetronomeRate(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            Trainee t = UserUtils.getTraineeByEmailAndPassword(userMan, email, password);

            if (t.getMetronomeRate() == null) {
                t.setMetronomeRate(60.0);
                userMan.updateMetronomeRate(60.0, t.getId());
            }

            JsonResponse jr = new JsonResponse<Double>(t.getMetronomeRate());
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json;charset=utf-8")
    @Path("get_trainee_metronome_rate")
    public String getTraineeMetronomeRate(@Context HttpServletRequest req, @FormParam("traineeId") Long traineeId) {
        try {
            CardioUtils.checkNull(traineeId);
            Trainee t = userMan.getTraineeById(traineeId);
            //todo: check rights
            JsonResponse<Double> jr = new JsonResponse<Double>(t.getMetronomeRate());
            return SecureResponseWrapper.getJsonResponse(jr);
        } catch (SportException e) {
            return SecureCardioExceptionWrapper.wrapException(e);
        }
    }
}
