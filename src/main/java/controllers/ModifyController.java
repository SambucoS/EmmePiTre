package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
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

        // 1. Aggiorna l'oggetto Track in RAM (Codice che hai già scritto)
        track.setName(txtName.getText());
        track.setArtist(txtArtist.getText());
        track.setAlbum(txtAlbum.getText());
        track.setGenre(cmbGenre.getValue());

        track.setExplicit(chkExplicit.isSelected());
        track.setFavourite(chkFavourite.isSelected());

        track.setYear(Integer.parseInt(txtYear.getText()));
        track.setDuration(Integer.parseInt(txtDuration.getText()));

        models.Library.getInstance().sync();

        models.Library.getInstance().notifyObservers();

        closeWindow();
    }

    @FXML
    void handleAnnulla(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        // Recupera lo stage attuale e lo chiude definitivamente, liberando le risorse
        Stage stage = (Stage) btnSalva.getScene().getWindow();
        stage.close();
    }

    private boolean isValid() {
        String errorMessage = "";

        if (txtName.getText() == null || txtName.getText().isBlank()) {
            errorMessage += "Titolo non valido!\n";
        }

        if (txtArtist.getText() == null || txtArtist.getText().isBlank()) {
            errorMessage += "Artista non valido!\n";
        }

        if (txtAlbum.getText() == null || txtAlbum.getText().isBlank()) {
            errorMessage += "Album non valido!\n";
        }

        if (cmbGenre.getValue() == null || cmbGenre.getValue().isBlank()) {
            errorMessage += "Genere non valido!\n";
        }



        // Validazione Anno
        if (txtYear.getText() == null || txtYear.getText().isBlank()) {
            errorMessage += "Anno non valido!\n";
        } else {
            try {
                Integer.parseInt(txtYear.getText());
            } catch (NumberFormatException e) {
                errorMessage += "L'anno deve essere un numero intero!\n";
            }
        }

        // Validazione Durata
        if (txtDuration.getText() == null || txtDuration.getText().isBlank()) {
            errorMessage += "Durata non valida!\n";
        } else {
            try {
                Integer.parseInt(txtDuration.getText());
            } catch (NumberFormatException e) {
                errorMessage += "La durata deve essere un numero intero (in secondi)!\n";
            }
        }

        // Se non ci sono errori, l'input è valido
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostra una finestra di avviso in caso di errore
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Per favore, correggi i campi errati");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}

