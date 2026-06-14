package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.PlaylistManager;

public class AutomaticPlaylistController {

    @FXML
    private ComboBox<String> criteriaComboBox;

    @FXML
    private TextField valueField;

    @FXML
    private ComboBox<String> tagComboBox;

    @FXML
    private Label messageLabel;

    private boolean created = false;

    @FXML
    public void initialize() {
        criteriaComboBox.getItems().addAll("Genere", "Anno", "Tag");
        criteriaComboBox.setValue("Genere");

        tagComboBox.getItems().addAll("Preferiti", "Explicit");

        criteriaComboBox.setOnAction(event -> updateInputMode());

        updateInputMode();
    }

    private void updateInputMode() {
        String criteria = criteriaComboBox.getValue();

        if ("Tag".equals(criteria)) {
            valueField.setVisible(false);
            valueField.setManaged(false);

            tagComboBox.setVisible(true);
            tagComboBox.setManaged(true);

            tagComboBox.setValue("Preferiti");

        } else {
            tagComboBox.setVisible(false);
            tagComboBox.setManaged(false);

            valueField.setVisible(true);
            valueField.setManaged(true);

            if ("Genere".equals(criteria)) {
                valueField.setPromptText("Inserisci genere");
            } else if ("Anno".equals(criteria)) {
                valueField.setPromptText("Inserisci anno");
            }
        }

        messageLabel.setText("");
    }

    public boolean isCreated() {
        return created;
    }

    @FXML
    private void handleGenerate() {
        String criteria = criteriaComboBox.getValue();

        if (criteria == null) {
            showError("Seleziona un criterio di generazione.");
            return;
        }

        try {
            if (criteria.equals("Genere")) {
                String value = valueField.getText();

                if (value == null || value.trim().isEmpty()) {
                    showError("Inserisci un genere valido.");
                    return;
                }

                PlaylistManager.getInstance()
                        .createAutomaticPlaylistByGenre(value);

            } else if (criteria.equals("Anno")) {
                String value = valueField.getText();

                if (value == null || value.trim().isEmpty()) {
                    showError("Inserisci un anno valido.");
                    return;
                }

                int year = Integer.parseInt(value.trim());

                PlaylistManager.getInstance()
                        .createAutomaticPlaylistByYear(year);

            } else if (criteria.equals("Tag")) {
                String selectedTag = tagComboBox.getValue();

                if (selectedTag == null || selectedTag.trim().isEmpty()) {
                    showError("Seleziona un tag valido.");
                    return;
                }

                PlaylistManager.getInstance()
                        .createAutomaticPlaylistByTag(selectedTag);
            }

            created = true;
            closeWindow();

        } catch (NumberFormatException e) {
            showError("L'anno deve essere un numero intero.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        created = false;
        closeWindow();
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void closeWindow() {
        Stage stage = (Stage) valueField.getScene().getWindow();
        stage.close();
    }
}