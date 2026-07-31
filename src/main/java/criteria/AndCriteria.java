package criteria;

import java.util.ArrayList;
import java.util.List;

import models.Track;

/**
 * Composite pattern: criterio composto da piu' criteri, anch'esso un
 * {@link TrackCriteria}. Una traccia soddisfa l'AndCriteria solo se soddisfa
 * TUTTI i criteri figli (combinazione in AND). Trattando foglie e composito
 * tramite la stessa interfaccia, i criteri si possono annidare liberamente.
 *
 * @version 1.0
 */
public class AndCriteria implements TrackCriteria {

    private final List<TrackCriteria> children = new ArrayList<>();

    /**
     * Aggiunge un criterio figlio alla combinazione. I criteri {@code null}
     * vengono ignorati.
     *
     * @param criteria il {@link TrackCriteria} da aggiungere alla combinazione
     * @return questa stessa istanza, per permettere chiamate concatenate
     */
    public AndCriteria add(TrackCriteria criteria) {
        if (criteria != null) {
            children.add(criteria);
        }
        return this;
    }

    /**
     * Indica se non e' stato aggiunto alcun criterio figlio.
     *
     * @return {@code true} se la combinazione non contiene criteri
     */
    public boolean isEmpty() {
        return children.isEmpty();
    }

    /**
     * Verifica se la traccia soddisfa tutti i criteri figli.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se ogni criterio figlio restituisce {@code true} per la traccia
     */
    @Override
    public boolean matches(Track track) {
        return children.stream().allMatch(c -> c.matches(track));
    }
}
