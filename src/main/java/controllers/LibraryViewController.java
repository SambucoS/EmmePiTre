package controllers;

import observer.LibraryObserver;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Library;
import models.Playlist;
import models.PlaylistManager;
import models.Track;
import commands.Command;
import commands.CommandManager;
import commands.RemoveTrackFromLibraryCommand;
import commands.RemoveTrackFromPlaylistCommand;
import controllers.cells.ActionsCell;
import controllers.cells.DurationCell;
import controllers.cells.ExplicitCell;
import controllers.cells.FavouriteCell;
import controllers.cells.PlaylistSidebarCell;
import util.DialogLoader;

import java.io.IOException;
import java.util.*;

public class LibraryViewController implements LibraryObserver {

    public Label titleBar;
    public HBox mainBar;

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
    private ListView<Playlist> sidebarPlaylistListView;

    private Playlist currentPlaylist = null;
    private boolean suppressSidebarListener = false;
    private final ObjectProperty<Track> currentPlayingTrack = new SimpleObjectProperty<>(null);
    private PlayerController playerController = null;

    private void refreshSidebarPlaylists() {
        if (sidebarPlaylistListView == null) return;

        suppressSidebarListener = true;

        Playlist selected = sidebarPlaylistListView.getSelectionModel().getSelectedItem();

        List<Playlist> ordered = new ArrayList<>(PlaylistManager.getInstance().getPlaylists());

        Playlist mostPlayed = PlaylistManager.getInstance().getMostListenedPlaylist();
        ordered.sort((a, b) -> {
            if (a.getName().equals("🔥 Top 10 Tracks")) return -1;
            if (b.getName().equals("🔥 Top 10 Tracks")) return 1;

            if (mostPlayed != null) {
                if (a.equals(mostPlayed)) return -1;
                if (b.equals(mostPlayed)) return 1;
            }

            return 0;
        });

        sidebarPlaylistListView.getItems().setAll(ordered);

        if (selected != null && ordered.contains(selected)) {
            sidebarPlaylistListView.getSelectionModel().select(selected);
        } else {
            currentPlaylist = null;
            addButton.setText("Add a Track");
        }

        suppressSidebarListener = false;
    }

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

        // 4-5-6. Rendering colonne interattive (stella, badge explicit, durata mm:ss)
        favouriteColumn.setCellFactory(column -> new FavouriteCell());
        explicitColumn.setCellFactory(column -> new ExplicitCell());
        lengthColumn.setCellFactory(column -> new DurationCell());

        // Caricamento iniziale dei dati finti per il test
        onLoadLibrary();
        generateTop10Playlist();
        // =========================================================
        // MENU A TENDINA (Modifica/Elimina) - Task 1.4
        // =========================================================

        // Caricamento iniziale delle playlist nella sidebar
        setupSidebarPlaylistListView();
        Playlist top10 = PlaylistManager.getInstance()
                .getPlaylists()
                .stream()
                .filter(p -> p.getName().equals("🔥 Top 10 Tracks"))
                .findFirst()
                .orElse(null);

        if (top10 != null) {
            currentPlaylist = top10;
            refreshTrackList();
            sidebarPlaylistListView.getSelectionModel().select(top10);
        }

