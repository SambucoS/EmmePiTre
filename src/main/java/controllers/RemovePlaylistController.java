package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Playlist;
import models.PlaylistManager;

public class RemovePlaylistController {

    @FXML
    private Label playlistNameLabel;

    private Playlist playlistToRemove;

    public void setPlaylistToRemove(Playlist playlist) {
        this.playlistToRemove = playlist;

        if (playlistNameLabel != null && playlist != null) {
            playlistNameLabel.setText("Playlist: " + playlist.getName());
        }
    }

    @FXML
    private void handleConfirmDelete() {
        if (playlistToRemove != null) {
            PlaylistManager.getInstance().deletePlaylist(playlistToRemove);
        }

        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) playlistNameLabel.getScene().getWindow();
        stage.close();
    }
}