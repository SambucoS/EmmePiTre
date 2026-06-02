package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Library;
import models.Track;

public class AddTrackController {

    @FXML private TextField txtPathname;
    @FXML private TextField txtName;
    @FXML private TextField txtArtist;
    @FXML private TextField txtAlbum;
    @FXML private TextField txtGenre;
    @FXML private TextField txtYear;
    @FXML private TextField txtDuration;
    @FXML private CheckBox chkFavourite;
    @FXML private CheckBox chkExplicit;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private Track trackArrived = null;
    private boolean saveClicked = false;
    @FXML
    public void initialize() {

    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Track getTrack() {
        return trackArrived;
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (isInputValid()) {
            // Estraiamo i dati stringa e booleani
            String pathname = txtPathname.getText();
            String name = txtName.getText();
            String artist = txtArtist.getText();
            String album = txtAlbum.getText();
            String genre = txtGenre.getText();
            boolean favourite = chkFavourite.isSelected();
            boolean explicit = chkExplicit.isSelected();

            // Convertiamo i dati numerici (sappiamo che sono validi grazie a isInputValid)
            int year = Integer.parseInt(txtYear.getText());
            int duration = Integer.parseInt(txtDuration.getText());

            // Creiamo il nuovo oggetto Track usando il costruttore completo
            trackArrived = new Track(pathname, name, artist, album, genre, year, favourite, explicit, duration);

            saveClicked = true;
            Library.getInstance().addTrack(trackArrived);

            closeStage();
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        // Chiude semplicemente la modale senza salvare nulla
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Valida l'input dell'utente nei campi di testo.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (txtPathname.getText() == null || txtPathname.getText().isBlank()) {
            errorMessage += "Percorso file non valido!\n";
        }
        if (txtName.getText() == null || txtName.getText().isBlank()) {
            errorMessage += "Titolo non valido!\n";
        }
        if (txtArtist.getText() == null || txtArtist.getText().isBlank()) {
            errorMessage += "Artista non valido!\n";
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Per favore, correggi i campi errati");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}