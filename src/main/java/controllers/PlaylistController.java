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
     * Metodo di supporto per aggiornare la ListView delle tracce.
     * Riceve una playlist e mostra le tracce contenute in quella playlist.
     */
    private void updateTracksList(Playlist selectedPlaylist) {
        if (playlistTracksListView != null) {
            playlistTracksListView.getItems().clear();

            if (selectedPlaylist != null) {
                playlistTracksListView.getItems().setAll(
                        selectedPlaylist.getTracks()
                );
            }
        }
    }

    /*
     * Metodo collegato al pulsante "Crea" della schermata createPlaylist.fxml.
     * Legge il nome inserito dall'utente e chiede al PlaylistManager
     * di creare una nuova playlist.
     */
    @FXML
    private void handleCreatePlaylist() {
        String playlistName = playlistNameField.getText();

        try {
            PlaylistManager.getInstance().createPlaylist(playlistName);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Playlist creata correttamente.");
            playlistNameField.clear();

        } catch (IllegalArgumentException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
        }
    }

    /*
     * Metodo collegato al pulsante "Annulla" della schermata createPlaylist.fxml.
     * Chiude la modale di creazione playlist.
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        if (playlistNameField != null) {
            playlistNameField.clear();
        }

        if (messageLabel != null) {
            messageLabel.setText("");
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /*
     * Metodo collegato al pulsante "Rimuovi traccia" della schermata managePlaylist.fxml.
     * Rimuove la traccia solo dalla playlist selezionata, non dalla libreria principale.
     */
    @FXML
    private void handleRemoveTrackFromPlaylist() {
        Playlist selectedPlaylist = null;

        if (playlistComboBox != null) {
            selectedPlaylist = playlistComboBox.getValue();
        }

        Track selectedTrack = null;

        if (playlistTracksListView != null) {
            selectedTrack = playlistTracksListView.getSelectionModel().getSelectedItem();
        }

        try {
            PlaylistManager.getInstance().removeTrackFromPlaylist(selectedTrack, selectedPlaylist);

            if (playlistTracksListView != null) {
                playlistTracksListView.getItems().remove(selectedTrack);
            }

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Traccia rimossa correttamente dalla playlist.");

        } catch (IllegalArgumentException e) {
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
        if (playlistComboBox != null) {
            playlistComboBox.setValue(null);
        }

        if (playlistTracksListView != null) {
            playlistTracksListView.getItems().clear();
        }

        if (messageLabel != null) {
            messageLabel.setText("");
        }
    }
}