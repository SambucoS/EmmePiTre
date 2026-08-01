package criteria;

import models.Track;

/**
 * Strategy concreta che seleziona le tracce pubblicate in un determinato anno.
 *
 * @version 1.0
 */
public class YearCriteria implements TrackCriteria {

    private final int year;

    /**
     * Crea il criterio per l'anno indicato.
     *
     * @param year l'anno da confrontare con quello della traccia
     */
    public YearCriteria(int year) {
        this.year = year;
    }

    /**
     * Verifica se l'anno della traccia coincide con quello del criterio.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se l'anno della traccia e' uguale a quello richiesto
     */
    @Override
    public boolean matches(Track track) {
        return track.getYear() == year;
    }
}
