package observer;

/**
 * Rappresenta il contratto del pattern Observer per le viste che devono
 * reagire ai cambiamenti della libreria musicale. Qualsiasi classe che si
 * registra presso {@code Library} come osservatore deve implementare questa
 * interfaccia per essere notificata quando i dati cambiano.
 *
 * @version 1.0
 */
public interface LibraryObserver {

    /**
     * Notificato a tutte le viste registrate quando la libreria cambia
     * (aggiunta, rimozione o modifica di una traccia).
     */
    void onLibraryChanged();
}
