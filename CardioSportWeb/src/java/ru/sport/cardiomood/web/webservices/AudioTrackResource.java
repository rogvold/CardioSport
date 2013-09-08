package ru.sport.cardiomood.web.webservices;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.sport.cardiomood.core.constants.ResponseConstants;
import ru.sport.cardiomood.core.jpa.AudioTrack;
import ru.sport.cardiomood.core.jpa.User;
import ru.sport.cardiomood.core.managers.AudioTrackManagerLocal;
import ru.sport.cardiomood.core.managers.UserManagerLocal;
import ru.sport.cardiomood.json.entity.JsonError;
import ru.sport.cardiomood.json.entity.JsonResponse;
import ru.sport.cardiomood.web.json.SecureResponseWrapper;
import ru.sport.cardiomood.web.utils.SessionUtils;

/**
 * File upload web-service implementation.
 * @author danon
 */
@Path("audiotrack")
@RequestScoped
public class AudioTrackResource {
    
    private static final String UPLOAD_DIRECTORY = "C:/uploads";
    private static final int BUFFER_SIZE = 10*1024; // 10KB
    
    @Inject
    private AudioTrackManagerLocal atm;
    
    @Inject
    private UserManagerLocal um;
    
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response  uploadTrack(
                @Context HttpServletRequest request,
                @FormDataParam("file") InputStream uploadedInputStream,
                @FormDataParam("file") FormDataContentDisposition fileDetail,
                @FormDataParam("file") FormDataBodyPart body,
                @QueryParam("name") String name,
                @QueryParam("description") String description
            ) {
        JsonError error = null;
        try {
            Long userId = SessionUtils.getUserId(SessionUtils.getSession(request, true));
            if (userId == null) {
                error = new JsonError("User is not logged in.", ResponseConstants.USER_IS_NOT_LOGGED_IN);
                throw new RuntimeException("Unauthorised operation.");
            }
            
            if (!fileDetail.getFileName().endsWith(".mp3") && !fileDetail.getFileName().endsWith(".wav")) {
                error = new JsonError("We only support .mp3 and .wav files.", ResponseConstants.USER_IS_NOT_LOGGED_IN);
                throw new RuntimeException("Unsupported format of the file: " + fileDetail.getFileName());
            }
            
            String uploadedFileName = saveUploadedFile(uploadedInputStream, fileDetail.getFileName());
            if (uploadedFileName == null) {
                throw new RuntimeException("Couldn't upload file.");
            }
            
            AudioTrack track = atm.addTrack(userId, uploadedFileName, name, description, fileDetail.getFileName(), body.getMediaType().toString());
            if (track == null) {
                throw new RuntimeException("Failed to add track to user's library.");
            }
            
            return Response.status(Response.Status.OK)
                    .entity(SecureResponseWrapper.getJsonResponse(new JsonResponse(track)))
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            
            if (error == null) {
                error = new JsonError(ex.getMessage(), ResponseConstants.NORMAL_ERROR_CODE);
            }
            return Response.status(Response.Status.OK)
                    .entity(SecureResponseWrapper.getJsonResponse(new JsonResponse(ResponseConstants.OK, error, null)))
                    .build();
        }
    }
    
