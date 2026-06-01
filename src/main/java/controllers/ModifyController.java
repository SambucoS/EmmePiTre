package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Track;

public class ModifyController {

    @FXML private Button btnAnnulla;
    @FXML private Button btnSalva;
    @FXML private CheckBox chkExplicit;
    @FXML private CheckBox chkFavourite;
    @FXML private ComboBox<String> cmbGenre;
    @FXML private TextField txtAlbum;
    @FXML private TextField txtArtist;
    @FXML private TextField txtDuration;
    @FXML private TextField txtName;
    @FXML private TextField txtYear;

    private Track track;

    @FXML
    public void initialize() {
        cmbGenre.getItems().addAll(
                "Pop", "Rock", "Rap", "Jazz", "Classical", "EDM"
        );
    }

    public void setTrack(Track track) {
        this.track = track;
        loadData();
    }

    private void loadData() {
        txtName.setText(track.getName());
        txtArtist.setText(track.getArtist());
        txtAlbum.setText(track.getAlbum());
        txtYear.setText(String.valueOf(track.getYear()));
        txtDuration.setText(String.valueOf(track.getDuration()));

        chkExplicit.setSelected(track.isExplicit());
        chkFavourite.setSelected(track.isFavourite());

        cmbGenre.setValue(track.getGenre());
    }

    @FXML
    void handleSalva(ActionEvent event) {

        if (!isValid()) return;

        track.setName(txtName.getText());
        track.setArtist(txtArtist.getText());
        track.setAlbum(txtAlbum.getText());
        track.setGenre((String) cmbGenre.getValue());

        track.setExplicit(chkExplicit.isSelected());
        track.setFavourite(chkFavourite.isSelected());

        track.setYear(Integer.parseInt(txtYear.getText()));
        track.setDuration(Integer.parseInt(txtDuration.getText()));

        closeWindow();
    }

    @FXML
    void handleAnnulla(ActionEvent event) {
        closeWindow();
    }

    private boolean isValid() {

        if (txtName.getText().isEmpty() ||
                txtArtist.getText().isEmpty()) {
            return false;
        }

        try {
            int year = Integer.parseInt(txtYear.getText());
            int duration = Integer.parseInt(txtDuration.getText());

            if (year < 1900 || duration <= 0) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private void closeWindow() {
        btnSalva.getScene().getWindow().hide();
    }
}

