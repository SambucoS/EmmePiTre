package controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Playlist;
import models.PlaylistManager;
import models.Track;
import commands.CommandManager;
import commands.ManageTrackPlaylistsCommand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller della modale "Aggiungi a playlist": mostra l'elenco di tutte le
 * playlist come checklist (precaricando quelle che gia' contengono la
 * traccia) e, al salvataggio, applica le modifiche tramite un unico
 * {@link ManageTrackPlaylistsCommand} cosi' che l'operazione sia annullabile.
 *
 * @version 1.0
 */
public class AddToPlaylistsModalController {

    @FXML
    private Label trackInfoLabel;

    @FXML
    private ListView<Playlist> playlistListView;

    private Track track;
    private final Map<Playlist, BooleanProperty> checkStates = new LinkedHashMap<>();

    /**
     * Imposta la traccia da assegnare alle playlist e popola la ListView con
     * tutte le playlist esistenti, precaricando lo stato dei checkbox in base
     * a quelle che gia' contengono la traccia.
     *
     * @param track la {@link Track} da gestire nella modale
     */
    public void setTrack(Track track) {
        this.track = track;
        trackInfoLabel.setText(track.getName() + "  —  " + track.getArtist());

        checkStates.clear();
        List<Playlist> allPlaylists = PlaylistManager.getInstance().getPlaylists();
        for (Playlist p : allPlaylists) {
            checkStates.put(p, new SimpleBooleanProperty(p.containsTrack(track)));
        }

        playlistListView.getItems().setAll(allPlaylists);
        playlistListView.setCellFactory(CheckBoxListCell.forListView(
                p -> checkStates.get(p),
                new StringConverter<>() {
                    @Override
                    public String toString(Playlist p) {
                        if (p == null) return "";
                        return p.getName();
                    }

                    @Override
                    public Playlist fromString(String s) {
                        return null;
                    }
                }
        ));
    }

    /**
     * Calcola la differenza tra lo stato dei checkbox e le playlist che gia'
     * contenevano la traccia, quindi esegue un {@link ManageTrackPlaylistsCommand}
     * con le sole playlist da aggiungere/rimuovere e chiude la finestra.
     */
    @FXML
    private void onSave() {
        List<Playlist> toAdd = new ArrayList<>();
        List<Playlist> toRemove = new ArrayList<>();

        for (Map.Entry<Playlist, BooleanProperty> entry : checkStates.entrySet()) {
            Playlist p = entry.getKey();
            boolean checked = entry.getValue().get();
            boolean wasIn = p.containsTrack(track);

            if (checked && !wasIn) {
                toAdd.add(p);
            } else if (!checked && wasIn) {
                toRemove.add(p);
            }
        }

        if (!toAdd.isEmpty() || !toRemove.isEmpty()) {
            ManageTrackPlaylistsCommand cmd = new ManageTrackPlaylistsCommand(track, toAdd, toRemove);
            CommandManager.getInstance().executeCommand(cmd);
        }

        closeStage();
    }

    /**
     * Chiude la finestra senza applicare alcuna modifica.
     */
    @FXML
    private void onCancel() {
        closeStage();
    }

    /**
     * Recupera lo stage corrente a partire da un elemento della scena e lo chiude.
     */
    private void closeStage() {
        Stage stage = (Stage) playlistListView.getScene().getWindow();
        stage.close();
    }
}
