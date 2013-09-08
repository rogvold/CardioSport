package ru.sport.cardiomood.utils;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.Track;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for audio track files.
 *
 * @author danon
 */
public class AudioTrackUtuls {
    
    private static final String ECHO_NEST_API_KEY = "66DUKOMQKYUBIP5NE";

    public static byte[] getMD5(File f) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = null;
        DigestInputStream dis = null;
        try {
            is = new FileInputStream(f);
            dis = new DigestInputStream(is, md);
            byte[] buf = new byte[10 * 1024];
            while (is.read(buf) >= 0);
            byte[] digest = md.digest();
            return digest;
        } finally {
            if (dis != null) {
                dis.close();
            }
        }
    }

    public static Integer calculateBPM(File trackFile, String contentType) {
        try {
            EchoNestAPI trackAPI = new EchoNestAPI(ECHO_NEST_API_KEY);
            Track track = trackAPI.uploadTrack(trackFile, false);
            if (track.getStatus() != Track.AnalysisStatus.ERROR && track.getStatus() != Track.AnalysisStatus.COMPLETE) 
                do {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                } while (track.getStatus() != Track.AnalysisStatus.ERROR && track.getStatus() != Track.AnalysisStatus.COMPLETE);
            Double tempo = track.getTempo();
            return tempo == null ? null : tempo.intValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
