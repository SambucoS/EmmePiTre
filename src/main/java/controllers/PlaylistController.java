package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Playlist;
import commands.AddPlaylistCommand;
import commands.Command;
import commands.CommandManager;

/**
 * Controller della schermata createPlaylist.fxml.
 * Gestisce la creazione di una nuova playlist tramite il Command Pattern.
 */
public class PlaylistController {

    // Campo di testo usato per inserire il nome della nuova playlist
    @FXML
    private TextField playlistNameField;

    // Label usata per mostrare messaggi di conferma o errore
    @FXML
    private Label messageLabel;

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
}
