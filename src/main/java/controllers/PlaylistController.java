package controllers;

import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

// Import necessario per collegare gli elementi dell'FXML al controller
import javafx.fxml.FXML;

// Import necessario perché il controller usa il metodo initialize()
import javafx.fxml.Initializable;

// Componenti grafici JavaFX usati negli FXML

// Classi del modello usate dal controller
import models.Playlist;
import models.PlaylistManager;
import models.Track;
import models.commands.*;

// Import richiesti dall'interfaccia Initializable
import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {

    // Campo di testo usato nella schermata createPlaylist.fxml
    // Serve per inserire il nome della nuova playlist
    @FXML
    private TextField playlistNameField;

    // Label usata per mostrare messaggi di conferma o errore
    // È presente sia nella schermata di creazione sia in quella di rimozione
    @FXML
    private Label messageLabel;

    // ComboBox usata nella schermata managePlaylist.fxml
    // Serve per selezionare la playlist da cui rimuovere una traccia
    @FXML
    private ComboBox<Playlist> playlistComboBox;

    // ListView usata nella schermata managePlaylist.fxml
    // Mostra le tracce contenute nella playlist selezionata
    @FXML
    private ListView<Track> playlistTracksListView;

    @FXML
    private Button buttonAggiungiTraccia;

    /*
     * Metodo chiamato automaticamente da JavaFX quando viene caricato l'FXML.
     *
     * Poiché questo controller viene usato da più schermate FXML,
     * alcuni elementi potrebbero essere null.
     *
     * Ad esempio:
     * - in createPlaylist.fxml non esistono playlistComboBox e playlistTracksListView
     * - in managePlaylist.fxml non esiste playlistNameField
     *
     * Per questo motivo controlliamo che playlistComboBox non sia null
     * prima di inizializzare la parte relativa alla rimozione da playlist.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Se la ComboBox esiste, significa che stiamo caricando la schermata managePlaylist.fxml
        if (playlistComboBox != null) {

            playlistComboBox.setConverter(new javafx.util.StringConverter<Playlist>() {
                @Override
                public String toString(Playlist playlist) {
                    if (playlist == null) {
                        return null;
                    }
                    // Usa getName() come hai fatto più sotto per la label
                    return playlist.getName();
                }

                @Override
                public Playlist fromString(String string) {
                    return null;
                }
            });

            // Carica nella ComboBox tutte le playlist presenti nel PlaylistManager
            playlistComboBox.getItems().setAll(
                    PlaylistManager.getInstance().getPlaylists()
            );

            /*
             * Quando l'utente seleziona una playlist dalla ComboBox,
             * la ListView viene aggiornata mostrando le tracce contenute
             * in quella playlist.
             */
            playlistComboBox.setOnAction(event -> {
                Playlist selectedPlaylist = playlistComboBox.getValue();


                // Controlliamo che la ListView esista prima di usarla
                if (playlistTracksListView != null) {

                    // Svuota la lista visualizzata prima di caricare le nuove tracce
                    playlistTracksListView.getItems().clear();

                    // aggiorna la ListView con le tracce della playlist selezionata
                    playlistTracksListView.getItems().setAll(
                            selectedPlaylist.getTracks()
                    );

                    messageLabel.setText("Playlist caricata: " + selectedPlaylist.getName());

                }
            });
        }
    }

    /*
     * Metodo collegato al pulsante "Crea" della schermata createPlaylist.fxml.
     * Legge il nome inserito dall'utente e chiede al PlaylistManager
     * di creare una nuova playlist.
     */
    @FXML
    private void handleCreatePlaylist() {

        // Recupera il testo scritto dall'utente nel campo nome playlist
        String playlistName = playlistNameField.getText();

        try {
            // Crea la playlist usando il manager
            Playlist nuovaPlaylist = new Playlist(playlistName);
            Command addPlaylistCmd = new AddPlaylistCommand(nuovaPlaylist);
            CommandManager.getInstance().executeCommand(addPlaylistCmd);

            // Se la creazione va a buon fine, mostra un messaggio verde
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Playlist creata correttamente.");

            // Pulisce il campo di testo dopo la creazione
            playlistNameField.clear();

        } catch (IllegalArgumentException e) {

            // Se ci sono errori, ad esempio nome vuoto,
            // mostra il messaggio di errore in rosso
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    /*
     * Metodo collegato al pulsante "Annulla" della schermata createPlaylist.fxml.
     * Svuota il campo di testo e cancella eventuali messaggi.
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        // Pulisce il campo di testo, se presente
        if (playlistNameField != null) {
            playlistNameField.clear();
        }

        // Pulisce eventuali messaggi di errore o conferma
        if (messageLabel != null) {
            messageLabel.setText("");
        }

        // Chiude la finestra/modale corrente
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /*
     * Metodo collegato al pulsante "Rimuovi" della schermata managePlaylist.fxml.
     * Rimuove dalla playlist selezionata la traccia scelta nella ListView.
     */
    @FXML
    private void handleRemoveTrackFromPlaylist() {

        // Recupera la playlist selezionata nella ComboBox
        Playlist selectedPlaylist = playlistComboBox.getValue();

        // Recupera la traccia selezionata nella ListView
        Track selectedTrack = playlistTracksListView.getSelectionModel().getSelectedItem();

        try {
            // 1. Creiamo il comando
            Command removeTrackFromPlCmd = new RemoveTrackFromPlaylistCommand(selectedPlaylist, selectedTrack);

            // 2. Lo eseguiamo e lo salviamo nello Stack
            CommandManager.getInstance().executeCommand(removeTrackFromPlCmd);

            // 3. AGGIORNAMENTO GRAFICO "A PROVA DI UNDO":
            // Invece di rimuovere l'elemento a mano, diciamo alla ListView di
            // rileggere l'intera lista aggiornata dalla playlist!
            playlistTracksListView.getItems().setAll(selectedPlaylist.getTracks());

            // Messaggio di successo
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Traccia rimossa correttamente dalla playlist.");

        } catch (IllegalArgumentException e) {

            // Se qualcosa non va, ad esempio nessuna playlist o traccia selezionata,
            // mostra il messaggio di errore in rosso
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    /*
     * Metodo collegato al pulsante "Annulla" della schermata managePlaylist.fxml.
     * Azzera la selezione della playlist, svuota la lista delle tracce
     * e cancella eventuali messaggi.
     */
    @FXML
    private void handleCancelRemove() {

        // Deseleziona la playlist scelta
        if (playlistComboBox != null) {
            playlistComboBox.setValue(null);
        }

        // Svuota la ListView delle tracce
        if (playlistTracksListView != null) {
            playlistTracksListView.getItems().clear();
        }

        // Cancella eventuali messaggi di errore o conferma
        if (messageLabel != null) {
            messageLabel.setText("");
        }


    }

    @FXML
    void handleAggiungiTraccia(ActionEvent event) {

        Playlist selectedPlaylist = playlistComboBox.getValue();

        if (selectedPlaylist == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Seleziona una playlist.");
            return;
        }

        try {
            // 1. leggi dati
            String title = trackTitleField.getText();
            String artist = trackArtistField.getText();
            String album = trackAlbumField.getText();
            String genre = trackGenreField.getText();
            int year = Integer.parseInt(trackYearField.getText());
            int duration = Integer.parseInt(trackDurationField.getText());

            // 2. crea track (QUESTO RESTA IDENTICO)
            Track newTrack = new Track(null, title, artist, album, genre, year, true, true, duration);

            // 3. Aggiungi alla playlist tramite il Command Pattern
            // Invece di chiamare direttamente la playlist o il manager, creiamo l'azione:
            Command addTrackCmd = new AddTrackToPlaylistCommand(selectedPlaylist, newTrack);

            // Chiediamo al gestore di eseguirla e salvarla in cronologia!
            CommandManager.getInstance().executeCommand(addTrackCmd);

            // 4. AGGIORNAMENTO GRAFICO (Fondamentale)
            // Ricordati di ricaricare la lista visiva per far apparire subito la nuova canzone
            // (e per farla riapparire correttamente in caso di futuri "Redo")
            playlistTracksListView.getItems().setAll(selectedPlaylist.getTracks());

            // 4. aggiorna ListView
            playlistTracksListView.getItems().setAll(selectedPlaylist.getTracks());

            // 6. messaggio
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Traccia aggiunta alla playlist.");

            // 7. pulizia campi
            trackTitleField.clear();
            trackArtistField.clear();
            trackAlbumField.clear();
            trackGenreField.clear();
            trackYearField.clear();
            trackDurationField.clear();

        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Anno e durata devono essere numeri.");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Errore: " + e.getMessage());
        }
    }

    @FXML
    private TextField renamePlaylistField;

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
}