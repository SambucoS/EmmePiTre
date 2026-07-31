package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

import java.util.List;

/**
 * Comando (pattern Command) che raggruppa in un'unica operazione atomica la
 * rinomina di una playlist e l'aggiunta/rimozione di piu' tracce, cosi' come
 * scelto dall'utente nella modale "Gestisci Playlist". Le aggiunte/rimozioni
 * di tracce sono composte in un {@link CompositeCommand} di
 * {@link AddTrackToPlaylistCommand} e {@link RemoveTrackFromPlaylistCommand};
 * la rinomina resta invece un'operazione diretta e non annullabile
 * separatamente, poiche' non necessita di integrazione con Undo/Redo.
 *
 * @version 1.0
 */
public class ManagePlaylistCommand implements Command {

    private final Playlist playlist;
    private final String newName;
    private final String oldName;
    private final CompositeCommand tracksComposite = new CompositeCommand();

    /**
     * Crea il comando con lo stato prima e dopo la modifica della playlist.
     *
     * @param playlist       la {@link Playlist} da modificare
     * @param newName        il nome da assegnare alla playlist
     * @param oldName        il nome precedente della playlist, usato per l'undo
     * @param tracksToAdd    le tracce da aggiungere alla playlist
     * @param tracksToRemove le tracce da rimuovere dalla playlist
     */
    public ManagePlaylistCommand(Playlist playlist, String newName, String oldName,
                                  List<Track> tracksToAdd, List<Track> tracksToRemove) {
        this.playlist = playlist;
        this.newName = newName;
        this.oldName = oldName;
        for (Track t : tracksToAdd) {
            tracksComposite.add(new AddTrackToPlaylistCommand(playlist, t));
        }
        for (Track t : tracksToRemove) {
            tracksComposite.add(new RemoveTrackFromPlaylistCommand(playlist, t));
        }
    }

    /**
     * Applica la rinomina (se cambiata) e le aggiunte/rimozioni di tracce.
     */
    @Override
    public void execute() {
        if (!oldName.equals(newName)) {
            PlaylistManager.getInstance().renamePlaylist(playlist, newName);
        }
        tracksComposite.execute();
    }

    /**
     * Ripristina il nome precedente e inverte le aggiunte/rimozioni di tracce.
     */
    @Override
    public void undo() {
        if (!oldName.equals(newName)) {
            PlaylistManager.getInstance().renamePlaylist(playlist, oldName);
        }
        tracksComposite.undo();
    }
}
