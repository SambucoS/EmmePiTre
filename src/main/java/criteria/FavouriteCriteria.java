package criteria;

import models.Track;

/** Strategy concreta: seleziona le tracce preferite. */
public class FavouriteCriteria implements TrackCriteria {

    @Override
    public boolean matches(Track track) {
        return track.isFavourite();
    }
}
