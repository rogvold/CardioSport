package ru.sport.cardiomood.json.entity;

import java.util.List;
import ru.sport.cardiomood.core.jpa.GPSEntity;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class JsonInput {

    private Long timestamp;
    private List<Integer> rr;
    private List<Pair> hr;
    private List<GPSEntity> geo;
    private Long workoutId;
    private Long audioTrackId;
    private Long audioTrackPos;

    public Long getAudioTrackId() {
        return audioTrackId;
    }

    public void setAudioTrackId(Long audioTrackId) {
        this.audioTrackId = audioTrackId;
    }

    public Long getAudioTrackPos() {
        return audioTrackPos;
    }

    public void setAudioTrackPos(Long audioTrackPos) {
        this.audioTrackPos = audioTrackPos;
    }

    public List<GPSEntity> getGeo() {
        return geo;
    }

    public void setGeo(List<GPSEntity> geo) {
        this.geo = geo;
    }

    public List<Pair> getHr() {
        return hr;
    }

    public void setHr(List<Pair> hr) {
        this.hr = hr;
    }

    public List<Integer> getRr() {
        return rr;
    }

    public void setRr(List<Integer> rr) {
        this.rr = rr;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public String toString() {
        return "JsonInput{" + "timestamp=" + timestamp + ", rr=" + rr + ", hr=" + hr + ", geo=" + geo + ", workoutId=" + workoutId + ", audioTrackId=" + audioTrackId + ", audioTrackPos=" + audioTrackPos + '}';
    }
}
