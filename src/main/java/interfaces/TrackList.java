package interfaces;
import models.Track;

import java.util.List;


public interface TrackList {
    void addTrack(Track t);
    void removeTrack(Track t);
    List<Track> getTracks();
    int getSize();
}
