package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Playlist;
import models.PlaylistManager;
import models.Track;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {

    // Campo di testo usato nella schermata createPlaylist.fxml
    @FXML
    private TextField playlistNameField;

    // Label usata per mostrare messaggi di conferma o errore
    @FXML
    private Label messageLabel;

    // ComboBox usata nella schermata managePlaylist.fxml
    // Serve per selezionare la playlist da gestire
    @FXML
    private ComboBox<Playlist> playlistComboBox;

    // ListView delle tracce presenti nella playlist selezionata
    @FXML
    private ListView<Track> playlistTracksListView;

    // Campo per rinominare la playlist, per ora solo grafico
    @FXML
    private TextField renamePlaylistField;

    // Campi del form "Aggiungi traccia", per ora solo grafici
    @FXML
    private TextField trackTitleField;

    @FXML
    private TextField trackArtistField;

    @FXML
    private TextField trackAlbumField;

    @FXML
    private TextField trackGenreField;

    @FXML
    private TextField trackYearField;

    @FXML
    private TextField trackDurationField;

    /*
     * Metodo chiamato automaticamente da JavaFX quando viene caricato un FXML.
     *
     * Questo controller viene usato sia da createPlaylist.fxml sia da managePlaylist.fxml.
     * Per questo alcuni elementi possono essere null:
     * - createPlaylist.fxml usa playlistNameField e messageLabel
     * - managePlaylist.fxml usa playlistComboBox e playlistTracksListView
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
         * Se playlistComboBox non è null, significa che stiamo caricando
         * la schermata managePlaylist.fxml.
         *
         * Qui carichiamo tutte le playlist disponibili nella ComboBox.
         */
        if (playlistComboBox != null) {
            playlistComboBox.getItems().setAll(
                    PlaylistManager.getInstance().getPlaylists()
            );

            /*
             * Quando l'utente seleziona una playlist dalla ComboBox,
             * aggiorniamo la ListView centrale con le tracce contenute
             * nella playlist selezionata.
             */
            playlistComboBox.setOnAction(event -> {
                Playlist selectedPlaylist = playlistComboBox.getValue();
                updateTracksList(selectedPlaylist);
            });
        }
    }
    /*
    * Aggiorna la ListView delle tracce in base alla playlist selezionata.
            *
            * Se la playlist selezionata è null, la lista delle tracce viene semplicemente svuotata.
     * Se invece la playlist è valida, vengono mostrate tutte le tracce contenute al suo interno.
            *
            * @param selectedPlaylist playlist selezionata dall'utente.
            */
    private void updateTracksList(Playlist selectedPlaylist) {
        // Controllo necessario perché la ListView esiste solo in managePlaylist.fxml.
        if (playlistTracksListView != null) {

            // Svuota la lista prima di caricare le tracce della nuova playlist selezionata.
            playlistTracksListView.getItems().clear();

            // Se è stata selezionata una playlist valida, mostra le sue tracce.
            if (selectedPlaylist != null) {
                playlistTracksListView.getItems().setAll(
                        selectedPlaylist.getTracks()
                );
            }
        }
    }

    /**
     * Gestisce il click sul pulsante "Crea" della schermata createPlaylist.fxml.
     *
     * Legge il nome inserito dall'utente, chiama il PlaylistManager per creare
     * una nuova playlist e mostra un messaggio di conferma o errore.
     *
     * Eventuali errori di validazione, come il nome vuoto, vengono intercettati
     * e mostrati nella GUI tramite messageLabel.
     */
    @FXML
    private void handleCreatePlaylist() {

        // Recupera il nome della playlist scritto dall'utente.
        String playlistName = playlistNameField.getText();

        try {
            // Crea la nuova playlist attraverso il manager.
            PlaylistManager.getInstance().createPlaylist(playlistName);

            // Messaggio di conferma in caso di creazione corretta.
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Playlist creata correttamente.");
            // Pulisce il campo di input dopo la creazione.
            playlistNameField.clear();

        } catch (IllegalArgumentException e) {

            // Mostra nella GUI eventuali errori di validazione.
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    /**
     * Gestisce il click sul pulsante "Annulla" della schermata createPlaylist.fxml.
     *
     * Pulisce eventuali dati inseriti, cancella i messaggi mostrati nella GUI
     * e chiude la finestra/modale corrente.
     *
     * @param event evento generato dal click sul pulsante Annulla.
     */
    @FXML
    private void handleCancel(ActionEvent event) {

        // Pulisce il campo del nome, se presente.
        if (playlistNameField != null) {
            playlistNameField.clear();
        }

        // Cancella eventuali messaggi di errore o conferma.
        if (messageLabel != null) {
            messageLabel.setText("");
        }

        // Recupera la finestra corrente a partire dal pulsante cliccato.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Chiude la finestra/modale.
        stage.close();
    }

    /**
     * Gestisce il click sul pulsante "Rimuovi traccia" della schermata managePlaylist.fxml.
     *
     * Recupera la playlist selezionata e la traccia selezionata nella ListView,
     * poi chiede al PlaylistManager di rimuovere la traccia dalla playlist.
     *
     * La traccia viene rimossa solo dalla playlist selezionata e non dalla libreria principale.
     * Eventuali errori, come playlist non selezionata o traccia non selezionata,
     * vengono intercettati e mostrati nella GUI.
     */
    @FXML
    private void handleRemoveTrackFromPlaylist() {
        Playlist selectedPlaylist = null;

        // Recupera la playlist selezionata nella ComboBox.
        if (playlistComboBox != null) {
            selectedPlaylist = playlistComboBox.getValue();
        }

        Track selectedTrack = null;

        // Recupera la traccia selezionata nella ListView.
        if (playlistTracksListView != null) {
            selectedTrack = playlistTracksListView.getSelectionModel().getSelectedItem();
        }

        try {
            // Rimuove la traccia dalla playlist attraverso il PlaylistManager.
            PlaylistManager.getInstance().removeTrackFromPlaylist(selectedTrack, selectedPlaylist);

            // Aggiorna anche la ListView, rimuovendo visivamente la traccia.
            if (playlistTracksListView != null) {
                playlistTracksListView.getItems().remove(selectedTrack);
            }

            // Messaggio di conferma.
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Traccia rimossa correttamente dalla playlist.");

        } catch (IllegalArgumentException e) {

            // Mostra eventuali errori nella GUI.
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    /**
     * Gestisce il click sul pulsante "Annulla" della schermata managePlaylist.fxml.
     *
     * Azzera la playlist selezionata, svuota la lista delle tracce mostrata
     * e cancella eventuali messaggi di conferma o errore.
     */
    @FXML
    private void handleCancelRemove() {

        // Deseleziona la playlist scelta nella ComboBox.
        if (playlistComboBox != null) {
            playlistComboBox.setValue(null);
        }

        // Svuota la ListView delle tracce.
        if (playlistTracksListView != null) {
            playlistTracksListView.getItems().clear();
        }

        // Cancella eventuali messaggi mostrati all'utente.
        if (messageLabel != null) {
            messageLabel.setText("");
        }
    }
}