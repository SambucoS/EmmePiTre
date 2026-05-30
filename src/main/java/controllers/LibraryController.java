package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Track;
import services.LibraryService;

import java.io.IOException;
import java.util.List;

public class LibraryController {

    private final LibraryService service = new LibraryService();

    @FXML private TableView<Track> trackTable;

    @FXML private TableColumn<Track, String> nameColumn;
    @FXML private TableColumn<Track, String> artistColumn;
    @FXML private TableColumn<Track, String> genreColumn;
    @FXML private TableColumn<Track, Integer> yearColumn;
    @FXML private TableColumn<Track, Integer> durationColumn;
    @FXML private TableColumn<Track, Boolean> favouriteColumn;
    @FXML private TableColumn<Track, Boolean> explicitColumn;

    @FXML
    public void initialize() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        explicitColumn.setCellValueFactory(new PropertyValueFactory<>("explicit"));
    }

    @FXML
    public void onLoadLibrary() {
        List<Track> tracks = service.getTracks();
        trackTable.getItems().setAll(tracks);
    }

    @FXML
    public void onAddTrack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add-track.fxml"));
            Parent root = loader.load();

            AddTrackController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add Track");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            Track newTrack = controller.getCreatedTrack();

            if (newTrack != null) {
                service.addTrack(newTrack); // importante: salva nel service
                trackTable.getItems().setAll(service.getTracks());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onRemoveTrack() {
        Track selected = trackTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            service.removeTrack(selected);
            trackTable.getItems().setAll(service.getTracks());
        }
    }
}