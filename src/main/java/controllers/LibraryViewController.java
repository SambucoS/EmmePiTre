package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Track;

import java.io.IOException;


    public Label titleBar;
    public HBox mainBar;

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

    }

    @FXML
    public void onRemoveTrack() {
        Track selected = trackList.getSelectionModel().getSelectedItem();
        if (selected != null) {
        }
    }


        }

    }

    @FXML
        try {
            Parent root = loader.load();



            e.printStackTrace();
        }
    }
}