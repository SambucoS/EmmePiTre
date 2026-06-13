package models;

import persistence.JsonStorageService;

import java.util.List;

public class PlaylistManager {

    private static PlaylistManager instance;
    private final JsonStorageService database;
    private final List<Playlist> playlists;

    private PlaylistManager() {
        this.database = new JsonStorageService();

        //carica i dati dal JSON
        this.playlists = database.loadPlaylistsFromFile();
    }

    public static PlaylistManager getInstance() {
        if (instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    // METODI CRUD PLAYLIST -
    // Metodo helper per il Command Pattern
    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        this.sync();
    }
    public List<Playlist> getPlaylists() {
        return this.playlists; // Restituisce la lista in RAM
    }

    public void createPlaylist(String name) {
        Playlist newPlaylist = new Playlist(name);
        this.playlists.add(newPlaylist); //Aggiunge la playlist alla RAM
        this.sync();                     //Salva sul disco
    }

    public void deletePlaylist(Playlist playlist) {
        this.playlists.remove(playlist); //Rimuove la playlist dalla RAM
        this.sync();                     //Salva sul disco
    }

    public void renamePlaylist(Playlist playlist, String newName) {
        if (playlist == null) {
            throw new IllegalArgumentException("La playlist non può essere null.");
        }

        playlist.setName(newName);
        this.sync();
    }

    // GESTIONE TRACCE NELLE PLAYLIST
    public void addTrackToPlaylist(Track track, Playlist playlist) {
        playlist.addTrack(track); //Aggiunge la traccia alla playlist in RAM
        this.sync();              //Salva sul disco
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

        playlist.removeTrack(track); //Rimuove la traccia dalla playlist in RAM
        this.sync();                 //Salva sul disco
    }

    public void reorderPlaylist(Playlist playlist, List<Track> newOrder) {
        playlist.reorderTracks(newOrder);
        sync();
    }

    // FUNZIONE DI SINCRONIZZAZIONE
    public void sync() {
        // Passa la lista aggiornata al service per la scrittura su file
        this.database.savePlaylistsToFile(this.playlists);
        System.out.println("Playlists salvate su file!");
    }
}