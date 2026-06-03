package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    private Playlist playlistTest;
    private Track tracciaValida;

    @BeforeEach
    void setUp() {
        playlistTest = new Playlist("Hit del Momento");
        tracciaValida = new Track("path", "Song", "Artist", "Album", "Pop", 2026, false, false, 180);
    }

    // =========================================================================
    // TEST VALORI LIMITE SUL NOME (Boundary Value Analysis / Exception Testing)
    // =========================================================================

    @Test
    public void constructor_ShouldInitializeCorrectly_WhenNameIsValid() {
        assertEquals("Hit del Momento", playlistTest.getName());
        assertTrue(playlistTest.isEmpty());
    }

    @Test
    public void constructor_ShouldThrowIllegalArgumentException_WhenNameIsEmpty() {
        // Valore limite: stringa vuota di lunghezza 0
        assertThrows(IllegalArgumentException.class, () -> new Playlist(""),
                "La stringa vuota deve sollevare un'eccezione");
    }

    @Test
    public void constructor_ShouldThrowIllegalArgumentException_WhenNameContainsOnlySpaces() {
        // Valore limite: stringa di soli spazi (il trim() la riduce a vuota)
        assertThrows(IllegalArgumentException.class, () -> new Playlist("     "),
                "Una stringa di soli spazi deve sollevare un'eccezione");
    }

    @Test
    public void constructor_ShouldThrowIllegalArgumentException_WhenNameIsNull() {
        // Valore limite: puntatore nullo
        assertThrows(IllegalArgumentException.class, () -> new Playlist(null),
                "Il valore null deve sollevare un'eccezione");
    }

    @Test
    public void setName_ShouldTrimSpaces_WhenNameHasOuterSpaces() {
        // Classe di equivalenza: stringa valida con spazi esterni superflui
        playlistTest.setName("   Rock e Metal   ");
        assertEquals("Rock e Metal", playlistTest.getName(), "I caratteri di spaziatura esterni devono essere rimossi");
    }

    // =========================================================================
    // TEST GESTIONE TRACCE (Stato della Lista e Robustezza)
    // =========================================================================

    @Test
    public void addTrack_ShouldIncreaseSize_WhenTrackIsValid() {
        playlistTest.addTrack(tracciaValida);

        assertAll("Verifica inserimento traccia ed evoluzione stato",
                () -> assertEquals(1, playlistTest.getSize()),
                () -> assertTrue(playlistTest.containsTrack(tracciaValida)),
                () -> assertFalse(playlistTest.isEmpty())
        );
    }

    @Test
    public void addTrack_ShouldThrowIllegalArgumentException_WhenTrackIsNull() {
        // Valore limite: inserimento elemento nullo
        assertThrows(IllegalArgumentException.class, () -> playlistTest.addTrack(null),
                "L'inserimento di una traccia null deve essere bloccato");
    }

    @Test
    public void removeTrack_ShouldDecreaseSize_WhenTrackIsPresent() {
        playlistTest.addTrack(tracciaValida);
        playlistTest.removeTrack(tracciaValida);

        assertAll("Verifica rimozione ed evoluzione stato",
                () -> assertEquals(0, playlistTest.getSize()),
                () -> assertFalse(playlistTest.containsTrack(tracciaValida)),
                () -> assertTrue(playlistTest.isEmpty())
        );
    }

    @Test
    public void removeTrack_ShouldThrowIllegalArgumentException_WhenTrackIsNull() {
        assertThrows(IllegalArgumentException.class, () -> playlistTest.removeTrack(null),
                "La rimozione di una traccia null deve essere bloccata");
    }

    // =========================================================================
    // TEST INCAPSULAMENTO (Sicurezza del Modello)
    // =========================================================================

    @Test
    public void getTracks_ShouldReturnImmutableList_ToPreventExternalModifications() {
        playlistTest.addTrack(tracciaValida);

        // Verifica del vincolo architetturale: la lista esterna non deve permettere alterazioni
        assertThrows(UnsupportedOperationException.class, () -> {
            playlistTest.getTracks().add(new Track());
        }, "La lista restituita deve essere rigidamente non modificabile per mantenere l'incapsulamento");
    }
}