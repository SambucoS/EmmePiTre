package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

import java.util.List;

public class ManageTrackPlaylistsCommand implements Command {

    private final Track track;
    private final List<Playlist> toAdd;
    private final List<Playlist> toRemove;

    public ManageTrackPlaylistsCommand(Track track, List<Playlist> toAdd, List<Playlist> toRemove) {
        this.track = track;
        this.toAdd = List.copyOf(toAdd);
        this.toRemove = List.copyOf(toRemove);
    }

    @Override
    public void execute() {
        for (Playlist p : toAdd) {
            PlaylistManager.getInstance().addTrackToPlaylist(track, p);
        }
        for (Playlist p : toRemove) {
            PlaylistManager.getInstance().removeTrackFromPlaylist(track, p);
        }
    }

    @Override
    public void undo() {
        for (Playlist p : toAdd) {
            PlaylistManager.getInstance().removeTrackFromPlaylist(track, p);
        }
        for (Playlist p : toRemove) {
            PlaylistManager.getInstance().addTrackToPlaylist(track, p);
        }
    }
}
