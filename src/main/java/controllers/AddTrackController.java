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
import models.commands.AddTrackCommand;
import models.commands.Command;
import models.commands.CommandManager;

import java.util.ArrayList;
import java.util.List;

/** ============ ADDTRACKCONTROLLER ============
* si occupa di gestire l'inserimento di nuovi parametri, della creazione e del salvataggio
di una nuova traccia nella libreria ( file tracks.json )*/

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

    private Track trackAdded = null; // Rappresenta la nuova traccia


    @FXML
    public void initialize() {

    }

    // ritorna la traccia
    public Track getTrack() {
        return trackAdded;
    }

    /**
     * Estrae i dati dai campi e aggiunge la nuova traccia creata alla libreria
     * @param event che risulta essere l'evento catturato (pressione del bottone "Salva")
     */
    @FXML
    void handleSave(ActionEvent event) {

        // richiama il metodo per il controllo degli errori
        if (isInputValid()) {

            // Vengono estratti i dati dai campi del form
            String pathname = txtPathname.getText();
            String name = txtName.getText();
            String artist = txtArtist.getText();
            String album = txtAlbum.getText();
            String genre = txtGenre.getText();
            boolean favourite = chkFavourite.isSelected();
            boolean explicit = chkExplicit.isSelected();

            // Viene convertito il contenuto nei campi year e duration da String a Integer
            int year = Integer.parseInt(txtYear.getText());
            int duration = Integer.parseInt(txtDuration.getText());

            // Creazione della nuova traccia
            trackAdded = new Track(pathname, name, artist, album, genre, year, favourite, explicit, duration);

            // Aggiunta alla libreria
            Command addCmd = new AddTrackCommand(Library.getInstance(), trackAdded);
            CommandManager.getInstance().executeCommand(addCmd);

            // Chiusura della modale
            closeStage();
        }
    }

    @FXML // Chiude la modale
    void handleCancel(ActionEvent event) {

        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


    /** Imposta il colore del bordo a rosso e definisce uno spessore
        @params t è il textfield su cui apporre la modifica di stile
     */
    public void setBorderRed(TextField t) {

        t.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    /** Annulla la modifica del bordo
        @params t è il textfield su cui apporre il reset dellostile
     */
    public void removeBorderRed(TextField t){
        t.setStyle("");
    }

    /**
     * Valida l'input dell'utente nei campi di testo.
     * tutte le verifiche seguono gli step:
     * controllo che il campo non sia vuoto/ci siano problemi (year/duration)
     * --> set del bordo rosso se ci sono errori || reset dello stile
     * per gestire il caso in cui l'utente commette errori più volte
     *
     * @return un booleano che definisce se gli input nei campi sono validi o meno
     * @throws NumberFormatException generata quando il testo
     * contiene caratteri che un numero non può avere
     */
    private boolean isInputValid() {
        List<String> errorMessages = new ArrayList<>(); // viene inizializzata la lista dei messaggi

        if (txtPathname.getText() == null || txtPathname.getText().isBlank()) {
            errorMessages.add("Percorso file non valido!\n");
            setBorderRed(txtPathname);

        } else {
            removeBorderRed(txtPathname);
        }
        if (txtName.getText() == null || txtName.getText().isBlank()) {
            errorMessages.add("Perfavore inserisci il nome della canzone\n");
            setBorderRed(txtName);
        } else {
            removeBorderRed(txtName);
        }
        if (txtArtist.getText() == null || txtArtist.getText().isBlank()) {
            errorMessages.add("Perfavore inserisci l'artista della canzone!\n");
            setBorderRed(txtArtist);
        }else {
            removeBorderRed(txtArtist);
        }

        if (txtGenre.getText() == null || txtGenre.getText().isBlank()) {
            errorMessages.add("Perfavore inserisci il genere della canzone\n");
            setBorderRed(txtGenre);
        }else{
            removeBorderRed(txtGenre);
        }

        if (txtAlbum.getText() == null || txtAlbum.getText().isBlank()) {
            errorMessages.add("Perfavore inserisci l'album della canzone\n");
            setBorderRed(txtAlbum);
        }else{
            removeBorderRed(txtAlbum);
        }

        // Validazione Anno
        if (txtYear.getText() == null || txtYear.getText().isBlank()) {
            errorMessages.add("Perfavore inserisci l'anno di rilascio della canzone\n");
            setBorderRed(txtYear);
        } else {
            try {
                Integer.parseInt(txtYear.getText().trim()); // Vengono tolti anche gli spazi per evitare situazioni del tipo "  2015 "
                removeBorderRed(txtYear);
            } catch (NumberFormatException e) {
                errorMessages.add("L'anno deve essere un numero intero!\n");
                setBorderRed(txtYear);
            }

        }

        // Validazione Durata
        if (txtDuration.getText() == null || txtDuration.getText().isBlank()) {
            errorMessages.add("Perfavore inserisci la durata della canzone\n");
            setBorderRed(txtDuration);
        } else {
            try {
                Integer.parseInt(txtDuration.getText().trim());
                removeBorderRed(txtDuration);
            } catch (NumberFormatException e) {
                errorMessages.add("La durata deve essere un numero intero (in secondi)!\n");
            }
        }

        if (errorMessages.size() == 0) { // Se non ci sono errori l'input è valido
            return true;
        } else {
            // Mostra una finestra di avviso in caso di errore
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Per favore, correggi i campi errati");
            if(errorMessages.size() == 1){ // Se il messaggio di errore è uno solo, viene stampato a video
            alert.setContentText(errorMessages.get(0));}
            alert.showAndWait();
            return false;
        }
    }
}