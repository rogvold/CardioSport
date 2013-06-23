package ru.sport.cardiomood.core.managers;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;
import ru.sport.cardiomood.core.enums.AudioTrackStatus;
import ru.sport.cardiomood.core.jpa.AudioTrack;
import ru.sport.cardiomood.utils.AudioTrackUtuls;
import ru.sport.cardiomood.utils.StringUtils;

/**
 * Implementation of stateless bean for manipulating audio tracks.
 * @author danon
 */
@Stateless
public class AudioTrackManager implements AudioTrackManagerLocal {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public AudioTrack addTrack(long userId, String filePath, String name, String description, String sourceFileName, String contentType) {
        AudioTrack track = new AudioTrack();
        track.setUserId(userId);
        track.setFilePath(filePath);
        track.setOriginalFileName(sourceFileName);
        track.setName(name);
        track.setDescription(description);
        track.setContentType(contentType);
        track.setStatus(AudioTrackStatus.NEW);
        track.setCreationDate(new Date());
        track.setLastModified(track.getCreationDate());
        em.persist(track);
        processTrack(track);
        return track;
    }
    
    @Override @Asynchronous
    public void processTrack(AudioTrack track) {
        if (track == null || track.getStatus() != AudioTrackStatus.NEW) {
            return;
        }
        try {
            File trackFile = new File(track.getFilePath());
            track.setFileSize(FileUtils.sizeOf(trackFile));
            track.setMd5(StringUtils.toHexString(AudioTrackUtuls.getMD5(trackFile)));
            track.setBpm(AudioTrackUtuls.calculateBPM(trackFile, track.getContentType()));
            track.setStatus(AudioTrackStatus.READY);
            track.setLastModified(new Date());
            em.merge(track);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    @Override
    public List<AudioTrack> getTracks(long userId) {
        return getTracks(userId, AudioTrackStatus.NEW, AudioTrackStatus.READY);
    }

    @Override
    public List<AudioTrack> getTracks(long userId, AudioTrackStatus... statuses) {
        Query q = em.createQuery("select t from AudioTrack t where t.userId = :userId and t.status in :statuses", AudioTrack.class);
        q.setParameter("userId", userId);
        q.setParameter("statuses", Arrays.asList(statuses));
        return q.getResultList();
    }

    @Override
    public AudioTrack findTrackById(long userId, long id) {
        AudioTrack track = em.find(AudioTrack.class, id);
        if (track == null) {
            return null;
        }
        if (new Long(userId).equals(track.getUserId()))
            return track;
        return null;
    }

    @Override
    public List<AudioTrack> findTrackByName(long userId, String name) {
        Query q = em.createQuery("select t from AudioTrack t where t.userId = :userId and t.status <> 'DELETED' and t.name like :name", AudioTrack.class);
        q.setParameter("name", "%" + name + "%");
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    @Override
    public boolean deleteTrack(long userId, long id) {
        AudioTrack track = findTrackById(userId, id);
        if (track == null) {
            return false;
        }
        
        track.setStatus(AudioTrackStatus.DELETED);
        track.setLastModified(new Date());
        em.merge(track);
        return true;
    }

    @Override
    public AudioTrack updateTrack(long userId, AudioTrack track) {
        if (track.getId() == null || track.getUserId() == null)
            throw new IllegalArgumentException("Incorrect audio track.");
        if (track.getUserId() == userId) {
            track.setLastModified(new Date());
            return em.merge(track);
        }
        throw new IllegalArgumentException("Operation is not authorised");
    }

}
