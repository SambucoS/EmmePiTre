package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Playlist;
import models.PlaylistManager;

/**
 * Controller JavaFX della modale di conferma eliminazione playlist.
 *
 * Questa classe gestisce la finestra che chiede all'utente conferma
 * prima di eliminare definitivamente una playlist.
 */
public class RemovePlaylistController {

    /**
     * Label usata per mostrare il nome della playlist che l'utente sta per eliminare.
     */
    @FXML
    private Label playlistNameLabel;

    /**
     * Playlist selezionata per l'eliminazione.
     */
    private Playlist playlistToRemove;

    /**
     * Imposta la playlist da eliminare e aggiorna la label della modale
     * mostrando il nome della playlist selezionata.
     *
     * @param playlist playlist che l'utente ha scelto di eliminare.
     * @return nessun valore di ritorno.
     * @throws IllegalArgumentException non viene lanciata direttamente da questo metodo.
     */
    public void setPlaylistToRemove(Playlist playlist) {
        this.playlistToRemove = playlist;

        // Se la label è stata caricata correttamente e la playlist esiste,
        // viene mostrato il nome della playlist nella modale.
        if (playlistNameLabel != null && playlist != null) {
            playlistNameLabel.setText("Playlist: " + playlist.getName());
        }
    }

    /**
     * Gestisce il click sul pulsante "Elimina" della modale.
     *
     * Se è presente una playlist selezionata, chiede al PlaylistManager
     * di eliminarla dalla collezione delle playlist e poi chiude la finestra.
     *
     * @param nessun parametro in ingresso.
     * @return nessun valore di ritorno.
     * @throws IllegalArgumentException può essere lanciata da PlaylistManager.deletePlaylist(...)
     *                                  se la playlist da eliminare non è valida.
     */
    @FXML
    private void handleConfirmDelete() {
        if (playlistToRemove != null) {
            // Elimina definitivamente la playlist selezionata.
            // Le tracce contenute nella playlist non vengono eliminate dalla libreria principale.
            PlaylistManager.getInstance().deletePlaylist(playlistToRemove);
        }

        // Dopo la conferma, la modale viene chiusa.
        closeWindow();
    }

    /**
     * Gestisce il click sul pulsante "Annulla" della modale.
     *
     * L'operazione di eliminazione viene annullata e la finestra viene chiusa
     * senza modificare lo stato delle playlist.
     *
     * @param nessun parametro in ingresso.
     * @return nessun valore di ritorno.
     * @throws nessuna eccezione prevista.
     */
    @FXML
    private void handleCancel() {
        // Chiude la modale senza eliminare la playlist.
        closeWindow();
    }

    /**
     * Chiude la finestra corrente della modale.
     *
     * Recupera lo Stage partendo dalla label presente nella scena e chiude
     * la finestra associata.
     *
     * @param nessun parametro in ingresso.
     * @return nessun valore di ritorno.
     * @throws NullPointerException può verificarsi se playlistNameLabel o la scena associata sono null.
     */
    private void closeWindow() {
        // Recupera la finestra corrente a partire da un elemento grafico della scena.
        Stage stage = (Stage) playlistNameLabel.getScene().getWindow();

        // Chiude la modale.
        stage.close();
    }
}