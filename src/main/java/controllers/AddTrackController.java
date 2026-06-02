package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Track;
import services.LibraryService;

import java.util.List;
public class AddTrackController {

    @FXML private TextField nameField;
    @FXML private TextField artistField;
    @FXML private TextField genreField;
    @FXML private TextField txtAlbum;
    @FXML private TextField yearField;
    @FXML private TextField durationField;
    @FXML private CheckBox favouriteCheck;
    @FXML private CheckBox explicitCheck;

    private Track createdTrack;

    @FXML
    private void onSave() {
        createdTrack = new Track(
                null,
                nameField.getText(),
                artistField.getText(),
                genreField.getText(),
                txtAlbum.getText(),
                yearField.getText(),
                favouriteCheck.isSelected(),
                explicitCheck.isSelected(),
                Integer.parseInt(durationField.getText())
        );

        // chiudi finestra
        close();
    }

    @FXML
    private void onCancel() {
        createdTrack = null;
        close();
    }

    private void close() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public Track getCreatedTrack() {
        return createdTrack;
    }
}
