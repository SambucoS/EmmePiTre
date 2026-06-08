package controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.util.Duration;
import models.Track;

import java.util.Random;

public class PlayerController {
    @FXML
    private TableColumn<Track, Void> actionsColumn;

    @FXML
    private Button loopbutton;

    @FXML
    private Button shufflebutton;

    @FXML
    private Button statusButton;

    @FXML
    private Slider progressSlider;

    @FXML
    private Label tnameLbl;

    @FXML
    private Label durationLbl;

    @FXML
    private Label currentTime;


    private Timeline timeline;
    private int seconds = 0;
    private Track track;
    private java.util.List<Track> currentPlaylist; // Lista delle canzoni
    private int currentIndex; // Posizione della canzone attuale

    private boolean isLoopActive = false;
    private boolean isShuffleActive = false;

    /**
     * Per il {@link PlayerController} viene inizializzata una Timeline, al
     * fine di gestire la riproduzione simulata della traccia selezionata
     */
    @FXML
    public void initialize() {

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                        @Override
                        public void handle(javafx.event.ActionEvent event) {
                            // 1. Aggiorna i secondi
                            seconds++;
                            currentTime.setText(durationFormatter(seconds));
                            progressSlider.setValue(seconds);

                            // 2. LOGICA FINE CANZONE AUTOMATICA
                            if (track != null && seconds >= track.getDuration()) {

                                if (isLoopActive) {
                                    // Caso 1: Il LOOP è attivo -> La canzone ricomincia da capo
                                    seconds = 0;
                                    currentTime.setText("00:00");
                                    progressSlider.setValue(0);
                                    System.out.println("Loop attivo: la canzone ricomincia da capo.");
                                } else {
                                    // Caso 2: Il LOOP NON è attivo -> Passa alla prossima
                                    System.out.println("Canzone finita, passo alla traccia successiva.");
                                    handleNext(null);
                                }
                            }
                        }
                    })
            );
            timeline.setCycleCount(Animation.INDEFINITE);
        }



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
        this.tnameLbl.setText("🎵   " + track.getName());
        this.durationLbl.setText(durationFormatter(track.getDuration()));
        this.statusButton.setText("⏸"); // Resetta il bottone se cambia la canzone
        seconds = 0;
        currentTime.setText("00:00");
        progressSlider.setMin(0);
        progressSlider.setMax(track.getDuration());
        timeline.play();
    }

    @FXML
    public void handlePrev(ActionEvent event) {
        timeline.stop();
        progressSlider.setValue(0);


        // RIPRODUZIONE CASUALE DELLE CANZONI (francesca)

        if (isShuffleActive) {
            // RIPRODUZIONE CASUALE: Anche andando indietro, genera un indice a caso
            Random random = new Random();
            currentIndex = random.nextInt(currentPlaylist.size());
        } else {

            // RIPRODUZIONE NORMALE: Va alla traccia precedente
            currentIndex--;

            // Se scendiamo sotto l'inizio della playlist (indice minore di 0)...
            if (currentIndex < 0) {
                if (isLoopActive) {
                    // Se il LOOP è attivo, ricomincia dall'ultima canzone della playlist
                    currentIndex = currentPlaylist.size() - 1;
                } else {
                    // Altrimenti si ferma alla prima canzone (indice 0) o resetta
                    currentIndex = 0;
                    System.out.println("Sei già all'inizio della playlist.");
                    return;
                }
            }
        }

        setTrack(currentPlaylist.get(currentIndex), currentPlaylist);

    }

    @FXML
    public void handleNext(ActionEvent event) {
        timeline.stop(); // viene fermata la Timeline

        progressSlider.setValue(0);


        // RIPRODUZIONE CASUALE DELLE CANZONI (francesca)

        if (isShuffleActive) {
            // RIPRODUZIONE CASUALE: Genera un indice a caso tra 0 e la fine della playlist
            Random random = new Random();
            currentIndex = random.nextInt(currentPlaylist.size());
        } else {

            // RIPRODUZIONE NORMALE: Va alla traccia successiva
            currentIndex++;

            // Se arriviamo alla fine della playlist...
            if (currentIndex >= currentPlaylist.size()) {
                if (isLoopActive) {
                    // Se il LOOP è attivo, ricomincia dalla prima canzone (indice 0)
                    currentIndex = 0;
                } else {
                    // Altrimenti si ferma all'ultima canzone o resetta senza riprodurre
                    currentIndex = currentPlaylist.size() - 1;
                    System.out.println("Playlist terminata.");
                    return;
                }
            }
        }

        setTrack(currentPlaylist.get(currentIndex), currentPlaylist);
    }
    /**
     * Attualmente serve per cambiare visivamente lo stato del bottone
     * @param actionEvent è l'evento che genera il cambio di stato (la pressione del pulsante "Play"|"Pause")
     */
    public void handleStatus(ActionEvent actionEvent) {
        if (statusButton.getText().equals("▶")){
            statusButton.setText("⏸");
            timeline.play();
        } else if (statusButton.getText().equals("⏸")) {
            statusButton.setText("▶");
            timeline.pause();
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
        return String.format("%02d:%02d", minutes, secondsLeft);
    }

    // Francesca
    @FXML
    void onhandleloop(ActionEvent event) {
        isLoopActive = !isLoopActive;

        if (isLoopActive) {
            // Colore quando è SELEZIONATO (es. un grigio chiaro semitrasparente o un colore a tua scelta)
            loopbutton.setStyle("-fx-background-color: #d3d3d3; -fx-background-radius: 5;");
        } else {
            // Torna TRASPARENTE quando viene deselezionato
            loopbutton.setStyle("-fx-background-color: transparent;");
        }
    }


    // Francesca
    @FXML
    void onhandleshuffle(ActionEvent event) {
        isShuffleActive = !isShuffleActive;

        if (isShuffleActive) {
            shufflebutton.setStyle("-fx-background-color: #d3d3d3; -fx-background-radius: 5;");
        } else {
            shufflebutton.setStyle("-fx-background-color: transparent;");
        }
    }



}