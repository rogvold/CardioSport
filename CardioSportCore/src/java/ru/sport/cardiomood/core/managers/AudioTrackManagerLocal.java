package ru.sport.cardiomood.core.managers;

import java.util.List;
import javax.ejb.Local;
import ru.sport.cardiomood.core.enums.AudioTrackStatus;
import ru.sport.cardiomood.core.jpa.AudioTrack;

/**
 * A local interface for audio AudioTrackManager bean. 
 * @author danon
 */
@Local
public interface AudioTrackManagerLocal {
 
    AudioTrack addTrack(long userId, String filePath, String name, String description, String originalFileName, String contentType);
    
    List<AudioTrack> getTracks(long userId);
    
    List<AudioTrack> getTracks(long userId, AudioTrackStatus... statuses);
    
    AudioTrack findTrackById(long userId, long id);
    
    List<AudioTrack> findTrackByName(long userId, String name); 
    
    boolean deleteTrack(long userId, long id);
    
    AudioTrack updateTrack(long userId, AudioTrack track);
    
    void processTrack(AudioTrack track);
}
