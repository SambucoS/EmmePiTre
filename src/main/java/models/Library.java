package models;

import interfaces.LibraryObserver;
import java.util.ArrayList;
import java.util.List;


    private static Library instance;
    private final List<LibraryObserver> observers;

    // Costruttore privato per il Singleton
    private Library() {
        this.observers = new ArrayList<>();
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
    public void addTrack(Track track) {
    }

    public void removeTrack(Track track) {
    }

    public List<Track> getTracks() {
    }
    // =========================================================
    // GESTIONE LABEL
    // =========================================================
    public void toggleFavourite(Track track) {
        track.setFavourite(!track.isFavourite());
    }
    public void toggleExplicit(Track track) {
        track.setExplicit(!track.isExplicit());
    }

    // =========================================================
    // FUNZIONE DI SINCRONIZZAZIONE (Task 1.2.4 / US 1.1)
    // =========================================================
    public void sync() {
    }
}

