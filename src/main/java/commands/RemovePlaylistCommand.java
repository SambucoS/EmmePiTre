package commands;

import models.Playlist;
import models.PlaylistManager;

/**
 * Comando (pattern Command) che rappresenta l'eliminazione di una playlist
 * dalla collezione gestita da {@link PlaylistManager}.
 *
 * @version 1.0
 */
public class RemovePlaylistCommand implements Command {

    private final Playlist playlist;

    /**
     * Crea il comando per l'eliminazione della playlist indicata.
     *
     * @param playlist la {@link Playlist} da eliminare
     */
    public RemovePlaylistCommand(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Rimuove la playlist dalla collezione.
     */
    @Override
    public void execute() {
        PlaylistManager.getInstance().deletePlaylist(playlist);
    }

    /**
     * Annulla l'eliminazione riaggiungendo la playlist alla collezione.
     */
    @Override
    public void undo() {
        PlaylistManager.getInstance().addPlaylist(playlist);
    }
}
