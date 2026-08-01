package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller della modale di conferma usata sia per l'eliminazione
 * definitiva di una traccia dalla libreria sia per la sua rimozione da una
 * singola playlist. Il messaggio mostrato cambia in base al contesto
 * impostato dal controller chiamante tramite {@link #setContext(boolean)}.
 *
 * @version 1.0
 */
public class DeleteTrackController {

    @FXML private Label messageLabel;
    @FXML private Button btnCancel;
    @FXML private Button btnConfirm;

    private boolean confirmed = false;

    /**
     * Imposta il contesto della richiesta, scegliendo il messaggio di
     * conferma appropriato.
     *
     * @param isFromLibrary {@code true} se la richiesta riguarda l'eliminazione
     *                      definitiva dalla libreria (a cascata su tutte le
     *                      playlist), {@code false} se riguarda la rimozione
     *                      da una singola playlist
     */
    public void setContext(boolean isFromLibrary) {
        if (isFromLibrary) {
            messageLabel.setText("Sei sicuro di voler eliminare definitivamente questa traccia?\nVerrà rimossa a cascata da tutte le playlist.");
        } else {
            messageLabel.setText("Sei sicuro di voler rimuovere questa traccia dalla playlist?");
        }
    }

    /**
     * Indica se l'utente ha confermato l'operazione.
     *
     * @return {@code true} se e' stato premuto "Conferma", {@code false} altrimenti
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Gestisce la conferma dell'operazione e chiude la finestra.
     *
     * @param event l'evento generato dalla pressione del pulsante di conferma
     */
    @FXML
    void handleConfirm(ActionEvent event) {
        this.confirmed = true;
        closeStage(btnConfirm);
    }

    /**
     * Gestisce l'annullamento dell'operazione e chiude la finestra.
     *
     * @param event l'evento generato dalla pressione del pulsante "Annulla"
     */
    @FXML
    void handleCancel(ActionEvent event) {
        this.confirmed = false;
        closeStage(btnCancel);
    }

    /**
     * Recupera lo stage a partire dal bottone premuto e lo chiude.
     *
     * @param button il bottone la cui scena appartiene allo stage da chiudere
     */
    private void closeStage(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
