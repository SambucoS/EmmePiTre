package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import models.Track;

public class PlayerController {
    @FXML
    private TableColumn<Track, Void> actionsColumn;
    @FXML
    private Button statusButton;

    @FXML
    private Slider progressSlider;

    @FXML
    private Label tnameLbl;

    @FXML
    private Label durationLbl;;

    private Track track;

    public void setTrack(Track track) {
        this.track = track;
        this.tnameLbl.setText(track.getName());
        this.durationLbl.setText(Integer.toString(track.getDuration()));

    }

    public void handleStatus(ActionEvent actionEvent) {
        if (statusButton.getText().equals("Play")){
            statusButton.setText("Pause");
        } else if (statusButton.getText().equals("Pause")) {
            statusButton.setText("Play");
        }
    }

}
