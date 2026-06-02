package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Track;
import models.Playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonStorageService {

    private final String FILE_PATH = "jsonfiles/tracks.json";
    private final String PLAYLISTS_FILE = "jsonfiles/playlists.json";

    private final ObjectMapper mapper = new ObjectMapper();

    // Nessuna variabile "cache" interna. Il service non ha "stato".

    public List<Track> loadFromFile() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                System.out.println("File JSON non trovato, creo una libreria vuota.");
                return new ArrayList<>();
            }

            System.out.println("=== Tracce caricate con successo dal file JSON ===");
            return mapper.readValue(file, new TypeReference<>() {});

        } catch (Exception e) {
            System.out.println(" ====== ECCEZIONE DURANTE IL CARICAMENTO =======");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveToFile(List<Track> tracksToSave) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), tracksToSave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ==========================================
    // SEZIONE PLAYLIST
    // ==========================================

    public List<Playlist> loadPlaylistsFromFile() {
        try {
            File file = new File(PLAYLISTS_FILE);

            if (!file.exists()) {
                System.out.println("File JSON delle playlist non trovato, creo un elenco vuoto.");
                return new ArrayList<>();
            }

            System.out.println("=== Playlist caricate con successo dal file JSON ===");
            // Jackson userà il costruttore vuoto che abbiamo aggiunto alla classe Playlist
            return mapper.readValue(file, new TypeReference<>() {});

        } catch (Exception e) {
            System.out.println(" ====== ECCEZIONE DURANTE IL CARICAMENTO DELLE PLAYLIST =======");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void savePlaylistsToFile(List<Playlist> playlistsToSave) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(PLAYLISTS_FILE), playlistsToSave);
        } catch (Exception e) {
            System.out.println(" ====== ECCEZIONE DURANTE IL SALVATAGGIO DELLE PLAYLIST =======");
            e.printStackTrace();
        }
    }
}