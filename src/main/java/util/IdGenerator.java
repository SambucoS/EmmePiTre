package util;

import java.util.UUID;

/**
 * Utility per la generazione di identificativi univoci, usati ad esempio
 * per assegnare un id a ogni nuova {@link models.Track}.
 *
 * @version 1.0
 */
public class IdGenerator {

    /**
     * Genera un nuovo identificativo univoco basato su UUID.
     *
     * @return una stringa contenente un UUID casuale
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
