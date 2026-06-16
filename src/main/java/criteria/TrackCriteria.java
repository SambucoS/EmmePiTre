package criteria;

import models.Track;

/**
 * Strategy pattern: contratto comune per i criteri di selezione delle tracce
 * usati nella creazione automatica delle playlist. Ogni criterio concreto
 * decide se una traccia soddisfa o meno la condizione.
 */
public interface TrackCriteria {

    boolean matches(Track track);
}
