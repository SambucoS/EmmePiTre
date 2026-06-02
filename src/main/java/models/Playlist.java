package models;

import interfaces.TrackList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Playlist implements TrackList {

    private String name;
    private final List<Track> tracks;

    // Costruttore vuoto obbligatorio per la deserializzazione JSON (Jackson)
    public Playlist() {
        this.tracks = new ArrayList<>();
    }

    public Playlist(String name) {
        setName(name);
        this.tracks = new ArrayList<>(); // Crea una lista vuota di tracce
    }
    @JsonSetter("tracks")
    private void setTracksForJackson(List<Track> loadedTracks) {
        this.tracks.clear();
        if (loadedTracks != null) {
            this.tracks.addAll(loadedTracks);
        }
    }

    // Leggiamo il nome della playlist
    public String getName() {
        return name;
    }

    // Controlla che il nome non sia null e non sia vuoto
    // trim() rimuove gli spazi all’inizio e alla fine
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della playlist non può essere vuoto.");
        }

        this.name = name.trim(); // Salva il nome ripulito dagli spazi inutili
    }

    // Aggiunge una traccia alla playlist
    @Override
    public void addTrack(Track track) {
        if (track == null) { // Controlliamo che la traccia non sia null
            throw new IllegalArgumentException("La traccia non può essere null.");
        }

        tracks.add(track);
    }

    // Rimuove una traccia solo dalla playlist, non dalla libreria generale
    @Override
    public void removeTrack(Track track) {
        if (track == null) {
            throw new IllegalArgumentException("La traccia non può essere null.");
        }

        tracks.remove(track);
    }

    // Restituisce l’elenco delle tracce nella playlist
    @Override
    public List<Track> getTracks() {
        // Chi riceve la lista può leggerla, ma non può modificarla direttamente (Incapsulamento)
        return Collections.unmodifiableList(tracks);
    }

    // Restituisce il numero di tracce presenti nella playlist
    @Override
    public int getSize() {
        return tracks.size();
    }

    // Controlla se una traccia è presente nella playlist
    public boolean containsTrack(Track track) {
        return tracks.contains(track);
    }

    @JsonIgnore
    // Restituisce true se la playlist non contiene tracce
    public boolean isEmpty() {
        return tracks.isEmpty();
    }

    // toString() per ottenere una rappresentazione testuale dell’oggetto
    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", tracks=" + tracks.size() +
                '}';
    }

    // Definisce quando due playlist sono considerate uguali: due playlist sono uguali se hanno lo stesso nome
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

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}