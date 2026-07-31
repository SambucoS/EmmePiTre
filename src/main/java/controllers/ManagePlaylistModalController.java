package controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Library;
import models.Playlist;
import models.Track;
import commands.CommandManager;
import commands.ManagePlaylistCommand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller della modale "Gestisci Playlist": permette di rinominare una
 * playlist e di scegliere, tramite checklist, quali tracce della libreria
 * devono farne parte. Al salvataggio le modifiche vengono applicate in
 * un'unica operazione annullabile tramite {@link ManagePlaylistCommand}.
 *
 * @version 1.0
 */
public class ManagePlaylistModalController {

    @FXML
    private TextField renameField;

    @FXML
    private ListView<Track> trackListView;

    private Playlist playlist;
    private final Map<Track, BooleanProperty> checkStates = new LinkedHashMap<>();
    private boolean saved = false;

    /**
     * Imposta la playlist da gestire, precompila il campo del nome e popola
     * la ListView con tutte le tracce della libreria, spuntando quelle gia'
     * presenti nella playlist.
     *
     * @param playlist la {@link Playlist} da modificare
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        renameField.setText(playlist.getName());

        checkStates.clear();
        List<Track> allTracks = Library.getInstance().getTracks();
        for (Track track : allTracks) {
            checkStates.put(track, new SimpleBooleanProperty(playlist.containsTrack(track)));
        }

        trackListView.getItems().setAll(allTracks);
        trackListView.setCellFactory(CheckBoxListCell.forListView(
                track -> checkStates.get(track),
                new StringConverter<>() {
                    @Override
                    public String toString(Track track) {
                        if (track == null) return "";
                        return track.getName() + "  —  " + track.getArtist();
                    }

                    @Override
                    public Track fromString(String s) {
                        return null;
                    }
                }
        ));
    }

    /**
     * Calcola l'eventuale rinomina e la differenza tra lo stato dei checkbox
     * e le tracce gia' presenti, quindi esegue un unico {@link ManagePlaylistCommand}
     * con tutte le modifiche e chiude la finestra.
     */
    @FXML
    private void onSave() {
        String newName = renameField.getText().trim();
        String oldName = playlist.getName();

        List<Track> toAdd = new ArrayList<>();
        List<Track> toRemove = new ArrayList<>();

        for (Map.Entry<Track, BooleanProperty> entry : checkStates.entrySet()) {
            Track track = entry.getKey();
            boolean checked = entry.getValue().get();
            boolean wasInPlaylist = playlist.containsTrack(track);

            if (checked && !wasInPlaylist) {
                toAdd.add(track);
            } else if (!checked && wasInPlaylist) {
                toRemove.add(track);
            }
        }

        boolean nameChanged = !newName.equals(oldName) && !newName.isEmpty();

        if (nameChanged || !toAdd.isEmpty() || !toRemove.isEmpty()) {
            ManagePlaylistCommand cmd = new ManagePlaylistCommand(
                    playlist,
                    nameChanged ? newName : oldName,
                    oldName,
                    toAdd,
                    toRemove
            );
            CommandManager.getInstance().executeCommand(cmd);
        }

        saved = true;
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
     * Indica se le modifiche sono state confermate con "Salva".
     *
     * @return {@code true} se l'utente ha salvato, {@code false} se ha annullato
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Recupera lo stage corrente a partire da un elemento della scena e lo chiude.
     */
    private void closeStage() {
        Stage stage = (Stage) renameField.getScene().getWindow();
        stage.close();
    }
}
