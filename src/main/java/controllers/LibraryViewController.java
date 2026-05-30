package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Track;
import services.LibraryService;

import java.util.List;

public class LibraryViewController {

    private final LibraryService service = new LibraryService();
    public Label titleBar;
    public HBox mainBar;

    @FXML private TableView<Track> trackList;
    @FXML private TableColumn<Track, String> titleColumn;
    @FXML private TableColumn<Track, String> authorColumn;
    @FXML private TableColumn<Track, String> albumColumn;
    @FXML private TableColumn<Track, String> genreColumn;
    @FXML private TableColumn<Track, Integer> lengthColumn;

    @FXML private TextField researchBar;
    @FXML private Button addButton;

    @FXML
    public void initialize() {
        // Colleghiamo le colonne ai nomi delle variabili in models.Track
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        // Deleghiamo l'azione del bottone al metodo segnaposto
        if (addButton != null) {
            addButton.setOnAction(event -> onAddTrack());
        }
    }

    // Task 1.2.1: Aggiunta metodo loadTracks() (o onLoadLibrary)
    @FXML
    public void onLoadLibrary() {
        List<Track> tracks = service.getTracks();
        trackList.getItems().setAll(tracks);
    }

    @FXML
    public void onAddTrack() {
        // METODO SEGNAPOSTO: Chi svilupperà l'AddTrackController scriverà qui la logica.
        // Per ora, tu mostri solo che il bottone è cablato e funzionante!
        System.out.println("Il bottone funziona! In attesa che il team implementi la schermata AddTrack...");

        /* * Esempio di Alert visivo (Opzionale, ma fa molta scena nelle presentazioni)
         * Alert alert = new Alert(Alert.AlertType.INFORMATION);
         * alert.setTitle("Lavori in corso");
         * alert.setHeaderText("Funzionalità in sviluppo");
         * alert.setContentText("Questa funzione sarà implementata a breve dal team.");
         * alert.showAndWait();
         */
    }

    @FXML
    public void onRemoveTrack() {
        Track selected = trackList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.removeTrack(selected);
            trackList.getItems().setAll(service.getTracks());
        }
    }
}