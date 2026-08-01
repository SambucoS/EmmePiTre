package criteria;

import models.Track;

/**
 * Strategy pattern: contratto comune per i criteri di selezione delle tracce
 * usati nella creazione automatica delle playlist. Ogni criterio concreto
 * decide se una traccia soddisfa o meno la condizione.
 *
 * @version 1.0
 */
public interface TrackCriteria {

    /**
     * Verifica se la traccia soddisfa il criterio.
     *
     * @param track la {@link Track} da valutare
     * @return {@code true} se la traccia soddisfa il criterio, {@code false} altrimenti
     */
    boolean matches(Track track);
}