        researchBar.textProperty().addListener((obs, old, newText) -> applySearch(newText));
        currentPlayingTrack.addListener((obs, oldTrack, newTrack) -> {
            ;
            Library.getInstance().getTrackWithID(currentPlayingTrack.get().getId()).setTimesListened();
            Library.getInstance().sync();

        });
        // Configurazione del Click Destro sulla riga intera (Metodo Nativo JavaFX)
        trackList.setRowFactory(tv -> {
            TableRow<Track> row = new TableRow<>();

            // Aggiorna menu contestuale ed evidenziazione: il menu dipende dal contesto corrente
            Runnable updateRow = () -> {
                Track item = row.getItem();
                if (item == null) {
                    row.setContextMenu(null);
                    row.setStyle("");
                } else {
                    row.setContextMenu(currentPlaylist != null
                            ? createPlaylistContextMenu(row)
                            : createLibraryContextMenu(row));
                    boolean isPlaying = Objects.equals(item, currentPlayingTrack.get());
                    row.setStyle(isPlaying
                            ? "-fx-background-color: #eaf7ee; -fx-border-color: transparent;"
                            : "");
                }
            };

            // Ri-valuta quando la riga riceve un item diverso
            row.itemProperty().addListener((obs, oldItem, newItem) -> updateRow.run());

            // Ri-valuta quando cambia la traccia in riproduzione
            currentPlayingTrack.addListener((obs, oldTrack, newTrack) -> updateRow.run());


            row.setOnMouseClicked(event -> {
                if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    Track selectedTrack = row.getItem();
                    openPlayerView(selectedTrack);
                }
            });

            row.setOnDragDetected(event -> {
                if (currentPlaylist == null || row.isEmpty() || !researchBar.getText().isBlank()) return;
                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent cc = new ClipboardContent();
                cc.putString(String.valueOf(row.getIndex()));
                db.setContent(cc);
                db.setDragView(row.snapshot(null, null));
                event.consume();
            });

            row.setOnDragOver(event -> {
                if (currentPlaylist == null || !event.getDragboard().hasString()) return;
                int draggedIndex = Integer.parseInt(event.getDragboard().getString());
                if (draggedIndex != row.getIndex()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            row.setOnDragDropped(event -> {
                if (currentPlaylist == null || !event.getDragboard().hasString()) return;
                int draggedIndex = Integer.parseInt(event.getDragboard().getString());
                int dropIndex = row.isEmpty() ? trackList.getItems().size() - 1 : row.getIndex();
                if (draggedIndex == dropIndex) {
                    event.setDropCompleted(false);
                    event.consume();
                    return;
                }
                List<Track> newOrder = new ArrayList<>(currentPlaylist.getTracks());
                Track dragged = newOrder.remove(draggedIndex);
                newOrder.add(dropIndex, dragged);
                PlaylistManager.getInstance().reorderPlaylist(currentPlaylist, newOrder);
                refreshTrackList();
                if (playerController != null) playerController.syncCurrentIndex();
                event.setDropCompleted(true);
                event.consume();
            });

            row.setOnDragDone(event -> event.consume());

            return row;
        });


        // Configurazione Click Sinistro sui tre puntini
        actionsColumn.setCellFactory(column -> new ActionsCell());
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

    /**
     * Configura la sidebar laterale delle playlist.
     *
     * Carica le playlist disponibili dal PlaylistManager e imposta la visualizzazione
     * in modo che nella ListView venga mostrato solo il nome della playlist.
     * Inoltre aggiunge un menu contestuale con tasto destro per modificare
     * o eliminare una playlist.
     *
     * @return nessun valore di ritorno.
     */
    private void setupSidebarPlaylistListView() {
        if (sidebarPlaylistListView == null) {
            return;
        }

        // Carica nella sidebar tutte le playlist presenti nel PlaylistManager.
        refreshSidebarPlaylists();

        if (sidebarPlaylistListView != null) {
            sidebarPlaylistListView.getSelectionModel().clearSelection();
        }

        refreshTrackList();

        // Testo mostrato quando non ci sono playlist disponibili.
        sidebarPlaylistListView.setPlaceholder(
                new Label("Nessuna playlist disponibile")
        );

        /*
         * Ogni cella mostra il nome della playlist e un menu contestuale
         * (Modifica/Elimina); le due azioni selezionano la playlist e aprono
         * la modale corrispondente.
         */
        sidebarPlaylistListView.setCellFactory(listView -> new PlaylistSidebarCell(
                playlist -> {
                    sidebarPlaylistListView.getSelectionModel().select(playlist);
                    openManagePlaylistModal(playlist);
                },
                playlist -> {
                    sidebarPlaylistListView.getSelectionModel().select(playlist);
                    openRemovePlaylistModal(playlist);
                }, playlist -> {
                    sidebarPlaylistListView.getSelectionModel().select(playlist);
                    currentPlaylist = playlist;
                    playlist.setTimesListened();
                    refreshTrackList();
                    PlaylistManager.getInstance().sync();
                    if (!playlist.getTracks().isEmpty()) {
                    openPlayerView(playlist.getTracks().getFirst());
            }

        }
        ));

        /*
         * Quando l'utente clicca su una playlist nella sidebar, la TableView mostra
         * le tracce di quella playlist e il bottone diventa "Gestisci Playlist".
         * Se la selezione viene azzerata, si torna alla vista Libreria.
         */
        sidebarPlaylistListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldPlaylist, selectedPlaylist) -> {
                    if (suppressSidebarListener) return;

                    currentPlaylist = selectedPlaylist;
                    if (selectedPlaylist != null) {
                        addButton.setText("Gestisci Playlist");
                        System.out.println("Playlist selezionata: " + selectedPlaylist.getName());
                    } else {
                        addButton.setText("Add a Track");
                    }
                    refreshTrackList();
                }
        );
    }
    // =========================================================
    // AGGIORNAMENTO AUTOMATICO OBSERVER (Task 1.2.5)
    // =========================================================
    @Override
    public void onLibraryChanged() {
        System.out.println("Notifica ricevuta dall'Observer: sto aggiornando la TableView!");
        if (currentPlaylist == null) {
            refreshTrackList();
        }
    }

