package models;

import observer.LibraryObserver;
import persistence.JsonStorageService;
import java.util.ArrayList;
import java.util.List;

public class Library implements TrackList {

    private static Library instance;
    private final JsonStorageService database;
    private final List<LibraryObserver> observers;
    private final List<Track> tracks;

    // Costruttore privato per il Singleton
    private Library() {
        this.database = new JsonStorageService();
        this.observers = new ArrayList<>();

        //carica i dati dal JSON
        this.tracks = database.loadFromFile();
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    //Implementazione Observer per comunicare i cambiamenti
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

    // METODI CRUD -
    @Override
    public void addTrack(Track track) {
        this.tracks.add(track); //Aggiunge la traccia alla RAM
        this.sync();            //Salva sul disco
        notifyObservers();      //Aggiorna la GUI
    }

    @Override
    public void removeTrack(Track track) {
        this.tracks.remove(track); //Rimuove la traccia dalla RAM
        this.sync();               //Salva sul disco
        notifyObservers();         //Aggiorna la GUI
    }

    @Override
    public List<Track> getTracks() {
        return this.tracks; // Restituisce la lista in RAM
    }

    @Override
    public int getSize() {
        return this.tracks.size();
    }

    // GESTIONE LABEL
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

    public Track getTrackWithID(String id){

        List<Track> list = Library.getInstance().getTracks();

        Track found = list.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
        return found;
    }

    // FUNZIONE DI SINCRONIZZAZIONE
    public void sync() {
        // Passa la lista aggiornata al service per la scrittura su file
        this.database.saveToFile(this.tracks);
        System.out.println("File JSON sovrascritto e aggiornato con i nuovi dati!");
    }


}