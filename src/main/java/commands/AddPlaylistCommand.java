package commands;

import models.Playlist;
import models.PlaylistManager;

/**
 * Comando (pattern Command) che rappresenta l'aggiunta di una playlist alla
 * collezione gestita da {@link PlaylistManager}. Usato principalmente per
 * ripristinare una playlist eliminata tramite {@link RemovePlaylistCommand#undo()}.
 *
 * @version 1.0
 */
public class AddPlaylistCommand implements Command {

    private final Playlist playlist;

    /**
     * Crea il comando per l'aggiunta della playlist indicata.
     *
     * @param playlist la {@link Playlist} da aggiungere
     */
    public AddPlaylistCommand(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Aggiunge la playlist alla collezione tramite il {@link PlaylistManager}.
     */
    @Override
    public void execute() {
        // Usa il nuovo metodo che abbiamo appena aggiunto
        PlaylistManager.getInstance().addPlaylist(playlist);
    }

    /**
     * Annulla l'aggiunta rimuovendo la playlist dalla collezione.
     */
    @Override
    public void undo() {
        // Usa il metodo deletePlaylist che avevi già scritto!
        PlaylistManager.getInstance().deletePlaylist(playlist);
    }
}
