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
import models.commands.CommandManager;
import models.commands.ManagePlaylistCommand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManagePlaylistModalController {

    @FXML
    private TextField renameField;

    @FXML
    private ListView<Track> trackListView;

    private Playlist playlist;
    private final Map<Track, BooleanProperty> checkStates = new LinkedHashMap<>();
    private boolean saved = false;

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

    @FXML
    private void onCancel() {
        closeStage();
    }

    public boolean isSaved() {
        return saved;
    }

    private void closeStage() {
        Stage stage = (Stage) renameField.getScene().getWindow();
        stage.close();
    }
}
