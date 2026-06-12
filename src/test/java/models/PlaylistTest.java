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

    @Test
    public void containsTrack_ShouldReturnFalse_WhenTrackNotPresent() {
        assertFalse(playlistTest.containsTrack(tracciaValida),
                "Una playlist vuota non contiene la traccia");
    }

    // =========================================================================
    // TEST RIORDINO TRACCE (reorderTracks)
    // =========================================================================

    @Test
    public void reorderTracks_ShouldChangeOrder_WhenNewOrderProvided() {
        Track a = new Track("p1", "A", "Art", "Alb", "Pop", 2020, false, false, 100);
        Track b = new Track("p2", "B", "Art", "Alb", "Pop", 2020, false, false, 100);
        Track c = new Track("p3", "C", "Art", "Alb", "Pop", 2020, false, false, 100);
        playlistTest.addTrack(a);
        playlistTest.addTrack(b);
        playlistTest.addTrack(c);

        // Inverte l'ordine: [a, b, c] -> [c, b, a]
        playlistTest.reorderTracks(java.util.List.of(c, b, a));

        assertAll("Verifica nuovo ordine e integrità",
                () -> assertEquals(3, playlistTest.getSize()),
                () -> assertEquals(c, playlistTest.getTracks().get(0)),
                () -> assertEquals(b, playlistTest.getTracks().get(1)),
                () -> assertEquals(a, playlistTest.getTracks().get(2))
        );
    }

    @Test
    public void reorderTracks_ShouldReplaceContent_WithProvidedList() {
        playlistTest.addTrack(tracciaValida);
        Track altra = new Track("p9", "Altra", "Art", "Alb", "Rock", 2021, false, false, 200);

        playlistTest.reorderTracks(java.util.List.of(altra));

        assertAll("La lista interna riflette esattamente il nuovo ordine passato",
                () -> assertEquals(1, playlistTest.getSize()),
                () -> assertTrue(playlistTest.containsTrack(altra)),
                () -> assertFalse(playlistTest.containsTrack(tracciaValida))
        );
    }

    // =========================================================================
    // TEST IDENTITÀ PLAYLIST (equals/hashCode basati sul nome)
    // =========================================================================

    @Test
    public void equals_ShouldReturnTrue_WhenNamesAreEqual() {
        Playlist stessoNome = new Playlist("Hit del Momento");
        assertEquals(playlistTest, stessoNome, "Due playlist con lo stesso nome sono uguali");
    }

    @Test
    public void equals_ShouldReturnFalse_WhenNamesDiffer() {
        Playlist altroNome = new Playlist("Relax");
        assertNotEquals(playlistTest, altroNome, "Nomi diversi rendono le playlist disuguali");
    }

    @Test
    public void hashCode_ShouldMatch_WhenNamesAreEqual() {
        Playlist stessoNome = new Playlist("Hit del Momento");
        assertEquals(playlistTest.hashCode(), stessoNome.hashCode(),
                "Playlist uguali (stesso nome) devono avere lo stesso hashCode");
    }
}