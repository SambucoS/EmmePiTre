package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Playlist;
import models.Track;
import services.PlaylistService;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class PlaylistController {

    @FXML
    private TextField playlistNameField;

    @FXML
    private Label messageLabel;

    private final PlaylistService playlistService = new PlaylistService();

    @FXML
    private Label messageLabel1;

    @FXML
    private ListView<?> playlistListView;

    @FXML
    private ListView<?> trackListView;



    @FXML
    private void handleCreatePlaylist() {
        String playlistName = playlistNameField.getText();
/*
        try {
            playlistService.createPlaylist(playlistName);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Playlist creata correttamente.");

            playlistNameField.clear();

        } catch (IllegalArgumentException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }*/

       // String playlistName = playlistNameField.getText();

        try {
            playlistService.createPlaylist(playlistName);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Playlist creata correttamente.");

            playlistNameField.clear();

            // NAVIGAZIONE ALLA PLAYLIST VIEW
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlaylistView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) playlistNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IllegalArgumentException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());

        } catch (IOException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Errore nel caricamento della view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        playlistNameField.clear();
        messageLabel.setText("");
    }

    @FXML
    private void handleAddTrack() {

        Track selectedTrack =
                LibraryViewController.getTrack();

        if (selectedTrack == null) {

            messageLabel.setText("Seleziona una traccia.");
            return;
        }

        Playlist.addTrack(selectedTrack);

        trackListView.getItems().setAll(
                //currentPlaylist.getTracks()
        );

        messageLabel.setText("Traccia aggiunta.");
    }

}