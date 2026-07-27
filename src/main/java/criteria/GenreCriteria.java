package criteria;

import models.Track;

/**
 * Strategy concreta che seleziona le tracce appartenenti a un certo genere
 * musicale, confrontato ignorando maiuscole/minuscole.
 *
 * @version 1.0
 */
public class GenreCriteria implements TrackCriteria {

    private final String genre;

    /**
     * Crea il criterio per il genere indicato.
     *
     * @param genre il genere musicale da confrontare con quello della traccia
     */
    public GenreCriteria(String genre) {
        this.genre = genre;
    }

    /**
     * Verifica se il genere della traccia corrisponde a quello del criterio.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se la traccia ha un genere non nullo uguale (ignorando il case) a quello richiesto
     */
    @Override
    public boolean matches(Track track) {
        return track.getGenre() != null && track.getGenre().equalsIgnoreCase(genre);
    }
}
