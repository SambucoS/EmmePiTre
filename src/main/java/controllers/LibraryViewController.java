package controllers;

import interfaces.LibraryObserver;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Library;
import models.Track;

public class LibraryViewController implements LibraryObserver {

    public Label titleBar;
    public HBox mainBar;

    @FXML private TableView<Track> trackList;
    @FXML private TableColumn<Track, Boolean> favouriteColumn;
    @FXML private TableColumn<Track, String> titleColumn;
    @FXML private TableColumn<Track, Boolean> explicitColumn;
    @FXML private TableColumn<Track, String> authorColumn;
    @FXML private TableColumn<Track, String> albumColumn;
    @FXML private TableColumn<Track, Integer> yearColumn;
    @FXML private TableColumn<Track, String> genreColumn;
    @FXML private TableColumn<Track, Integer> lengthColumn;

    @FXML private TextField researchBar;
    @FXML private Button addButton;

    @FXML
    public void initialize() {
        // 1. Stile della barra di selezione (Grigio elegante, testo nero)
        trackList.setStyle(
                "-fx-selection-bar: #e0e0e0; " +
                        "-fx-selection-bar-text: #000000; " +
                        "-fx-selection-bar-non-focused: #f0f0f0;"
        );

        // 2. Correzione PropertyValueFactory (Tutti uniformati al POJO del tuo compagno)
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        explicitColumn.setCellValueFactory(new PropertyValueFactory<>("explicit"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        // Nota: Questa riga genererà un warning finché il team non aggiungerà 'album' alla classe Track
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // 3. Registrazione del Controller come Observer del Modello (Task 1.2.5)
        Library.getInstance().addObserver(this);

        // 4. Logica Grafica Interattiva: Colonna Preferiti (Stella)
        favouriteColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean isFav, boolean empty) {
                super.updateItem(isFav, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Track currentTrack = getTableRow().getItem();
                    Label starLabel = new Label();
                    starLabel.setCursor(Cursor.HAND);

                    if (currentTrack.isFavourite()) {
                        starLabel.setText("★");
                        starLabel.setStyle("-fx-text-fill: #1DB954; -fx-font-size: 18px;");
                    } else {
                        starLabel.setText("☆");
                        starLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 18px;");
                    }

                    starLabel.setOnMouseClicked(event -> {
                        // Caro Modello, l'utente vuole cambiare il preferito. Pensaci tu!
                        Library.getInstance().toggleFavourite(currentTrack);
                    });

                    setGraphic(starLabel);
                }
            }
        });

        // 5. Logica Grafica Interattiva: Colonna Explicit (Badge "E")
        explicitColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean isExplicit, boolean empty) {
                super.updateItem(isExplicit, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Track currentTrack = getTableRow().getItem();
                    Label explicitBadge = new Label("E");
                    explicitBadge.setCursor(Cursor.HAND);

                    String baseStyle = "-fx-background-color: #888888; -fx-text-fill: white; -fx-padding: 1 5 1 5; -fx-background-radius: 3; -fx-font-size: 10px;";

                    if (currentTrack.isExplicit()) {
                        explicitBadge.setStyle(baseStyle + "-fx-font-weight: bold; -fx-opacity: 1.0;");
                    } else {
                        explicitBadge.setStyle(baseStyle + "-fx-font-weight: normal; -fx-opacity: 0.4;");
                    }

                    explicitBadge.setOnMouseClicked(event -> {
                        Library.getInstance().toggleExplicit(currentTrack);
                    });

                    setGraphic(explicitBadge);
                }
            }
        });

        // 6. Formattazione personalizzata della Durata (da secondi a mm:ss)
        lengthColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer totalSeconds, boolean empty) {
                super.updateItem(totalSeconds, empty);
                if (empty || totalSeconds == null) {
                    setText(null);
                } else {
                    int minutes = totalSeconds / 60;
                    int seconds = totalSeconds % 60;
                    setText(String.format("%02d:%02d", minutes, seconds));
                }
            }
        });

        // Caricamento iniziale dei dati finti per il test
        onLoadLibrary();
    }

    // =========================================================
    // AGGIORNAMENTO AUTOMATICO OBSERVER (Task 1.2.5)
    // =========================================================
    @Override
    public void onLibraryChanged() {
        System.out.println("Notifica ricevuta dall'Observer: sto aggiornando la TableView!");
        trackList.getItems().setAll(Library.getInstance().getTracks());
        trackList.refresh();
    }

    @FXML
    public void onLoadLibrary() {
        // Popoliamo il Singleton globale solo se è attualmente vuoto (evita duplicazioni al refresh)
        if (Library.getInstance().getTracks().isEmpty()) {
            Library.getInstance().addTrack(new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Pop", 2023, true, true, 215));
            Library.getInstance().addTrack(new Track("C:/music/song2.mp3", "Brano Pulito", "Artista Indie", "Lo-fi", 2024, false, false, 180));
            Library.getInstance().addTrack(new Track("C:/music/song3.mp3", "Classico Greve", "Rapper Serio", "Rap", 1999, true, false, 240));
        } else {
            trackList.getItems().setAll(Library.getInstance().getTracks());
        }
    }

    @FXML
    public void onAddTrack() {
        // Cliccando sul bottone "Add a Track", simuliamo l'aggiunta di un brano nel Modello globale.
        // Grazie all'Observer Pattern, vedrai la riga aggiungersi da sola nella tabella!
        int nextId = Library.getInstance().getTracks().size() + 1;
        Track tracciaSimulata = new Track("path/test.mp3", "Nuova Hit " + nextId, "Mainstream Artist", "Dance", 2026, false, true, 195);

        Library.getInstance().addTrack(tracciaSimulata);
    }

    @FXML
    public void onRemoveTrack() {
        Track selected = trackList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Chiamiamo il metodo di rimozione della Library, l'interfaccia si adeguerà da sola via notifica
            Library.getInstance().removeTrack(selected);
        }
    }
}