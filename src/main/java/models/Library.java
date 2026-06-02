package models;

import interfaces.LibraryObserver;
import interfaces.TrackList;
import services.JsonStorageService;
import java.util.ArrayList;
import java.util.List;

public class Library implements TrackList {

    private static Library instance;
    private final JsonStorageService database;
    private final List<LibraryObserver> observers;

    // LA NOVITÀ: Ora è la Library a possedere la lista delle tracce in memoria!
    private final List<Track> tracks;

    // Costruttore privato per il Singleton
    private Library() {
        this.database = new JsonStorageService();
        this.observers = new ArrayList<>();

        // Al primo avvio, carica i dati dal JSON e li salva nella sua lista interna
        this.tracks = database.loadFromFile();
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    // =========================================================
    // GESTIONE OBSERVER (Task 1.2.5)
    // =========================================================
    public void addObserver(LibraryObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(LibraryObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        for (LibraryObserver observer : observers) {
            observer.onLibraryChanged();
        }
    }

    // =========================================================
    // METODI CRUD (Task 1.2.4)
    // =========================================================
    @Override
    public void addTrack(Track track) {
        this.tracks.add(track); // 1. Aggiunge la traccia alla RAM
        this.sync();            // 2. Salva sul disco
        notifyObservers();      // 3. Aggiorna la GUI
    }

    @Override
    public void removeTrack(Track track) {
        this.tracks.remove(track); // 1. Rimuove la traccia dalla RAM
        this.sync();               // 2. Salva sul disco
        notifyObservers();         // 3. Aggiorna la GUI
    }

    @Override
    public List<Track> getTracks() {
        return this.tracks; // Restituisce la lista in RAM
    }

    @Override
    public int getSize() {
        return this.tracks.size();
    }

    // =========================================================
    // GESTIONE LABEL
    // =========================================================
    public void toggleFavourite(Track track) {
        track.setFavourite(!track.isFavourite());
        this.sync();
        notifyObservers();
    }

    public void toggleExplicit(Track track) {
        track.setExplicit(!track.isExplicit());
        this.sync();
        notifyObservers();
    }

    // =========================================================
    // FUNZIONE DI SINCRONIZZAZIONE (Task 1.2.4 / US 1.1)
    // =========================================================
    public void sync() {
        // Passa la lista aggiornata al DAO per la scrittura su file
        this.database.saveToFile(this.tracks);
        System.out.println("File JSON sovrascritto e aggiornato con i nuovi dati!");
    }
}