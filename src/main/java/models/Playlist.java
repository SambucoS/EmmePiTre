package models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import observer.PlaylistObserver;

/**
 * Classe modello che rappresenta una singola playlist.
 *
 * Una playlist possiede un nome identificativo e una lista di tracce.
 * La classe implementa l'interfaccia TrackList, quindi fornisce metodi
 * comuni per aggiungere, rimuovere e consultare tracce.
 */
public class Playlist implements TrackList {

    /**
     * Nome della playlist.
     */
    private String name;

    /**
     * Lista delle tracce contenute nella playlist.
     */
    private final List<Track> tracks;

    private int timesListened;

    /**
     * Osservatori registrati per essere notificati quando cambia il contenuto
     * della playlist (aggiunta, rimozione o riordino di tracce). Non viene
     * serializzato su JSON: è uno stato puramente a runtime.
     */
    private final List<PlaylistObserver> observers = new ArrayList<>();

    /**
     * Costruttore vuoto obbligatorio per la deserializzazione JSON tramite Jackson.
     *
     * Inizializza la lista delle tracce come lista vuota.
     *
     * @param nessun parametro in ingresso.
     * @return nessun valore di ritorno, perché è un costruttore.
     * @throws nessuna eccezione prevista.
     */
    public Playlist() {
        this.tracks = new ArrayList<>();
    }

    /**
     * Costruttore usato per creare una playlist con un nome specifico.
     *
     * La playlist viene creata inizialmente senza tracce.
     *
     * @param name nome da assegnare alla playlist.
     * @return nessun valore di ritorno, perché è un costruttore.
     * @throws IllegalArgumentException se il nome della playlist è null o vuoto.
     */
    public Playlist(String name) {
        setName(name);
        this.tracks = new ArrayList<>(); // Crea una lista vuota di tracce
        this.timesListened = 0;
    }

    /**
     * Metodo usato da Jackson per caricare la lista delle tracce dal file JSON.
     *
     * Poiché l'attributo tracks è final, non viene sostituita la lista,
     * ma vengono copiati al suo interno gli elementi caricati dal JSON.
     *
     * @param loadedTracks lista di tracce caricata dal file JSON.
     * @return nessun valore di ritorno.
     * @throws nessuna eccezione prevista.
     */
    @JsonSetter("tracks")
    private void setTracksForJackson(List<Track> loadedTracks) {
        this.tracks.clear();

        // Se il JSON contiene una lista di tracce valida, la copiamo nella lista interna.
        if (loadedTracks != null) {
            this.tracks.addAll(loadedTracks);
        }
    }

    /**
     * Restituisce il nome della playlist.
     *
     * @param nessun parametro in ingresso.
     * @return nome della playlist.
     * @throws nessuna eccezione prevista.
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome della playlist.
     *
     * Il nome non può essere null, vuoto o composto solo da spazi.
     * Il metodo trim() rimuove eventuali spazi all'inizio e alla fine.
     *
     * @param name nuovo nome della playlist.
     * @return nessun valore di ritorno.
     * @throws IllegalArgumentException se il nome è null o vuoto.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto.");
        }

        // Salva il nome eliminando eventuali spazi inutili.
        this.name = name.trim(); // Salva il nome ripulito dagli spazi inutili
    }

    public int getTimesListened(){ return this.timesListened;}
    public void setTimesListened(){ this.timesListened ++;}

    /**
     * Registra un osservatore che verrà notificato ad ogni cambiamento
     * del contenuto di questa playlist.
     *
     * @param observer osservatore da registrare.
     */
    @JsonIgnore
    public void addObserver(PlaylistObserver observer) {
        observers.add(observer);
    }

