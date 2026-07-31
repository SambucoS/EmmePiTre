package criteria;

import models.Track;

/**
 * Strategy concreta che seleziona le tracce contrassegnate come "New Release".
 *
 * @version 1.0
 */
public class NewReleaseCriteria implements TrackCriteria {

    /**
     * Verifica se la traccia e' contrassegnata come nuova uscita.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se {@link Track#isNewRelease()} restituisce {@code true}
     */
    @Override
    public boolean matches(Track track) {
        return track.isNewRelease();
    }
}
