package observer;

import models.Playlist;

/**
 * Rappresenta il contratto del pattern Observer per i componenti che devono
 * reagire ai cambiamenti del contenuto di una playlist (aggiunta, rimozione
 * o riordino di tracce). Qualsiasi classe che si registra presso una
 * {@code Playlist} come osservatore deve implementare questa interfaccia
 * per essere notificata quando la playlist cambia.
 *
 * @version 1.0
 */
public interface PlaylistObserver {

    /**
     * Notificato agli osservatori registrati quando la playlist osservata
     * cambia (aggiunta, rimozione o riordino di una traccia).
     *
     * @param playlist la playlist che ha subito la modifica
     */
    void onPlaylistChanged(Playlist playlist);
}
