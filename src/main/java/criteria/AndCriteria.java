package criteria;

import java.util.ArrayList;
import java.util.List;

import models.Track;

/**
 * Composite pattern: criterio composto da piu' criteri, anch'esso un
 * {@link TrackCriteria}. Una traccia soddisfa l'AndCriteria solo se soddisfa
 * TUTTI i criteri figli (combinazione in AND). Trattando foglie e composito
 * tramite la stessa interfaccia, i criteri si possono annidare liberamente.
 */
public class AndCriteria implements TrackCriteria {

    private final List<TrackCriteria> children = new ArrayList<>();

    public AndCriteria add(TrackCriteria criteria) {
        if (criteria != null) {
            children.add(criteria);
        }
        return this;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public boolean matches(Track track) {
        return children.stream().allMatch(c -> c.matches(track));
    }
}
