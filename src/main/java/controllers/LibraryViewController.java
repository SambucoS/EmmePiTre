package controllers;

import interfaces.LibraryObserver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Library;
import models.Track;
import models.commands.Command;
import models.commands.CommandManager;
import models.commands.RemoveTrackCommand;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class LibraryViewController implements LibraryObserver {

    public Label titleBar;
    public HBox mainBar;
    private Parent currentPlayerView;

    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Track> trackList;
    @FXML
    private TableColumn<Track, Boolean> favouriteColumn;
    @FXML
    private TableColumn<Track, String> titleColumn;
    @FXML
    private TableColumn<Track, Boolean> explicitColumn;
    @FXML
    private TableColumn<Track, String> authorColumn;
    @FXML
    private TableColumn<Track, String> albumColumn;
    @FXML
    private TableColumn<Track, Integer> yearColumn;
    @FXML
    private TableColumn<Track, String> genreColumn;
    @FXML
    private TableColumn<Track, Integer> lengthColumn;
    @FXML
    private TableColumn<Track, Void> actionsColumn;
    @FXML
    private TextField researchBar;
    @FXML
    private Button addButton;
    @FXML private Button undoButton;
    @FXML private Button redoButton;

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
        // =========================================================
        // MENU A TENDINA (Modifica/Elimina) - Task 1.4
        // =========================================================

        // Configurazione del Click Destro sulla riga intera (Metodo Nativo JavaFX)
        trackList.setRowFactory(tv -> {
            TableRow<Track> row = new TableRow<>();

            // Creiamo un UNICO menu per questa riga
            ContextMenu rowMenu = createActionMenu(row);

            // Invece di gestire a mano il click destro, usiamo la proprietà nativa.
            // Il listener assicura che il menu esista solo se la riga contiene effettivamente una canzone.
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem == null) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(rowMenu);
                }
            });
            row.setOnMouseClicked(event -> {
                // Controllo fondamentale: !row.isEmpty() evita crash se fai doppio click su una riga vuota
                if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    Track selectedTrack = row.getItem();
                    openPlayerView(selectedTrack);
                }
            });

            return row;
        });

        // Configurazione Click Sinistro sui tre puntini
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Label dotsLabel = new Label("⋮");
            {
                dotsLabel.setCursor(Cursor.HAND);
                dotsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #888888; -fx-padding: 0 5 0 5;");

                dotsLabel.setOnMouseClicked(event -> {
                    if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        event.consume(); // Blocca il click per non farlo "rimbalzare" sulla riga sotto

                        TableRow<Track> row = getTableRow();
                        if (row != null && !row.isEmpty() && row.getContextMenu() != null) {
                            ContextMenu menu = row.getContextMenu(); // Recuperiamo il menu nativo della riga

                            // Se il menu è aperto, chiudilo (effetto toggle), altrimenti mostralo
                            if (menu.isShowing()) {
                                menu.hide();
                            } else {
                                menu.show(dotsLabel, javafx.geometry.Side.BOTTOM, 0, 0);
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(dotsLabel);
                }
            }
        });
        // =========================================================
        // SCORCIATOIE DA TASTIERA (Ctrl+Z e Ctrl+Y)
        // =========================================================
        Platform.runLater(() -> {
            Scene scene = trackList.getScene(); // Recuperiamo la scena usando un elemento della GUI

            if (scene != null) {
                // Scorciatoia per UNDO: Ctrl + Z
                // Nota: Usiamo SHORTCUT_DOWN invece di CONTROL_DOWN. È una best practice di JavaFX
                // perché si adatta automaticamente: usa 'Ctrl' su Windows/Linux e 'Cmd' su Mac.
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN),
                        this::onUndo
                );

                // Scorciatoia per REDO: Ctrl + Y
                scene.getAccelerators().put(
                        new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN),
                        this::onRedo
                );
            }
        });
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
            Library.getInstance().addTrack(new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album", "Pop", 2023, true, true, 215));
            Library.getInstance().addTrack(new Track("C:/music/song2.mp3", "Brano Pulito", "Artista Indie", "Album", "Lo-fi", 2024, false, false, 180));
            Library.getInstance().addTrack(new Track("C:/music/song3.mp3", "Classico Greve", "Rapper Serio", "Album", "Rap", 1999, true, false, 240));
        } else {
            trackList.getItems().setAll(Library.getInstance().getTracks());
        }
    }

    @FXML
    public void onAddTrack() throws IOException {
        // Cliccando sul bottone "Add a Track", simuliamo l'aggiunta di un brano nel Modello globale.
        // Grazie all'Observer Pattern, vedrai la riga aggiungersi da sola nella tabella!
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addTrackModal.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // B. Recuperiamo il controller e impostiamo il contesto (true = da Libreria)
        AddTrackController dialogController = loader.getController();

        // C. Prepariamo la finestra (Stage)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Form Aggiunta");
        dialogStage.initModality(Modality.APPLICATION_MODAL); // Rende la finestra bloccante
        dialogStage.setResizable(false); // Blocca il ridimensionamento

        // D. Impostiamo la scena con le dimensioni fisse 400x120
        Scene scene = new Scene(root, 600, 450);
        dialogStage.setScene(scene);

        // E. Mostriamo il modale e mettiamo "in pausa" questo codice finché non viene chiuso
        dialogStage.showAndWait();
        updateUndoRedoButtons();
    }

    private ContextMenu createActionMenu(TableRow<Track> row) {
        ContextMenu contextMenu = new ContextMenu();

        // Applichiamo lo stile direttamente al contenitore del menu.
        // -fx-selection-bar imposta il colore grigio quando ci passi sopra col mouse.
        // -fx-selection-bar-text mantiene il testo nero quando è evidenziato.
        contextMenu.setStyle(
                "-fx-selection-bar: #d0d0d0; " +
                        "-fx-selection-bar-text: #000000; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: normal;"
        );

        MenuItem editItem = new MenuItem("Modifica traccia");
        MenuItem deleteItem = new MenuItem("Elimina traccia");

        // 1. Logica per la MODIFICA (Placeholder)
        editItem.setOnAction(event -> {
            Track currentTrack = row.getItem();
            modifyTrack(currentTrack);
            });

        // 2. Logica per l'ELIMINAZIONE (Apertura Modale)
        deleteItem.setOnAction(event -> {
            Track currentTrack = row.getItem();

            // Evidenzia visivamente la riga che stiamo per eliminare
            trackList.getSelectionModel().select(currentTrack);

            try {
                // A. Carichiamo il file FXML del modale
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/deleteTrack.fxml"));
                Parent root = loader.load();

                // B. Recuperiamo il controller e impostiamo il contesto (true = da Libreria)
                DeleteTrackController dialogController = loader.getController();
                dialogController.setContext(true);

                // C. Prepariamo la finestra (Stage)
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Conferma Eliminazione");
                dialogStage.initModality(Modality.APPLICATION_MODAL); // Rende la finestra bloccante
                dialogStage.setResizable(false); // Blocca il ridimensionamento

                // D. Impostiamo la scena con le dimensioni fisse 400x120
                Scene scene = new Scene(root, 600, 150);
                dialogStage.setScene(scene);

                // E. Mostriamo il modale e mettiamo "in pausa" questo codice finché non viene chiuso
                dialogStage.showAndWait();

                // F. Controlliamo cosa ha scelto l'utente
                if (dialogController.isConfirmed()) {
                    // Se ha cliccato "Sono sicuro", eliminiamo la traccia dal modello centrale!
                    // Grazie all'Observer Pattern, la riga sparirà automaticamente dalla TableView
                    Command removeCmd = new RemoveTrackCommand(Library.getInstance(), currentTrack);
                    CommandManager.getInstance().executeCommand(removeCmd);
                    updateUndoRedoButtons();
                    System.out.println("Traccia eliminata definitivamente: " + currentTrack.getName());
                } else {
                    System.out.println("Eliminazione annullata.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Errore durante il caricamento del modale deleteTrack.fxml");
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }

    @FXML
    public void onCreatePlaylist() {
        try {
            // A. Carica il file FXML del modale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/createPlaylist.fxml"));
            Parent root = loader.load();

            // B. Prepara la finestra (Stage)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nuova Playlist");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Blocca la finestra principale
            dialogStage.setResizable(false);

            // C. Imposta le dimensioni (regolale se il tuo modale è più grande/piccolo)
            Scene scene = new Scene(root, 400, 200);
            dialogStage.setScene(scene);

            // D. Mostra il modale e attendi la chiusura
            dialogStage.showAndWait();

            // ========================================================
            // IL MODALE SI È CHIUSO: AGGIORNIAMO I TASTI UNDO/REDO
            // ========================================================
            updateUndoRedoButtons();

            // Se hai un menù laterale o una lista visiva delle playlist,
            // questo è il punto esatto in cui dovresti ricaricarla!
            // Esempio: sideBarPlaylists.getItems().setAll(models.PlaylistManager.getInstance().getPlaylists());

            // E. Verifica per il debug: stampa su console le playlist attualmente esistenti
            System.out.println("--- STATO ATTUALE PLAYLIST ---");
            models.PlaylistManager.getInstance().getPlaylists().forEach(p ->
                    System.out.println("- " + p.getName())
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il caricamento di createPlaylist.fxml");
        }
    }


    /**
     * Carica dinamicamente la vista del Player musicale (playerView.fxml),
     * assegna la traccia selezionata al suo controller e inserisce la vista
     * all'interno del contenitore principale (rootPane), sostituendo quella precedente.
     *
     * @param track la traccia musicale da passare al player per la riproduzione
     * @throws IOException qualora il file non esistesse/percorso errato
     */
    private void openPlayerView(Track track) {
        try {
            // Creazione del caricatore FXML, specificando il path della view del Player
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/playerView.fxml"));


            // Caricamento del contenuto del file fxml, l'albero dei nodi
            Parent playerView = loader.load();

            // Caricamento del controller associato alla view del Player (PlayerController).
            PlayerController playerController = loader.getController();

            // Passaggio dell'oggetto 'Track' selezionato E dell'intera lista in tabella al controller del player
            playerController.setTrack(track, trackList.getItems());

            // Per rimuovere eventuali istanze di player aperti in precedenza, viene pulito il contenitore principale
            // per evitare sovrapposizioni
            rootPane.getChildren().clear();

            // Inject della view del player nel contenitore principale
            rootPane.getChildren().add(playerView);

            //currentPlayerView = playerView;

        } catch (IOException e) {

            e.printStackTrace();

            // Usando l'Alert viene mostrato un messaggio di errore a schermo per avvisare l'utente
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Impossibile aprire il Player Musicale",
                    ButtonType.OK);

            alert.showAndWait(); // Serve a bloccare l'interfaccia fintanto che l'utente clicca sul pulsante di conferma
        }
    }

    @FXML
    public void modifyTrack(Track track) {
        Track selected = track;

        if (selected == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modify.fxml"));
            Parent root = loader.load();

            ModifyController controller = loader.getController();
            controller.setTrack(selected);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modify Track");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // =========================================================
    // GESTIONE UNDO / REDO GRAFICO
    // =========================================================

    @FXML
    public void onUndo() {
        // 1. Chiediamo al manager di annullare l'ultima azione
        models.commands.CommandManager.getInstance().undo();

        // 2. Aggiorniamo i colori/stati dei bottoncini
        updateUndoRedoButtons();

        // 3. Ricarichiamo la grafica della tabella per far apparire/sparire i dati
        trackList.getItems().setAll(Library.getInstance().getTracks());
    }

    @FXML
    public void onRedo() {
        // 1. Chiediamo al manager di ripristinare l'azione annullata
        models.commands.CommandManager.getInstance().redo();

        // 2. Aggiorniamo i bottoni
        updateUndoRedoButtons();

        // 3. Ricarichiamo la grafica
        trackList.getItems().setAll(Library.getInstance().getTracks());
    }

    /**
     * Controlla lo stato degli stack nel CommandManager e accende/spegne i bottoni
     */
    public void updateUndoRedoButtons() {
        // Se non ci sono azioni da annullare (canUndo = false), il bottone è disabilitato (true)
        undoButton.setDisable(!models.commands.CommandManager.getInstance().canUndo());

        // Se non ci sono azioni da ripristinare (canRedo = false), il bottone è disabilitato (true)
        redoButton.setDisable(!models.commands.CommandManager.getInstance().canRedo());
    }
}