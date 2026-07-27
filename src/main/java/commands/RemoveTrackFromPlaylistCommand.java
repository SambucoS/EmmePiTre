package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

/**
 * Comando (pattern Command) che rappresenta la rimozione di una traccia da
 * una specifica playlist, senza intaccare la libreria principale ne' le
 * altre playlist.
 *
 * @version 1.0
 */
public class RemoveTrackFromPlaylistCommand implements Command {

    private final Playlist playlist;
    private final Track track;

    /**
     * Crea il comando per la rimozione della traccia dalla playlist indicata.
     *
     * @param playlist la {@link Playlist} da cui rimuovere la traccia
     * @param track    la {@link Track} da rimuovere
     */
    public RemoveTrackFromPlaylistCommand(Playlist playlist, Track track) {
        this.playlist = playlist;
        this.track = track;
    }

    /**
     * Rimuove la traccia dalla playlist tramite il manager.
     */
    @Override
    public void execute() {
        // Usa il manager così scatta in automatico il this.sync() e salva su JSON!
        PlaylistManager.getInstance().removeTrackFromPlaylist(track, playlist);
    }

    /**
     * Annulla la rimozione riaggiungendo la traccia alla playlist.
     */
    @Override
    public void undo() {
        // Per annullare, la riaggiungiamo (sempre tramite manager per il JSON)
        PlaylistManager.getInstance().addTrackToPlaylist(track, playlist);
    }
}
