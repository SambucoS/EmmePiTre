package controllers;

// Import necessario per collegare gli elementi dell'FXML al controller
import javafx.fxml.FXML;

// Import necessario perché il controller usa il metodo initialize()
import javafx.fxml.Initializable;

// Componenti grafici JavaFX usati negli FXML
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

// Classi del modello usate dal controller
import models.Playlist;
import models.PlaylistManager;
import models.Track;

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

    // ComboBox usata nella schermata removeFromPlaylist.fxml
    // Serve per selezionare la playlist da cui rimuovere una traccia
    @FXML
    private ComboBox<Playlist> playlistComboBox;

    // ListView usata nella schermata removeFromPlaylist.fxml
    // Mostra le tracce contenute nella playlist selezionata
    @FXML
    private ListView<Track> playlistTracksListView;

    /*
     * Metodo chiamato automaticamente da JavaFX quando viene caricato l'FXML.
     *
     * Poiché questo controller viene usato da più schermate FXML,
     * alcuni elementi potrebbero essere null.
     *
     * Ad esempio:
     * - in createPlaylist.fxml non esistono playlistComboBox e playlistTracksListView
     * - in removeFromPlaylist.fxml non esiste playlistNameField
     *
     * Per questo motivo controlliamo che playlistComboBox non sia null
     * prima di inizializzare la parte relativa alla rimozione da playlist.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Se la ComboBox esiste, significa che stiamo caricando la schermata removeFromPlaylist.fxml
        if (playlistComboBox != null) {

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

                    // Se l'utente ha selezionato una playlist valida,
                    // mostra nella ListView tutte le sue tracce
                    if (selectedPlaylist != null) {
                        playlistTracksListView.getItems().setAll(
                                selectedPlaylist.getTracks()
                        );
                    }
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
            PlaylistManager.getInstance().createPlaylist(playlistName);

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
    private void handleCancel() {

        // Controllo utile perché playlistNameField esiste solo nella schermata di creazione
        if (playlistNameField != null) {
            playlistNameField.clear();
        }

        // Cancella il messaggio mostrato all'utente
        if (messageLabel != null) {
            messageLabel.setText("");
        }
    }

    /*
     * Metodo collegato al pulsante "Rimuovi" della schermata removeFromPlaylist.fxml.
     * Rimuove dalla playlist selezionata la traccia scelta nella ListView.
     */
    @FXML
    private void handleRemoveTrackFromPlaylist() {

        // Recupera la playlist selezionata nella ComboBox
        Playlist selectedPlaylist = playlistComboBox.getValue();

        // Recupera la traccia selezionata nella ListView
        Track selectedTrack = playlistTracksListView.getSelectionModel().getSelectedItem();

        try {
            /*
             * Chiede al PlaylistManager di rimuovere la traccia dalla playlist.
             * Questa operazione NON elimina la traccia dalla libreria principale,
             * ma solo dalla playlist selezionata.
             */
            PlaylistManager.getInstance().removeTrackFromPlaylist(selectedTrack, selectedPlaylist);

            // Aggiorna anche la ListView rimuovendo visivamente la traccia
            playlistTracksListView.getItems().remove(selectedTrack);

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
     * Metodo collegato al pulsante "Annulla" della schermata removeFromPlaylist.fxml.
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
}