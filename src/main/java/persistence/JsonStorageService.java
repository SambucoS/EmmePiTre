package persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Track;
import models.Playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Servizio di persistenza che legge e scrive tracce e playlist su file JSON
 * tramite Jackson. La classe non mantiene alcuno stato interno oltre al
 * mapper: ogni chiamata legge o scrive direttamente sui file di destinazione.
 *
 * @version 1.0
 */
public class JsonStorageService {

    private final String FILE_PATH = "jsonfiles/tracks.json";
    private final String PLAYLISTS_FILE = "jsonfiles/playlists.json";

    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // Nessuna variabile "cache" interna. Il service non ha "stato".

    /**
     * Carica l'elenco delle tracce dal file JSON della libreria.
     *
     * @return la {@link List} di {@link Track} lette dal file, oppure una lista
     *         vuota se il file non esiste o si verifica un errore di lettura
     */
    public List<Track> loadFromFile() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists() || file.length() == 0) {
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

    /**
     * Sovrascrive il file JSON della libreria con l'elenco di tracce fornito.
     *
     * @param tracksToSave la {@link List} di {@link Track} da salvare su file
     */
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

    /**
     * Carica l'elenco delle playlist dal relativo file JSON.
     *
     * @return la {@link List} di {@link Playlist} lette dal file, oppure una lista
     *         vuota se il file non esiste o si verifica un errore di lettura
     */
    public List<Playlist> loadPlaylistsFromFile() {
        try {
            File file = new File(PLAYLISTS_FILE);

            if (!file.exists() || file.length() == 0) {
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

    /**
     * Sovrascrive il file JSON delle playlist con l'elenco fornito.
     *
     * @param playlistsToSave la {@link List} di {@link Playlist} da salvare su file
     */
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