    @FXML
    public void onLoadLibrary() {
        // Popoliamo il Singleton globale solo se è attualmente vuoto (evita duplicazioni al refresh)
        if (Library.getInstance().getTracks().isEmpty()) {
            Library.getInstance().addTrack(new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album", "Pop", 2023, true, true, 215));
            Library.getInstance().addTrack(new Track("C:/music/song2.mp3", "Brano Pulito", "Artista Indie", "Album", "Lo-fi", 2024, false, false, 180));
            Library.getInstance().addTrack(new Track("C:/music/song3.mp3", "Classico Greve", "Rapper Serio", "Album", "Rap", 1999, true, false, 240));
        } else {
            refreshTrackList();
        }
    }

    @FXML
    public void onAddTrack() {
        if (currentPlaylist != null) {
            openManagePlaylistModal(currentPlaylist);
            return;
        }

        DialogLoader.showModal("/views/addTrackModal.fxml", "Form Aggiunta", 600, 450, null);
        updateUndoRedoButtons();
    }

    @FXML
    public void onGoHome() {
        sidebarPlaylistListView.getSelectionModel().clearSelection();
        currentPlaylist = null;
        addButton.setText("Add a Track");
        refreshTrackList();
    }

    private void openManagePlaylistModal(Playlist playlist) {
        ManagePlaylistModalController controller = DialogLoader.showModal(
                "/views/managePlaylistModal.fxml", "Gestisci Playlist", 500, 560,
                c -> c.setPlaylist(playlist));

        if (controller != null && controller.isSaved()) {
            refreshSidebarPlaylists();
            sidebarPlaylistListView.getSelectionModel().select(playlist);
            refreshTrackList();
            updateUndoRedoButtons();
        }
    }

    private ContextMenu createLibraryContextMenu(TableRow<Track> row) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle(
                "-fx-selection-bar: #d0d0d0; " +
                        "-fx-selection-bar-text: #000000; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: normal;"
        );

        MenuItem editItem = new MenuItem("Modifica traccia");
        MenuItem addToPlaylistItem = new MenuItem("Aggiungi a una playlist");
        MenuItem deleteItem = new MenuItem("Elimina traccia");

        //editItem.setOnAction(event -> modifyTrack(row.getItem()));

        editItem.setOnAction(event -> {
            Track selectedTrack = row.getItem();
            onEdit(selectedTrack);

        });

        addToPlaylistItem.setOnAction(event -> openAddToPlaylistsModal(row.getItem()));

        deleteItem.setOnAction(event -> {
            Track currentTrack = row.getItem();
            trackList.getSelectionModel().select(currentTrack);

            DeleteTrackController dialogController = DialogLoader.showModal(
                    "/views/deleteTrack.fxml", "Conferma Eliminazione", 600, 150,
                    c -> c.setContext(true));

            if (dialogController != null && dialogController.isConfirmed()) {
                // Cascade: rimuove dalla libreria E da tutte le playlist che la contengono
                Command removeCmd = new RemoveTrackFromLibraryCommand(currentTrack);
                CommandManager.getInstance().executeCommand(removeCmd);
                updateUndoRedoButtons();
                if (currentTrack.equals(currentPlayingTrack.get())) {
                    rootPane.getChildren().clear();
                }
            }
        });

        contextMenu.getItems().addAll(editItem, addToPlaylistItem, deleteItem);
        return contextMenu;
    }

