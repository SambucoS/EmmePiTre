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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Library;
import models.Track;

import java.io.IOException;

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
    @FXML private TableColumn<Track, Void> actionsColumn;
    @FXML private TextField researchBar;
    @FXML private Button addButton;

    @FXML
    public void initialize() {
        //Stile della barra di selezione
        trackList.setStyle(
                "-fx-selection-bar: #e0e0e0; " +
                        "-fx-selection-bar-text: #000000; " +
                        "-fx-selection-bar-non-focused: #f0f0f0;"
        );

        //Correzione PropertyValueFactory
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        explicitColumn.setCellValueFactory(new PropertyValueFactory<>("explicit"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        //Registrazione del Controller come Observer
        Library.getInstance().addObserver(this);

        //Colonna Preferiti
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
                        Library.getInstance().toggleFavourite(currentTrack);
                    });

                    setGraphic(starLabel);
                }
            }
        });

        //Colonna Explicit
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

        //Formattazione personalizzata della Durata (da secondi a mm:ss)
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
        //popolamento
        onLoadLibrary();
        // MENU A TENDINA (Modifica/Elimina)

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
                        event.consume(); //Blocca il click per non farlo "rimbalzare" sulla riga sotto

                        TableRow<Track> row = getTableRow();
                        if (row != null && !row.isEmpty() && row.getContextMenu() != null) {
                            ContextMenu menu = row.getContextMenu(); // Recuperiamo il menu nativo della riga

                            // Se il menu è aperto, chiudilo, altrimenti mostralo
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
    }

    // AGGIORNAMENTO AUTOMATICO OBSERVER (Task 1.2.5)
    @Override
    public void onLibraryChanged() {
        System.out.println("Notifica ricevuta dall'Observer: sto aggiornando la TableView!");
        trackList.getItems().setAll(Library.getInstance().getTracks());
        trackList.refresh();
    }

    @FXML
    public void onLoadLibrary() {
            trackList.getItems().setAll(Library.getInstance().getTracks());
    }

    @FXML
    public void onAddTrack() throws IOException {
        //Carichiamo modale
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addTrackModal.fxml"));
        Parent root = loader.load();

        //Recuperiamo il controller
        AddTrackController dialogController = loader.getController();

        //Prepariamo la finestra (Stage)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Form Aggiunta");
        dialogStage.initModality(Modality.APPLICATION_MODAL); // Rende la finestra bloccante
        dialogStage.setResizable(false); // Blocca il ridimensionamento

        //Impostiamo la scena con le dimensioni fisse
        Scene scene = new Scene(root, 600, 300);
        dialogStage.setScene(scene);

        //Mostriamo il modale e aspettiamo
        dialogStage.showAndWait();
    }

    private ContextMenu createActionMenu(TableRow<Track> row) {
        ContextMenu contextMenu = new ContextMenu();
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
            System.out.println("Modifica richiesta per la traccia: " + currentTrack.getName());
        });

        // 2. Logica per l'ELIMINAZIONE (Apertura Modale)
        deleteItem.setOnAction(event -> {
            Track currentTrack = row.getItem();

            // Evidenzia visivamente la riga che stiamo per eliminare
            trackList.getSelectionModel().select(currentTrack);

            try {
                //Carica il file FXML del modale
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/deleteTrack.fxml"));
                Parent root = loader.load();

                //Carica il controller e setta il contesto (libreria o playlist)
                DeleteTrackController dialogController = loader.getController();
                dialogController.setContext(true);

                //Prepariamo la finestra
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Conferma Eliminazione");
                dialogStage.initModality(Modality.APPLICATION_MODAL); // Rende la finestra bloccante
                dialogStage.setResizable(false); // Blocca il ridimensionamento

                //Impostiamo le dimensioni
                Scene scene = new Scene(root, 600, 150);
                dialogStage.setScene(scene);

                //Mostriamo il modale e aspettiamo
                dialogStage.showAndWait();

                //Controlliamo cosa ha scelto l'utente
                if (dialogController.isConfirmed()) {
                    // Se ha cliccato "Sono sicuro", eliminiamo la traccia
                    Library.getInstance().removeTrack(currentTrack);
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
            //Carica il file FXML del modale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/createPlaylist.fxml"));
            Parent root = loader.load();

            //Prepara la finestra (Stage)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nuova Playlist");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Blocca la finestra principale
            dialogStage.setResizable(false);

            //Imposta le dimensioni
            Scene scene = new Scene(root, 400, 200);
            dialogStage.setScene(scene);

            //Mostra il modale e attendi
            dialogStage.showAndWait();

            //debug, valutare cancellazione
            System.out.println("--- STATO ATTUALE PLAYLIST ---");
            models.PlaylistManager.getInstance().getPlaylists().forEach(p ->
                    System.out.println("- " + p.getName())
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il caricamento di createPlaylist.fxml");
        }
    }

    private void openPlayerView(Track track) {
        try {
            // 1. Carichiamo il file FXML del Player
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/playerView.fxml"));
            Parent root = loader.load();

            // 2. Recuperiamo il controller del Player e gli passiamo la traccia corrente
            PlayerController playerController = loader.getController();
            playerController.setTrack(track); // Questo metodo dovrai crearlo nel PlayerViewController

            // 3. Prepariamo la nuova finestra (Stage)
            Stage playerStage = new Stage();
            playerStage.setTitle("Riproduzione: " + track.getName());

            // Scegli l'approccio che preferisci:
            // Opzione A: Finestra indipendente (L'utente può navigare sia nella libreria che nel player contemporaneamente)
            playerStage.initModality(Modality.NONE);

            // Opzione B: Finestra bloccante (Scomoda per un player, ma blocca la libreria finché non chiudi)
            // playerStage.initModality(Modality.APPLICATION_MODAL);

            // 4. Impostiamo la scena (Adatta le dimensioni al look del tuo player)
            Scene scene = new Scene(root, 600, 100);
            playerStage.setScene(scene);

            // 5. Mostriamo la finestra del player
            playerStage.show();

            System.out.println("Player avviato per: " + track.getName());

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante il caricamento di playerView.fxml: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossibile aprire il Player Musicale.", ButtonType.OK);
            alert.showAndWait();
        }
    }

}