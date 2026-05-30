package interfaces;
import models.Track;


public interface TrackList {
    void addTrack(Track t);
    void removeTrack(Track t);
    List<Track> getTracks();
    int getSize();
}