    @GET
    @Path("download")
    public Response downloadTrack(@Context HttpServletRequest request, @QueryParam("id") long id) {
        try {
            Long userId = SessionUtils.getUserId(SessionUtils.getSession(request, true));
            if (userId == null) {
                throw new RuntimeException("Unauthorised operation.");
            }
            
            User u = um.getUserById(userId);
            if (u == null) {
                throw new RuntimeException("No such userId = " + userId);
            }
            
            return downloadTrack(u.getEmail(), u.getPassword(), id);
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(null)
                    .build();
        }
    }
    
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getTrackList(@Context HttpServletRequest request) {
        JsonError error = null;
        try {
            Long userId = SessionUtils.getUserId(SessionUtils.getSession(request, true));
            if (userId == null) {
                error = new JsonError("User is not logged in.", ResponseConstants.USER_IS_NOT_LOGGED_IN);
                throw new RuntimeException("Unauthorised operation.");
            }
            
            User u = um.getUserById(userId);
            if (u == null) {
                throw new RuntimeException("No such userId = " + userId);
            }
            
            return getTrackList(u.getEmail(), u.getPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            if (error == null) {
                error = new JsonError(ex.getMessage(), ResponseConstants.NORMAL_ERROR_CODE);
            }
            return Response.status(Response.Status.OK)
                    .entity(SecureResponseWrapper.getJsonResponse(new JsonResponse(ResponseConstants.OK, error, null)))
                    .build();
        }
    }
    
    @GET
    @Path("client_list")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getTrackList(@FormParam("email") String email, @FormParam("password") String password) {
        JsonError error = null; 
        try {
            if (!um.checkLoginInfo(email, password)) {
                error = new JsonError("User is not logged in.", ResponseConstants.USER_IS_NOT_LOGGED_IN);
                throw new RuntimeException("Unauthorised operation.");
            }
            List<AudioTrack> tracks = atm.getTracks(um.getUserIdByEmai(email));
            return Response.ok(SecureResponseWrapper.getJsonResponse(new JsonResponse<List<AudioTrack>>(tracks)))
                    .build();
        } catch (Exception ex) {
            if (error == null) {
                error = new JsonError(ex.getMessage(), ResponseConstants.NORMAL_ERROR_CODE);
            }
            return Response.status(Response.Status.OK)
                    .entity(SecureResponseWrapper.getJsonResponse(new JsonResponse(ResponseConstants.OK, error, null)))
                    .build();
        }
    }
    
    @POST
    @Path("client_download")
    public Response downloadTrack(@FormParam("email") String email, @FormParam("password") String password, @FormParam("id") long id) {
        try {
            if (!um.checkLoginInfo(email, password)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            AudioTrack track = atm.findTrackById(um.getUserIdByEmai(email), id);
            if (track == null)
                return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(new File(track.getFilePath()), track.getContentType())
                    .header("Content-Disposition", "attachment; filename=\"" + track.getOriginalFileName() + "\"")
                    .lastModified(track.getLastModified())
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("delete")
    public Response deleteTrack(@Context HttpServletRequest request, @QueryParam("id") long id) {
        JsonError error = null;
        try {
            Long userId = SessionUtils.getUserId(SessionUtils.getSession(request, true));
            if (userId == null) {
                error = new JsonError("User is not logged in.", ResponseConstants.USER_IS_NOT_LOGGED_IN);
                throw new RuntimeException("Unauthorised operation.");
            }
            boolean result = atm.deleteTrack(userId, id);
            return Response.status(Response.Status.OK)
                    .entity(SecureResponseWrapper.getJsonResponse(new JsonResponse<String>(result ? ResponseConstants.YES : ResponseConstants.NO)))
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            
            if (error == null) {
                error = new JsonError(ex.getMessage(), ResponseConstants.NORMAL_ERROR_CODE);
            }
            return Response.status(Response.Status.OK)
                    .entity(SecureResponseWrapper.getJsonResponse(new JsonResponse(ResponseConstants.ERROR, error, null)))
                    .build();
        }
    }
    
    
    /**
     * Saves input stream to upload directory.
     * @param is input stream of uploaded file.
     * @return full name of target file
     */
    private String saveUploadedFile(InputStream is, String originalName) {
        try {
            File uploadDir = new File(UPLOAD_DIRECTORY);
            uploadDir.mkdirs();
            File targetFile = File.createTempFile("upload", originalName.substring(originalName.lastIndexOf('.')), uploadDir);
            OutputStream os = new FileOutputStream(targetFile);
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = 0;
                while ((read = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, read);
                }
                return targetFile.getAbsolutePath();
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        
    }
}