    private ContextMenu createPlaylistContextMenu(TableRow<Track> row) {

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle(
                "-fx-selection-bar: #d0d0d0; " +
                        "-fx-selection-bar-text: #000000; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: normal;"
        );

        MenuItem addToOtherPlaylistItem = new MenuItem("Aggiungi ad un'altra playlist");
        MenuItem removeFromPlaylistItem = new MenuItem("Rimuovi dalla playlist");

        addToOtherPlaylistItem.setOnAction(event -> openAddToPlaylistsModal(row.getItem()));

        removeFromPlaylistItem.setOnAction(event -> {
            Track currentTrack = row.getItem();
            trackList.getSelectionModel().select(currentTrack);

            DeleteTrackController dialogController = DialogLoader.showModal(
                    "/views/deleteTrack.fxml", "Rimuovi dalla playlist", 600, 150,
                    c -> c.setContext(false)); // false = "Rimuovi dalla playlist"

            if (dialogController != null && dialogController.isConfirmed()) {
                Command cmd = new RemoveTrackFromPlaylistCommand(currentPlaylist, currentTrack);
                CommandManager.getInstance().executeCommand(cmd);
                refreshTrackList();
                updateUndoRedoButtons();
                if (currentTrack.equals(currentPlayingTrack.get())) {
                    rootPane.getChildren().clear();
                }
            }
        });

        contextMenu.getItems().addAll(addToOtherPlaylistItem, removeFromPlaylistItem);
        return contextMenu;
    }

