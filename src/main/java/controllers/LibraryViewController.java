package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Track;



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
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        }
    }

    @FXML
    public void onLoadLibrary() {
    }

    @FXML
    public void onAddTrack() {

    }

    @FXML
    public void onRemoveTrack() {
        Track selected = trackList.getSelectionModel().getSelectedItem();
        if (selected != null) {
        }
    }
}