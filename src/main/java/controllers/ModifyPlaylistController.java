package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Playlist;
import models.PlaylistManager;

public class ModifyPlaylistController {

    @FXML
    private Label currentPlaylistNameLabel;

    @FXML
    private TextField newPlaylistNameField;

    @FXML
    private Label messageLabel;

    private Playlist playlistToModify;
    private boolean saved = false;

    public void setPlaylistToModify(Playlist playlist) {
        this.playlistToModify = playlist;

        if (playlist != null) {
            currentPlaylistNameLabel.setText("Nome attuale: " + playlist.getName());
            newPlaylistNameField.setText(playlist.getName());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        if (playlistToModify == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Nessuna playlist selezionata.");
            return;
        }

        String newName = newPlaylistNameField.getText();

        try {
            PlaylistManager.getInstance().renamePlaylist(playlistToModify, newName);

            saved = true;
            closeWindow();

        } catch (IllegalArgumentException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        saved = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) currentPlaylistNameLabel.getScene().getWindow();
        stage.close();
    }
}