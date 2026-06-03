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
    private java.util.List<Track> currentPlaylist; // Lista delle canzoni
    private int currentIndex; // Posizione della canzone attuale

    /**
     * Serve a impostare i parametri da visualizzare durante la riproduzione,
     * passando anche il contesto dell'intera lista per permettere lo skip.
     * * @param track la traccia corrente
     * @param playlist l'intera lista delle tracce visualizzata nella tabella
     */
    public void setTrack(Track track, java.util.List<Track> playlist) {
        this.track = track;
        this.currentPlaylist = playlist;
        this.currentIndex = playlist.indexOf(track); // Trova in che posizione siamo

        this.tnameLbl.setText(track.getName());
        this.durationLbl.setText(durationFormatter(track.getDuration()));
        this.statusButton.setText("Play"); // Resetta il bottone se cambia la canzone
    }

    @FXML
    public void handlePrev(ActionEvent event) {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentIndex--;
            if (currentIndex < 0) {
                currentIndex = currentPlaylist.size() - 1; // Se sei alla prima, riparti dall'ultima
            }
            // Richiama setTrack con la nuova canzone per aggiornare la UI
            setTrack(currentPlaylist.get(currentIndex), currentPlaylist);
        }
    }

    @FXML
    public void handleNext(ActionEvent event) {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentIndex++;
            if (currentIndex >= currentPlaylist.size()) {
                currentIndex = 0; // Se sei all'ultima, riparti dalla prima
            }
            // Richiama setTrack con la nuova canzone per aggiornare la UI
            setTrack(currentPlaylist.get(currentIndex), currentPlaylist);
        }
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