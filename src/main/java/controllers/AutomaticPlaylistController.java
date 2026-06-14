package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Library;
import models.PlaylistManager;
import models.Track;

import java.util.List;

public class AutomaticPlaylistController {

    @FXML
    private ComboBox<String> criteriaComboBox;

    @FXML
    private ComboBox<String> valueComboBox;

    @FXML
    private Label messageLabel;

    private boolean created = false;

    @FXML
    public void initialize() {
        criteriaComboBox.getItems().addAll("Genere", "Anno", "Tag");
        criteriaComboBox.setValue("Genere");

        criteriaComboBox.setOnAction(event -> updateValueComboBox());

        updateValueComboBox();
    }

    private void updateValueComboBox() {
        valueComboBox.getItems().clear();
        valueComboBox.setValue(null);

        String criteria = criteriaComboBox.getValue();

        if ("Genere".equals(criteria)) {
            List<String> genres = Library.getInstance().getTracks()
                    .stream()
                    .map(Track::getGenre)
                    .filter(genre -> genre != null && !genre.isBlank())
                    .distinct()
                    .toList();

            valueComboBox.getItems().addAll(genres);
            valueComboBox.setPromptText("Seleziona genere");

        } else if ("Anno".equals(criteria)) {
            List<String> years = Library.getInstance().getTracks()
                    .stream()
                    .map(track -> String.valueOf(track.getYear()))
                    .distinct()
                    .sorted()
                    .toList();

            valueComboBox.getItems().addAll(years);
            valueComboBox.setPromptText("Seleziona anno");

        } else if ("Tag".equals(criteria)) {
            valueComboBox.getItems().addAll("Preferiti", "Explicit");
            valueComboBox.setPromptText("Seleziona tag");
        }

        messageLabel.setText("");
    }

    public boolean isCreated() {
        return created;
    }

    @FXML
    private void handleGenerate() {
        String criteria = criteriaComboBox.getValue();
        String value = valueComboBox.getValue();

        if (criteria == null) {
            showError("Seleziona un criterio di generazione.");
            return;
        }

        if (value == null || value.trim().isEmpty()) {
            showError("Seleziona un valore valido.");
            return;
        }

        try {
            if (criteria.equals("Genere")) {
                PlaylistManager.getInstance()
                        .createAutomaticPlaylistByGenre(value);

            } else if (criteria.equals("Anno")) {
                int year = Integer.parseInt(value);

                PlaylistManager.getInstance()
                        .createAutomaticPlaylistByYear(year);

            } else if (criteria.equals("Tag")) {
                PlaylistManager.getInstance()
                        .createAutomaticPlaylistByTag(value);
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
        Stage stage = (Stage) valueComboBox.getScene().getWindow();
        stage.close();
    }
}