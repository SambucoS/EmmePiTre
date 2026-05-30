package models;

import interfaces.LibraryObserver;
import services.LibraryService;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private static Library instance;
    private final LibraryService database;
    private final List<LibraryObserver> observers;

    // Costruttore privato per il Singleton
    private Library() {
        this.database = new LibraryService();
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
        database.addTrack(track);

        this.sync();         // <-- Task 1.2.4: Richiamo della funzione di sincronizzazione
        notifyObservers();   // <-- Task 1.2.5: Notifica alla GUI
    }

    public void removeTrack(Track track) {
        database.removeTrack(track);

        this.sync();         // <-- Task 1.2.4: Richiamo della funzione di sincronizzazione
        notifyObservers();   // <-- Task 1.2.5: Notifica alla GUI
    }

    public List<Track> getTracks() {
        return database.getTracks();
    }
    // =========================================================
    // GESTIONE LABEL
    // =========================================================
    public void toggleFavourite(Track track) {
        track.setFavourite(!track.isFavourite());
        this.sync();           // 1. Salva su disco (Quando implementato)
        notifyObservers();     // 2. Avvisa le interfacce grafiche
    }
    public void toggleExplicit(Track track) {
        track.setExplicit(!track.isExplicit());
        this.sync();           // 1. Salva su disco (Quando implementato)
        notifyObservers();     // 2. Avvisa le interfacce grafiche
    }


    // =========================================================
    // FUNZIONE DI SINCRONIZZAZIONE (Task 1.2.4 / US 1.1)
    // =========================================================
    // Rinominata in sync() per essere già pronta per l'interfaccia Tracklist
    public void sync() {
        this.database.saveToFile();
        System.out.println("File JSON sovrascritto e aggiornato con i nuovi preferiti/espliciti!");
    }
}