package ru.sport.cardiomood.core.enums;

/**
 * Represents possible statuses of audio tracks.
 * @author danon
 */
public enum AudioTrackStatus {

    NEW("New"), 
    READY("Ready"), 
    DELETED("Deleted");
    
    private final String stringValue;
    
    private AudioTrackStatus(String s) {
        stringValue = s;
    }
    
    @Override
    public String toString() {
        return stringValue;
    }
    
}
