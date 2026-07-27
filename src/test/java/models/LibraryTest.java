package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari del Singleton {@link Library}: aggiunta e rimozione di
 * tracce e robustezza nel caso di rimozione di una traccia non presente.
 *
 * @version 1.0
 */
class LibraryTest {

    private Library library;
    private Track track1;

    @BeforeEach
    void setUp() {
        library = Library.getInstance();
        track1 = new Track("path1", "Title 1", "Artist 1", "Album 1", "Rock", 2000, false, false, 200);
    }

    @Test
    void testAddTrack() {
        int initialSize = library.getTracks().size();

        library.addTrack(track1);

        assertEquals(initialSize + 1, library.getTracks().size(), "La dimensione della libreria deve incrementare di 1");
        assertTrue(library.getTracks().contains(track1), "La traccia deve essere presente nella lista");

        // Clean-up
        library.removeTrack(track1);
    }

    @Test
    void testRemoveTrack() {
        // Assicuriamoci che la traccia sia presente
        library.addTrack(track1);
        int sizeAfterAdd = library.getTracks().size();

        // Rimozione
        library.removeTrack(track1);

        assertEquals(sizeAfterAdd - 1, library.getTracks().size(), "La dimensione della libreria deve diminuire di 1");
        assertFalse(library.getTracks().contains(track1), "La traccia non deve più essere presente");
    }

    @Test
    void testRemoveNonExistentTrackDoesNotThrowException() {
        Track ghostTrack = new Track("ghost", "Ghost", "Ghost", "Ghost", "Pop", 2026, false, false, 100);
        int initialSize = library.getTracks().size();

        // Chiamare la rimozione di un brano non esistente non dovrebbe far crashare il programma
        // e non dovrebbe alterare la dimensione della lista
        assertDoesNotThrow(() -> library.removeTrack(ghostTrack));
        assertEquals(initialSize, library.getTracks().size());
    }
}