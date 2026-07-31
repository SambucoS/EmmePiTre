package criteria;

import models.Track;

/**
 * Strategy concreta che seleziona le tracce con contenuto esplicito.
 *
 * @version 1.0
 */
public class ExplicitCriteria implements TrackCriteria {

    /**
     * Verifica se la traccia e' contrassegnata come contenuto esplicito.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se {@link Track#isExplicit()} restituisce {@code true}
     */
    @Override
    public boolean matches(Track track) {
        return track.isExplicit();
    }
}
