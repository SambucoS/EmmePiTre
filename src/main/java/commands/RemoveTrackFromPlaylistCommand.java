package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

public class RemoveTrackFromPlaylistCommand implements Command {

    private final Playlist playlist;
    private final Track track;

    public RemoveTrackFromPlaylistCommand(Playlist playlist, Track track) {
        this.playlist = playlist;
        this.track = track;
    }

    @Override
    public void execute() {
        // Usa il manager così scatta in automatico il this.sync() e salva su JSON!
        PlaylistManager.getInstance().removeTrackFromPlaylist(track, playlist);
    }

    @Override
    public void undo() {
        // Per annullare, la riaggiungiamo (sempre tramite manager per il JSON)
        PlaylistManager.getInstance().addTrackToPlaylist(track, playlist);
    }
}