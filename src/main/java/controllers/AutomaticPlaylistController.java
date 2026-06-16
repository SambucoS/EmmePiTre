package controllers;

import criteria.AndCriteria;
import criteria.ExplicitCriteria;
import criteria.FavouriteCriteria;
import criteria.GenreCriteria;
import criteria.NewReleaseCriteria;
import criteria.YearCriteria;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Library;
import models.PlaylistManager;
import models.Track;

import java.util.function.Function;

/**
 * Controller della modale di creazione automatica di una playlist.
 *
 * I criteri scelti dall'utente (genere, anno, preferiti, explicit, new release)
 * vengono combinati in AND tramite il Composite {@link AndCriteria}, costruito a
 * partire dalle strategie concrete {@link criteria.TrackCriteria}. La selezione
 * vera e propria e' delegata a {@link PlaylistManager#createAutomaticPlaylist}.
 */
public class AutomaticPlaylistController {

    private static final String ANY = "Qualsiasi";

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private CheckBox favouriteCheck;
    @FXML
    private CheckBox explicitCheck;
    @FXML
    private CheckBox newReleaseCheck;
    @FXML
    private Label messageLabel;

    private boolean created = false;

    @FXML
    public void initialize() {
        populateCombo(genreComboBox, Track::getGenre);
        populateCombo(yearComboBox, t -> String.valueOf(t.getYear()));
    }

    /**
     * Popola una ComboBox con i valori distinti e ordinati ricavati dalle tracce
     * della libreria tramite il mapper dato, anteponendo l'opzione "Qualsiasi".
     */
    private void populateCombo(ComboBox<String> combo, Function<Track, String> mapper) {
        combo.getItems().add(ANY);
        Library.getInstance().getTracks().stream()
                .map(mapper)
                .filter(v -> v != null && !v.isBlank())
                .distinct()
                .sorted()
                .forEach(combo.getItems()::add);
        combo.setValue(ANY);
    }

    public boolean isCreated() {
        return created;
    }

    @FXML
    private void handleGenerate() {
        // Composite: combina in AND tutti i criteri selezionati
        AndCriteria criteria = new AndCriteria();

        String genre = genreComboBox.getValue();
        if (genre != null && !ANY.equals(genre)) {
            criteria.add(new GenreCriteria(genre));
        }

        String year = yearComboBox.getValue();
        if (year != null && !ANY.equals(year)) {
            criteria.add(new YearCriteria(Integer.parseInt(year)));
        }

        if (favouriteCheck.isSelected()) {
            criteria.add(new FavouriteCriteria());
        }
        if (explicitCheck.isSelected()) {
            criteria.add(new ExplicitCriteria());
        }
        if (newReleaseCheck.isSelected()) {
            criteria.add(new NewReleaseCriteria());
        }

        if (criteria.isEmpty()) {
            showError("Seleziona almeno un criterio.");
            return;
        }

        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            showError("Inserisci un nome per la playlist.");
            return;
        }

        try {
            PlaylistManager.getInstance().createAutomaticPlaylist(name, criteria);
            created = true;
            closeWindow();
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
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
