package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

/**
 * Comando (pattern Command) che rappresenta l'aggiunta di una traccia a una
 * specifica playlist tramite il {@link PlaylistManager}, cosi' che la
 * modifica venga anche persistita su JSON.
 *
 * @version 1.0
 */
public class AddTrackToPlaylistCommand implements Command {

    private final Playlist playlist;
    private final Track track;

    /**
     * Crea il comando per l'aggiunta della traccia alla playlist indicata.
     *
     * @param playlist la {@link Playlist} di destinazione
     * @param track    la {@link Track} da aggiungere
     */
    public AddTrackToPlaylistCommand(Playlist playlist, Track track) {
        this.playlist = playlist;
        this.track = track;
    }

    /**
     * Aggiunge la traccia alla playlist tramite il manager.
     */
    @Override
    public void execute() {
        // Esegue l'aggiunta e salva su JSON in automatico tramite il manager
        PlaylistManager.getInstance().addTrackToPlaylist(track, playlist);
    }

    /**
     * Annulla l'aggiunta rimuovendo la traccia dalla playlist.
     */
    @Override
    public void undo() {
        // Per annullare un'aggiunta, la rimuoviamo (sempre tramite manager)
        PlaylistManager.getInstance().removeTrackFromPlaylist(track, playlist);
    }
}
