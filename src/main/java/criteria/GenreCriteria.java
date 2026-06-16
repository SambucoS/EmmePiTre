package criteria;

import models.Track;

/** Strategy concreta: seleziona le tracce di un certo genere. */
public class GenreCriteria implements TrackCriteria {

    private final String genre;

    public GenreCriteria(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean matches(Track track) {
        return track.getGenre() != null && track.getGenre().equalsIgnoreCase(genre);
    }
}
