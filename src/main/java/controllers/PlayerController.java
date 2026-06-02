package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import models.Track;

public class PlayerController {

    @FXML
    private Button statusButton;

    @FXML
    private Slider progressSlider;

    private Track track;

    public void setTrack(Track track) {
        this.track = track;
    }

    public void handleStatus(ActionEvent actionEvent) {
        if (statusButton.getText().equals("Play")){
            statusButton.setText("Pause");
        } else if (statusButton.getText().equals("Pause")) {
            statusButton.setText("Play");
        }
    }

}
