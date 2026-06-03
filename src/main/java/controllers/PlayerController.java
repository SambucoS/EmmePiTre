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

    /**
     * Serve a impostare i parametri, da visualizzare durante la riproduzione
     * @param track la traccia passata dal controller principale
     */
    public void setTrack(Track track) {
        this.track = track;
        this.tnameLbl.setText(track.getName());
        this.durationLbl.setText(durationFormatter(track.getDuration()));

    }

    /**
     * Attualmente serve per cambiare visivamente lo stato del bottone
     * @param actionEvent è l'evento che genera il cambio di stato (la pressione del pulsante "Play"|"Pause")
     */
    public void handleStatus(ActionEvent actionEvent) {
        if (statusButton.getText().equals("Play")){
            statusButton.setText("Pause");
        } else if (statusButton.getText().equals("Pause")) {
            statusButton.setText("Play");
        }
    }

    /**
     * Si occupa di formattare la durata passando da ad es. 120 secondi a 2:00
     *
     * @param seconds la durata della canzone in secondi
     * @return la durata formattata
     */
    public String durationFormatter(int seconds) {
        int minutes = seconds / 60; // corrisponde al lato dei minuti "'MM':SS"
        int secondsLeft = seconds % 60; // corrsisponde al lato dei secondi "MM:'SS'"
        return String.format("%d:%02d", minutes, secondsLeft);
    }
}