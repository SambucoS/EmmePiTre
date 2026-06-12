package commands;

import models.Playlist;
import models.PlaylistManager;

public class RemovePlaylistCommand implements Command {

    private final Playlist playlist;

    public RemovePlaylistCommand(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public void execute() {
        PlaylistManager.getInstance().deletePlaylist(playlist);
    }

    @Override
    public void undo() {
        PlaylistManager.getInstance().addPlaylist(playlist);
    }
}