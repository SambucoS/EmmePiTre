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
    public void constructor_ShouldGenerateNonNullId() {
        assertNotNull(trackBase.getId(), "Il costruttore deve generare un id non nullo");
    }

    @Test
    public void constructor_ShouldStartWithZeroTimesListened() {
        assertEquals(0, trackBase.getTimesListened(), "Una nuova traccia parte da 0 ascolti");
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
    public void setTimesListened_ShouldIncrementByOne() {
        trackBase.setTimesListened();
        trackBase.setTimesListened();
        assertEquals(2, trackBase.getTimesListened(), "Ogni chiamata incrementa il contatore di 1");
    }

    // =========================================================================
    // 2. IDENTITÀ (equals/hashCode basati sull'id univoco della traccia)
    // =========================================================================
    // Nota: il costruttore genera un id UUID univoco e non esiste un setter per
    // l'id, quindi due tracce costruite separatamente non sono mai uguali.
    // L'uguaglianza coincide di fatto con l'identità (stesso id).

    @Test
    public void equals_ShouldReturnTrue_WhenSameInstance() {
        assertTrue(trackBase.equals(trackBase), "Una traccia deve essere uguale a se stessa (riflessività)");
    }

    @Test
    public void equals_ShouldReturnFalse_WhenDistinctInstancesEvenWithSameFields() {
        // Stessi campi ma id diverso (generato dal costruttore) => non uguali
        Track trackGemella = new Track("C:/music/song1.mp3", "Canzone Figa", "Artista Famoso", "Album1", "Pop", 2023, true, true, 215);
        assertNotEquals(trackBase, trackGemella, "Due tracce distinte hanno id diversi e non devono essere uguali");
    }

    @Test
    public void equals_ShouldReturnFalse_WhenComparedToNull() {
        assertNotEquals(null, trackBase, "Una traccia non deve mai essere uguale a null");
    }

    @Test
    public void equals_ShouldReturnFalse_WhenComparedToDifferentType() {
        assertNotEquals("non una traccia", trackBase, "Una traccia non deve essere uguale a un oggetto di altro tipo");
    }

    @Test
    public void equals_ShouldNotThrow_WhenFieldsAreNull() {
        // Robustezza: i campi null non devono causare NullPointerException nel confronto
        Track trackNull1 = new Track(null, null, null, null, null, 2023, true, true, 215);
        Track trackNull2 = new Track(null, null, null, null, null, 2023, true, true, 215);
        assertAll("Il confronto con campi null non deve sollevare eccezioni",
                () -> assertDoesNotThrow(() -> trackNull1.equals(trackNull2)),
                () -> assertTrue(trackNull1.equals(trackNull1), "Riflessività valida anche con campi null"),
                () -> assertNotEquals(trackNull1, trackNull2, "id diversi => non uguali anche con campi null")
        );
    }

    @Test
    public void hashCode_ShouldBeConsistent_AcrossMultipleCalls() {
        assertEquals(trackBase.hashCode(), trackBase.hashCode(), "hashCode deve essere stabile per la stessa istanza");
    }

    @Test
    public void hashCode_ShouldBeConsistentWithEquals_ForSameInstance() {
        Track stessoRiferimento = trackBase;
        assertTrue(trackBase.equals(stessoRiferimento));
        assertEquals(trackBase.hashCode(), stessoRiferimento.hashCode(),
                "Oggetti uguali devono avere lo stesso hashCode");
    }
}
