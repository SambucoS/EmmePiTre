package criteria;

import models.Track;

/** Strategy concreta: seleziona le tracce contrassegnate come "New Release". */
public class NewReleaseCriteria implements TrackCriteria {

    @Override
    public boolean matches(Track track) {
        return track.isNewRelease();
    }
}
