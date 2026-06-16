package criteria;

import models.Track;

/** Strategy concreta: seleziona le tracce di un certo anno. */
public class YearCriteria implements TrackCriteria {

    private final int year;

    public YearCriteria(int year) {
        this.year = year;
    }

    @Override
    public boolean matches(Track track) {
        return track.getYear() == year;
    }
}
