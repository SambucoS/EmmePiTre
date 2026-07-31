package criteria;

import models.Track;

/**
 * Strategy concreta che seleziona le tracce contrassegnate come preferite.
 *
 * @version 1.0
 */
public class FavouriteCriteria implements TrackCriteria {

    /**
     * Verifica se la traccia e' stata contrassegnata come preferita.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se {@link Track#isFavourite()} restituisce {@code true}
     */
    @Override
    public boolean matches(Track track) {
        return track.isFavourite();
    }
}