    @FXML
    public void onCreatePlaylist() {
        DialogLoader.showModal("/views/createPlaylist.fxml", "Nuova Playlist", 400, 200, null);

        // Il modale si è chiuso: ricarichiamo la sidebar e aggiorniamo i tasti Undo/Redo
        refreshSidebarPlaylists();
        updateUndoRedoButtons();

        // Verifica per il debug: stampa su console le playlist attualmente esistenti
        System.out.println("--- STATO ATTUALE PLAYLIST ---");
        PlaylistManager.getInstance().getPlaylists().forEach(p ->
                System.out.println("- " + p.getName())
        );
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
            playerController = loader.getController();

            // Registra il callback PRIMA di setTrack, così il primo aggiornamento viene catturato
            playerController.setOnTrackChanged(currentPlayingTrack::set);

            // Passaggio dell'oggetto 'Track' selezionato E dell'intera lista in tabella al controller del player
            playerController.setTrack(track, new ArrayList<>(trackList.getItems()));

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
            controller.setOnModifyDone(() -> {
                List<Track> list = Library.getInstance().getTracks();

                Track found = list.stream()
                        .filter(t -> t.getId().equals(currentPlayingTrack.get().getId()))
                        .findFirst()
                        .orElse(null);

                openPlayerView(found);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // =========================================================
    // GESTIONE UNDO / REDO GRAFICO
    // =========================================================

    @FXML
    public void onUndo() {
        CommandManager.getInstance().undo();
        updateUndoRedoButtons();
        refreshSidebarPlaylists();
        refreshTrackList();
        if (playerController != null) playerController.syncCurrentIndex();
    }

    @FXML
    public void onRedo() {
        CommandManager.getInstance().redo();
        updateUndoRedoButtons();
        refreshSidebarPlaylists();
        refreshTrackList();
        if (playerController != null) playerController.syncCurrentIndex();
    }

    private void refreshTrackList() {
        applySearch(researchBar.getText());
    }

    private void applySearch(String query) {
        List<Track> source = currentPlaylist != null
                ? currentPlaylist.getTracks()
                : Library.getInstance().getTracks();
        if (query == null || query.isBlank()) {
            trackList.getItems().setAll(source);
        } else {
            String q = query.toLowerCase();
            trackList.getItems().setAll(
                    source.stream()
                            .filter(t -> matches(t.getName(), q)
                                    || matches(t.getArtist(), q)
                                    || matches(t.getAlbum(), q)
                                    || matches(t.getGenre(), q))
                            .toList()
            );
        }
        trackList.refresh();
    }
    private void generateTop10Playlist() {

        PlaylistManager pm = PlaylistManager.getInstance();

        Playlist top10 = pm.getPlaylists()
                .stream()
                .filter(p -> p.getName().equals("🔥 Top 10 Tracks"))
                .findFirst()
                .orElseGet(() -> {
                    Playlist p = new Playlist("🔥 Top 10 Tracks");
                    pm.addPlaylist(p);
                    return p;
                });

        List<Track> topTracks = Library.getInstance()
                .getTracks()
                .stream()
                .sorted((t1, t2) ->
                        Integer.compare(t2.getTimesListened(), t1.getTimesListened()))
                .limit(10)
                .toList();

        top10.reorderTracks(topTracks);

    }
    private boolean matches(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }

    /**
     * Controlla lo stato degli stack nel CommandManager e accende/spegne i bottoni
     */
    public void updateUndoRedoButtons() {
        // Se non ci sono azioni da annullare (canUndo = false), il bottone è disabilitato (true)
        undoButton.setDisable(!commands.CommandManager.getInstance().canUndo());

        // Se non ci sono azioni da ripristinare (canRedo = false), il bottone è disabilitato (true)
        redoButton.setDisable(!commands.CommandManager.getInstance().canRedo());
    }

    /**
     * Apre la modale di conferma eliminazione playlist.
     *
     * Il metodo carica removePlaylist.fxml, passa al relativo controller
     * la playlist selezionata e mostra una finestra modale.
     *
     * @param playlist playlist che l'utente vuole eliminare.
     * @return nessun valore di ritorno.
     * @throws RuntimeException non viene lanciata direttamente, ma eventuali errori
     *                          di caricamento dell'FXML vengono gestiti nel catch.
     */
    private void openRemovePlaylistModal(Playlist playlist) {
        DialogLoader.<RemovePlaylistController>showModal(
                "/views/removePlaylist.fxml", "Conferma eliminazione playlist", 400, 200,
                c -> c.setPlaylistToRemove(playlist));

        // Dopo la chiusura ricarichiamo sidebar e tabella: se la playlist è stata
        // eliminata sparisce dall'elenco e le sue tracce non restano visibili.
        refreshSidebarPlaylists();
        refreshTrackList();
    }

    private void openAddToPlaylistsModal(Track track) {
        DialogLoader.<AddToPlaylistsModalController>showModal(
                "/views/addToPlaylistsModal.fxml", "Aggiungi a playlist", 400, 440,
                c -> c.setTrack(track));

        updateUndoRedoButtons();

        // Se si è in vista playlist, aggiorna la tracklist
        if (currentPlaylist != null) {
            refreshTrackList();
        }
    }

    private void onEdit(Track selectedTrack){
        if (selectedTrack.equals(currentPlayingTrack.get())) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma modifica");
            alert.setHeaderText(null);
            alert.setContentText(
                    "La traccia è attualmente in riproduzione.\n" +
                            "Continuare la modifica interromperà la riproduzione.\n\n" +
                            "Vuoi procedere?"
            );

            ButtonType yesButton = new ButtonType("Sì");
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == yesButton) {
                // stop playback se necessario
                rootPane.getChildren().clear();

                modifyTrack(selectedTrack);
            }

        } else {

            modifyTrack(selectedTrack);
        }
    }
}