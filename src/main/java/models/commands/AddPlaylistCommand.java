package models.commands;

import models.Playlist;
import models.PlaylistManager;

public class AddPlaylistCommand implements Command {

    private final Playlist playlist;

    public AddPlaylistCommand(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public void execute() {
        // Usa il nuovo metodo che abbiamo appena aggiunto
        PlaylistManager.getInstance().addPlaylist(playlist);
    }

    @Override
    public void undo() {
        // Usa il metodo deletePlaylist che avevi già scritto!
        PlaylistManager.getInstance().deletePlaylist(playlist);
    }
}