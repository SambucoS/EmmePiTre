package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlaylistController {

    @FXML
    private TextField playlistNameField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleCreatePlaylist() {
        String playlistName = playlistNameField.getText();

        try {
            models.PlaylistManager.getInstance().createPlaylist(playlistName);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Playlist creata correttamente.");
            playlistNameField.clear();

        } catch (IllegalArgumentException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        playlistNameField.clear();
        messageLabel.setText("");
    }
}