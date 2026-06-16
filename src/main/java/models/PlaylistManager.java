package models;

import criteria.TrackCriteria;
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

    private boolean playlistNameExists(String name, Playlist playlistToIgnore) {
        if (name == null) {
            return false;
        }

        String normalizedName = name.trim();

        return playlists.stream()
                .anyMatch(playlist ->
                        playlist != playlistToIgnore &&
                                playlist.getName().equalsIgnoreCase(normalizedName)
                );
    }

    private void checkPlaylistNameAvailable(String name, Playlist playlistToIgnore) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto.");
        }

        if (playlistNameExists(name, playlistToIgnore)) {
            throw new IllegalArgumentException("Esiste già una playlist con questo nome.");
        }
    }

    // METODI CRUD PLAYLIST -
    // Metodo helper per il Command Pattern
    public void addPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("La playlist non può essere null.");
        }

        checkPlaylistNameAvailable(playlist.getName(), null);

        this.playlists.add(playlist);
        this.sync();
    }

    public List<Playlist> getPlaylists() {
        return this.playlists; // Restituisce la lista in RAM
    }

    public void createPlaylist(String name) {
        checkPlaylistNameAvailable(name, null);

        Playlist newPlaylist = new Playlist(name);
        this.playlists.add(newPlaylist);
        this.sync();
    }

    /**
     * Crea una playlist automatica selezionando dalla libreria le tracce che
     * soddisfano il criterio dato (Strategy/Composite pattern). Il criterio puo'
     * essere singolo o composto (es. {@link criteria.AndCriteria}).
     *
     * @param name     nome della nuova playlist
     * @param criteria criterio di selezione delle tracce
     * @return la playlist creata
     * @throws IllegalArgumentException se il nome non e' valido o nessuna traccia soddisfa il criterio
     */
    public Playlist createAutomaticPlaylist(String name, TrackCriteria criteria) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto.");
        }
        if (criteria == null) {
            throw new IllegalArgumentException("Seleziona almeno un criterio.");
        }

        String playlistName = name.trim();

        // Controlla che non esista già una playlist con lo stesso nome
        checkPlaylistNameAvailable(playlistName, null);

        Playlist automaticPlaylist = new Playlist(playlistName);

        for (Track track : Library.getInstance().getTracks()) {
            if (criteria.matches(track)) {
                automaticPlaylist.addTrack(track);
            }
        }

        if (automaticPlaylist.isEmpty()) {
            throw new IllegalArgumentException("Nessuna traccia trovata per i criteri selezionati.");
        }

        this.playlists.add(automaticPlaylist);
        this.sync();

        return automaticPlaylist;
    }

    public void deletePlaylist(Playlist playlist) {
        this.playlists.remove(playlist); //Rimuove la playlist dalla RAM
        this.sync();                     //Salva sul disco
    }

    public void renamePlaylist(Playlist playlist, String newName) {
        if (playlist == null) {
            throw new IllegalArgumentException("La playlist non può essere null.");
        }

        checkPlaylistNameAvailable(newName, playlist);

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
    public Playlist getMostListenedPlaylist() {
        return playlists.stream()
                .max((p1, p2) -> Integer.compare(
                        p1.getTimesListened(),
                        p2.getTimesListened()
                ))
                .orElse(null);
    }
    public boolean isMostListenedPlaylist(Playlist playlist) {
        Playlist best = getMostListenedPlaylist();
        return best != null && best.equals(playlist);
    }
    // FUNZIONE DI SINCRONIZZAZIONE
    public void sync() {
        // Passa la lista aggiornata al service per la scrittura su file
        this.database.savePlaylistsToFile(this.playlists);
        System.out.println("Playlists salvate su file!");
    }
}