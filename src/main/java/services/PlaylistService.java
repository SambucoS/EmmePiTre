package services;

import models.Playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistService {

    private final List<Playlist> playlists;

    public PlaylistService() {
        this.playlists = new ArrayList<>();
    }

    public Playlist createPlaylist(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto.");
        }

        if (existsPlaylist(name)) {
            throw new IllegalArgumentException("Esiste già una playlist con questo nome.");
        }

        Playlist playlist = new Playlist(name.trim());
        playlists.add(playlist);

        return playlist;
    }

    public List<Playlist> getPlaylists() {
        return Collections.unmodifiableList(playlists);
    }

    public boolean existsPlaylist(String name) {
        if (name == null) {
            return false;
        }

        return playlists.stream()
                .anyMatch(playlist -> playlist.getName().equalsIgnoreCase(name.trim()));
    }
}