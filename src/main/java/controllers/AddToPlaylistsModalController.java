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
import models.commands.CommandManager;
import models.commands.ManageTrackPlaylistsCommand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddToPlaylistsModalController {

    @FXML
    private Label trackInfoLabel;

    @FXML
    private ListView<Playlist> playlistListView;

    private Track track;
    private final Map<Playlist, BooleanProperty> checkStates = new LinkedHashMap<>();

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

    @FXML
    private void onCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) playlistListView.getScene().getWindow();
        stage.close();
    }
}
