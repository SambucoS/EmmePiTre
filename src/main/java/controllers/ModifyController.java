package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Track;

/**
 * Gestisce l'interfaccia utente per la modifica dei dettagli di una traccia musicale.
 * Questa classe funge da controller per la vista di modifica e permette di
 * convalidare e aggiornare i dati di un oggetto {@link Track} esistente.
 * * @author GRUPPO_14_SAD
 * @version 1.0
 */
public class ModifyController {

    @FXML
    private Button btnSalva;
    @FXML
    private CheckBox chkExplicit;
    @FXML
    private CheckBox chkFavourite;
    @FXML
    private ComboBox<String> cmbGenre;
    @FXML
    private TextField txtAlbum;
    @FXML
    private TextField txtArtist;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtYear;

    @FXML
    private CheckBox chkNewRelease;

    private Track track;
    private Runnable onModifyDone;

    public void setOnModifyDone(Runnable onModifyDone) {
        this.onModifyDone = onModifyDone;
    }

    /**
     * Inizializza il controller. Questo metodo viene chiamato automaticamente
     * da JavaFX dopo il caricamento del file FXML.
     * Si occupa di popolare la tendina dei generi musicali.
     */
    @FXML
    public void initialize() {
        cmbGenre.getItems().addAll(
                "Pop", "Rock", "Rap", "Jazz", "Classical", "EDM"
        );
    }

    /**
     * Imposta la traccia musicale che l'utente desidera modificare.
     * Richiama automaticamente il caricamento dei dati nell'interfaccia.
     *
     * @param track la traccia {@link Track} da modificare
     */
    public void setTrack(Track track) {
        this.track = track;
        loadData();
    }

    /**
     * Carica i dettagli della traccia selezionata e li inserisce nei
     * rispettivi campi di testo e controlli dell'interfaccia utente.
     */
    private void loadData() {
        txtName.setText(track.getName());
        txtArtist.setText(track.getArtist());
        txtAlbum.setText(track.getAlbum());
        txtYear.setText(String.valueOf(track.getYear()));
        txtDuration.setText(String.valueOf(track.getDuration()));

        chkExplicit.setSelected(track.isExplicit());
        chkFavourite.setSelected(track.isFavourite());
        chkNewRelease.setSelected(track.isNewRelease());

        cmbGenre.setValue(track.getGenre());
    }

    /**
     * Gestisce l'evento di salvataggio delle modifiche.
     * Se l'input risulta valido, aggiorna i dati dell'oggetto {@link Track},
     * sincronizza la libreria musicale, notifica gli observer e chiude la finestra.
     *
     * @param event l'evento generato dalla pressione del pulsante "Salva"
     */
    @FXML
    void handleSalva(ActionEvent event) {
        if (!isValid()) return;

        track.setName(txtName.getText());
        track.setArtist(txtArtist.getText());
        track.setAlbum(txtAlbum.getText());
        track.setGenre(cmbGenre.getValue());

        track.setExplicit(chkExplicit.isSelected());
        track.setFavourite(chkFavourite.isSelected());

        track.setNewRelease(chkNewRelease.isSelected());

        track.setYear(Integer.parseInt(txtYear.getText()));
        track.setDuration(Integer.parseInt(txtDuration.getText()));

        models.Library.getInstance().sync();

        models.Library.getInstance().notifyObservers();
        if (onModifyDone != null) {
            onModifyDone.run();
        }
        closeWindow();
    }

    /**
     * Gestisce l'evento di annullamento.
     * Interrompe l'operazione di modifica e chiude la finestra corrente.
     *
     * @param event l'evento generato dalla pressione del pulsante "Annulla"
     */
    @FXML
    void handleAnnulla(ActionEvent event) {
        closeWindow();
    }

    /**
     * Recupera lo stage attuale e lo chiude definitivamente,
     * nascondendo la finestra e liberando le risorse.
     */
    private void closeWindow() {

        Stage stage = (Stage) btnSalva.getScene().getWindow();
        stage.close();
    }

    /**
     * Valida i dati inseriti dall'utente nei campi dell'interfaccia.
     * Controlla che i campi testuali non siano vuoti e che anno e durata
     * siano formattati correttamente come numeri interi. Mostra un {@link Alert}
     * in caso di errori di validazione.
     *
     * @return {@code true} se tutti i parametri inseriti sono validi, {@code false} in caso contrario
     */
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

        if (txtYear.getText() == null || txtYear.getText().isBlank()) {
            errorMessage += "Anno non valido!\n";
        } else {
            try {
                Integer.parseInt(txtYear.getText());
            } catch (NumberFormatException e) {
                errorMessage += "L'anno deve essere un numero intero!\n";
            }
        }

        if (txtDuration.getText() == null || txtDuration.getText().isBlank()) {
            errorMessage += "Durata non valida!\n";
        } else {
            try {
                Integer.parseInt(txtDuration.getText());
            } catch (NumberFormatException e) {
                errorMessage += "La durata deve essere un numero intero (in secondi)!\n";
            }
        }

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



