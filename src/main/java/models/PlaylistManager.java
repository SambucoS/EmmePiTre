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

    public Playlist createAutomaticPlaylistByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Il genere non può essere vuoto.");
        }

        String selectedGenre = genre.trim();
        String playlistName = "Playlist " + selectedGenre;

        // Controlla che non esista già una playlist con lo stesso nome
        checkPlaylistNameAvailable(playlistName, null);

        Playlist automaticPlaylist = new Playlist(playlistName);

        for (Track track : Library.getInstance().getTracks()) {
            if (track.getGenre() != null &&
                    track.getGenre().equalsIgnoreCase(selectedGenre)) {
                automaticPlaylist.addTrack(track);
            }
        }

        if (automaticPlaylist.isEmpty()) {
            throw new IllegalArgumentException("Nessuna traccia trovata per il genere: " + selectedGenre);
        }

        this.playlists.add(automaticPlaylist);
        this.sync();

        return automaticPlaylist;
    }

    public Playlist createAutomaticPlaylistByYear(int year) {
        String playlistName = "Playlist " + year;

        // Controlla che non esista già una playlist con lo stesso nome
        checkPlaylistNameAvailable(playlistName, null);

        Playlist automaticPlaylist = new Playlist(playlistName);

        for (Track track : Library.getInstance().getTracks()) {
            if (track.getYear() == year) {
                automaticPlaylist.addTrack(track);
            }
        }

        if (automaticPlaylist.isEmpty()) {
            throw new IllegalArgumentException("Nessuna traccia trovata per l'anno: " + year);
        }

        this.playlists.add(automaticPlaylist);
        this.sync();

        return automaticPlaylist;
    }

    public Playlist createAutomaticPlaylistByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Il tag non può essere vuoto.");
        }

        String selectedTag = tag.trim().toLowerCase();
        String playlistName;

        if (selectedTag.equals("preferiti") ||
                selectedTag.equals("favourite") ||
                selectedTag.equals("favorite")) {
            playlistName = "Playlist Preferiti";

        } else if (selectedTag.equals("explicit") ||
                selectedTag.equals("espliciti")) {
            playlistName = "Playlist Explicit";

        } else {
            throw new IllegalArgumentException("Tag non valido. Usa: preferiti oppure explicit.");
        }

        // Controlla che non esista già una playlist con lo stesso nome
        checkPlaylistNameAvailable(playlistName, null);

        Playlist automaticPlaylist = new Playlist(playlistName);

        for (Track track : Library.getInstance().getTracks()) {
            if (selectedTag.equals("preferiti") ||
                    selectedTag.equals("favourite") ||
                    selectedTag.equals("favorite")) {

                if (track.isFavourite()) {
                    automaticPlaylist.addTrack(track);
                }

            } else if (selectedTag.equals("explicit") ||
                    selectedTag.equals("espliciti")) {

                if (track.isExplicit()) {
                    automaticPlaylist.addTrack(track);
                }
            }
        }

        if (automaticPlaylist.isEmpty()) {
            throw new IllegalArgumentException("Nessuna traccia trovata per il tag: " + tag);
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