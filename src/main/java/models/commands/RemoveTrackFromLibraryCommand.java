package models.commands;

import models.Library;
import models.Playlist;
import models.PlaylistManager;
import models.Track;

import java.util.ArrayList;
import java.util.List;

public class RemoveTrackFromLibraryCommand implements Command {

    private final Track track;
    // Playlist che contenevano la traccia: salvate in execute(), ripristinate in undo()
    private final List<Playlist> affectedPlaylists = new ArrayList<>();

    public RemoveTrackFromLibraryCommand(Track track) {
        this.track = track;
    }

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

    @Override
    public void undo() {
        Library.getInstance().addTrack(track);
        for (Playlist p : affectedPlaylists) {
            PlaylistManager.getInstance().addTrackToPlaylist(track, p);
        }
    }
}
