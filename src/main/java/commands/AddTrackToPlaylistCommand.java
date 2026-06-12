package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

public class AddTrackToPlaylistCommand implements Command {

    private final Playlist playlist;
    private final Track track;

    public AddTrackToPlaylistCommand(Playlist playlist, Track track) {
        this.playlist = playlist;
        this.track = track;
    }

    @Override
    public void execute() {
        // Esegue l'aggiunta e salva su JSON in automatico tramite il manager
        PlaylistManager.getInstance().addTrackToPlaylist(track, playlist);
    }

    @Override
    public void undo() {
        // Per annullare un'aggiunta, la rimuoviamo (sempre tramite manager)
        PlaylistManager.getInstance().removeTrackFromPlaylist(track, playlist);
    }
}