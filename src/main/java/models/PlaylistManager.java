package models;

import services.JsonStorageService;

import java.util.List;

public class PlaylistManager {

    private static PlaylistManager instance;
    private final JsonStorageService database;
    private final List<Playlist> playlists;

    // Se vuoi aggiornare la GUI delle playlist in tempo reale,
    // puoi creare un PlaylistObserver analogo a LibraryObserver!

    private PlaylistManager() {
        this.database = new JsonStorageService();
        this.playlists = database.loadPlaylistsFromFile(); // Il DAO fa il suo lavoro
    }

    public static PlaylistManager getInstance() {
        if (instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    public List<Playlist> getPlaylists() {
        return this.playlists;
    }

    public void createPlaylist(String name) {
        Playlist newPlaylist = new Playlist(name);
        this.playlists.add(newPlaylist);
        this.sync();
    }

    public void deletePlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("La playlist non può essere null.");
        }

        this.playlists.remove(playlist);
        this.sync();
    }

    public void addTrackToPlaylist(Track track, Playlist playlist) {
        playlist.addTrack(track);
        this.sync();
    }

    public void removeTrackFromPlaylist(Track track, Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("Seleziona una playlist.");
        }

        if (track == null) {
            throw new IllegalArgumentException("Seleziona una traccia da rimuovere.");
        }

        if (!playlist.containsTrack(track)) {
            throw new IllegalArgumentException("La traccia non è presente nella playlist.");
        }

        playlist.removeTrack(track);
        this.sync();
    }

    private void sync() {
        this.database.savePlaylistsToFile(this.playlists);
        System.out.println("Playlists salvate su file!");
    }
}