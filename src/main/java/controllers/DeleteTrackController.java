package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteTrackController {

    @FXML private Label messageLabel;
    @FXML private Button btnCancel;
    @FXML private Button btnConfirm;

    private boolean confirmed = false;

    // Riceve il contesto dal controller chiamante
    public void setContext(boolean isFromLibrary) {
        if (isFromLibrary) {
            messageLabel.setText("Sei sicuro di voler eliminare definitivamente questa traccia?\nVerrà rimossa a cascata da tutte le playlist.");
        } else {
            messageLabel.setText("Sei sicuro di voler rimuovere questa traccia dalla playlist?");
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    void handleConfirm(ActionEvent event) {
        this.confirmed = true;
        closeStage(btnConfirm);
    }

    @FXML
    void handleCancel(ActionEvent event) {
        this.confirmed = false;
        closeStage(btnCancel);
    }

    private void closeStage(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}