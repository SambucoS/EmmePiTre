package criteria;

import models.Track;

/** Strategy concreta: seleziona le tracce con contenuto esplicito. */
public class ExplicitCriteria implements TrackCriteria {

    @Override
    public boolean matches(Track track) {
        return track.isExplicit();
    }
}
