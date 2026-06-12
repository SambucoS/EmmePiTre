package commands;

import models.Playlist;
import models.PlaylistManager;
import models.Track;

import java.util.List;

public class ManagePlaylistCommand implements Command {

    private final Playlist playlist;
    private final String newName;
    private final String oldName;
    private final List<Track> tracksToAdd;
    private final List<Track> tracksToRemove;

    public ManagePlaylistCommand(Playlist playlist, String newName, String oldName,
                                  List<Track> tracksToAdd, List<Track> tracksToRemove) {
        this.playlist = playlist;
        this.newName = newName;
        this.oldName = oldName;
        this.tracksToAdd = List.copyOf(tracksToAdd);
        this.tracksToRemove = List.copyOf(tracksToRemove);
    }

    @Override
    public void execute() {
        if (!oldName.equals(newName)) {
            PlaylistManager.getInstance().renamePlaylist(playlist, newName);
        }
        for (Track t : tracksToAdd) {
            PlaylistManager.getInstance().addTrackToPlaylist(t, playlist);
        }
        for (Track t : tracksToRemove) {
            PlaylistManager.getInstance().removeTrackFromPlaylist(t, playlist);
        }
    }

    @Override
    public void undo() {
        if (!oldName.equals(newName)) {
            PlaylistManager.getInstance().renamePlaylist(playlist, oldName);
        }
        for (Track t : tracksToAdd) {
            PlaylistManager.getInstance().removeTrackFromPlaylist(t, playlist);
        }
        for (Track t : tracksToRemove) {
            PlaylistManager.getInstance().addTrackToPlaylist(t, playlist);
        }
    }
}