    /**
     * Rimuove un osservatore precedentemente registrato, così da non
     * riceverne più le notifiche.
     *
     * @param observer osservatore da rimuovere.
     */
    public void removeObserver(PlaylistObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica tutti gli osservatori registrati che il contenuto della
     * playlist è cambiato.
     */
    private void notifyObservers() {
        for (PlaylistObserver observer : observers) {
            observer.onPlaylistChanged(this);
        }
    }

    /**
     * Aggiunge una traccia alla playlist.
     *
     * @param track traccia da aggiungere alla playlist.
     * @return nessun valore di ritorno.
     * @throws IllegalArgumentException se la traccia è null.
     */
    @Override
    public  void addTrack(Track track) {
        if (track == null) { // Controlliamo che la traccia non sia null
            throw new IllegalArgumentException("La traccia non può essere null.");
        }

        tracks.add(track);
        notifyObservers();
    }

    /**
     * Rimuove una traccia dalla playlist.
     *
     * La rimozione riguarda solo questa playlist e non elimina la traccia
     * dalla libreria principale dell'applicazione.
     *
     * @param track traccia da rimuovere dalla playlist.
     * @return nessun valore di ritorno.
     * @throws IllegalArgumentException se la traccia è null.
     */
    @Override
    public void removeTrack(Track track) {
        if (track == null) {
            throw new IllegalArgumentException("La traccia non può essere null.");
        }

        tracks.remove(track);
        notifyObservers();
    }

    /**
     * Restituisce l'elenco delle tracce presenti nella playlist.
     *
     * La lista restituita non è modificabile direttamente dall'esterno.
     * Questo protegge lo stato interno della playlist e obbliga a usare
     * i metodi addTrack() e removeTrack() per modificarla.
     *
     * @param nessun parametro in ingresso.
     * @return lista non modificabile delle tracce contenute nella playlist.
     * @throws nessuna eccezione prevista.
     */
    @Override
    public List<Track> getTracks() {
        // Chi riceve la lista può leggerla, ma non può modificarla direttamente (Incapsulamento)
        return Collections.unmodifiableList(tracks);
    }

    /**
     * Restituisce il numero di tracce contenute nella playlist.
     *
     * @param nessun parametro in ingresso.
     * @return numero di tracce presenti nella playlist.
     * @throws nessuna eccezione prevista.
     */
    @Override
    public int getSize() {
        return tracks.size();
    }

    /**
     * Verifica se una traccia è presente nella playlist.
     *
     * @param track traccia da cercare nella playlist.
     * @return true se la traccia è presente, false altrimenti.
     * @throws nessuna eccezione prevista.
     */
    public boolean containsTrack(Track track) {
        return tracks.contains(track);
    }

    public void reorderTracks(List<Track> newOrder) {
        tracks.clear();
        tracks.addAll(newOrder);
        notifyObservers();
    }

    /**
     * Verifica se la playlist è vuota.
     *
     * L'annotazione JsonIgnore evita che questa proprietà venga considerata
     * durante la serializzazione/deserializzazione JSON.
     *
     * @param nessun parametro in ingresso.
     * @return true se la playlist non contiene tracce, false altrimenti.
     * @throws nessuna eccezione prevista.
     */
    @JsonIgnore
    // Restituisce true se la playlist non contiene tracce
    public boolean isEmpty() {
        return tracks.isEmpty();
    }

    /**
     * Restituisce una rappresentazione testuale della playlist.
     *
     * Utile per debug, stampa in console o visualizzazione provvisoria.
     *
     * @param nessun parametro in ingresso.
     * @return stringa descrittiva della playlist.
     * @throws nessuna eccezione prevista.
     */
    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", tracks=" + tracks.size() +
                '}';
    }

    /**
     * Confronta questa playlist con un altro oggetto.
     *
     * Due playlist sono considerate uguali se hanno lo stesso nome.
     *
     * @param obj oggetto da confrontare con questa playlist.
     * @return true se l'oggetto rappresenta una playlist con lo stesso nome, false altrimenti.
     * @throws nessuna eccezione prevista.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Playlist)) {
            return false;
        }

        Playlist playlist = (Playlist) obj;
        return Objects.equals(name, playlist.name);
    }

    /**
     * Calcola il codice hash della playlist.
     *
     * È coerente con equals(), quindi viene calcolato usando il nome della playlist.
     *
     * @param nessun parametro in ingresso.
     * @return codice hash della playlist.
     * @throws nessuna eccezione prevista.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}