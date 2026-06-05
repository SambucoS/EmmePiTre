package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrackTest {

    private Track trackBase;

    @BeforeEach
    void setUp() {
        // Setup della classe di equivalenza: Oggetto Valido (Stato iniziale stabile)
        trackBase = new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album1", "Pop", 2023, true, true, 215);
    }

    // =========================================================================
    // 1. HAPPY PATH TESTS (Verifica delle funzionalità nel cammino principale)
    // =========================================================================

    @Test
    public void constructor_ShouldInitializeFieldsCorrectly_WhenDataIsValid() {
        assertAll("Verifica inizializzazione costruttore completo",
                () -> assertEquals("C:/music/song1.mp3", trackBase.getPathname()),
                () -> assertEquals("Canzone Figa", trackBase.getName()),
                () -> assertEquals("Artista Famoso", trackBase.getArtist()),
                () -> assertEquals("Album1", trackBase.getAlbum()),
                () -> assertEquals("Pop", trackBase.getGenre()),
                () -> assertEquals(2023, trackBase.getYear()),
                () -> assertTrue(trackBase.isFavourite()),
                () -> assertTrue(trackBase.isExplicit()),
                () -> assertEquals(215, trackBase.getDuration())
        );
    }

    @Test
    public void setters_ShouldModifyStateCorrectly_WhenNewValuesArePassed() {
        trackBase.setName("Nuovo Titolo");
        trackBase.setYear(2026);
        trackBase.setFavourite(false);

        assertAll("Verifica mutamento dello stato tramite setter",
                () -> assertEquals("Nuovo Titolo", trackBase.getName()),
                () -> assertEquals(2026, trackBase.getYear()),
                () -> assertFalse(trackBase.isFavourite())
        );
    }

    @Test
    public void equals_ShouldReturnTrue_WhenTracksAreLogicallyIdentical() {
        Track trackIdentica = new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album1", "Pop", 2023, true, true, 215);
        assertTrue(trackBase.equals(trackIdentica), "Due tracce con gli stessi identici campi devono essere uguali");
    }

    // =========================================================================
    // 2. EXCEPTION & ROBUSTEZZA TESTS (Cammini alternativi e Valori Limite)
    // =========================================================================

    @Test
    public void equals_ShouldReturnFalse_WhenYearIsDifferent() {
        // Valore limite (BVA): stessa traccia ma anno modificato di 1 unità
        Track trackModificata = new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album1", "Pop", 2024, true, true, 215);
        assertFalse(trackBase.equals(trackModificata), "L'anno differente deve rendere le tracce disuguali");
    }

    @Test
    public void equals_ShouldReturnFalse_WhenDurationIsDifferent() {
        // Valore limite (BVA): stessa traccia ma durata modificata di 1 secondo
        Track trackModificata = new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album1", "Pop", 2023, true, true, 216);
        assertFalse(trackBase.equals(trackModificata), "La durata differente deve rendere le tracce disuguali");
    }

    @Test
    public void equals_ShouldReturnFalse_WhenStringsAreDifferent() {
        // Classe di equivalenza input non valido per il confronto
        Track trackModificata = new Track("C:/music/song1.mp3", "Canzone Diversa", "Artista Famoso", "Album1", "Pop", 2023, true, true, 215);
        assertFalse(trackBase.equals(trackModificata), "Il nome differente deve rendere le tracce disuguali");
    }

    @Test
    public void equals_ShouldNotCrash_WhenFieldsAreNull() {
        // Gestione della robustezza in caso di valori nulli latenti (No crash atteso)
        Track trackNull1 = new Track(null, null, null, null, null, 2023, true, true, 215);
        Track trackNull2 = new Track(null, null, null, null, null, 2023, true, true, 215);
        assertTrue(trackNull1.equals(trackNull2), "Il metodo deve gestire i campi null senza sollevare NullPointerException");
    }
}