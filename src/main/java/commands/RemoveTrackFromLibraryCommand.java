package commands;

import models.Library;
import models.Playlist;
import models.PlaylistManager;
import models.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Comando (pattern Command) che elimina definitivamente una traccia dalla
 * libreria, a cascata anche da tutte le playlist che la contengono. Le
 * playlist coinvolte vengono memorizzate durante {@link #execute()} cosi'
 * che {@link #undo()} possa ripristinare esattamente lo stato precedente.
 *
 * @version 1.0
 */
public class RemoveTrackFromLibraryCommand implements Command {

    private final Track track;
    // Playlist che contenevano la traccia: salvate in execute(), ripristinate in undo()
    private final List<Playlist> affectedPlaylists = new ArrayList<>();

    /**
     * Crea il comando per l'eliminazione a cascata della traccia indicata.
     *
     * @param track la {@link Track} da eliminare dalla libreria e dalle playlist
     */
    public RemoveTrackFromLibraryCommand(Track track) {
        this.track = track;
    }

    /**
     * Individua le playlist che contengono la traccia, la rimuove da ciascuna
     * di esse e infine la elimina dalla libreria.
     */
    @Override
    public void execute() {
        affectedPlaylists.clear();
        for (Playlist p : PlaylistManager.getInstance().getPlaylists()) {
            if (p.containsTrack(track)) {
                affectedPlaylists.add(p);
            }
        }
        for (Playlist p : affectedPlaylists) {
            PlaylistManager.getInstance().removeTrackFromPlaylist(track, p);
        }
        Library.getInstance().removeTrack(track);
    }

    /**
     * Ripristina la traccia in libreria e la riaggiunge a tutte le playlist
     * che la contenevano prima dell'eliminazione.
     */
    @Override
    public void undo() {
        Library.getInstance().addTrack(track);
        for (Playlist p : affectedPlaylists) {
            PlaylistManager.getInstance().addTrackToPlaylist(track, p);
        }
    }
}
