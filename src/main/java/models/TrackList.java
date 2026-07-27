package models;

import models.Track;
import java.util.List;

/**
 * Rappresenta il contratto per la gestione di una collezione di tracce musicali.
 * Qualsiasi classe che implementa questa interfaccia deve fornire la logica per
 * inserire, rimuovere, visualizzare e contare i brani in essa contenuti.
 *
 * @version 1.0
 */
public interface TrackList {

    /**
     * Aggiunge una nuova traccia alla collezione.
     *
     * @param t la traccia {@link Track} da aggiungere; non deve essere null
     */
    void addTrack(Track t);

    /**
     * Rimuove una traccia specifica dalla collezione, se presente.
     *
     * @param t la traccia {@link Track} da rimuovere dalla lista
     */
    void removeTrack(Track t);

    /**
     * Restituisce l'elenco completo di tutte le tracce presenti nella collezione.
     *
     * @return una {@link List} contenente tutte le tracce salvate
     */
    List<Track> getTracks();

    /**
     * Restituisce il numero totale di tracce attualmente presenti nella collezione.
     *
     * @return il numero di elementi (intero) contenuti nella lista
     */
    int getSize();
}