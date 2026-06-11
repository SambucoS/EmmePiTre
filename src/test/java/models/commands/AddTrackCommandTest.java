package models.commands;

import models.Library;
import models.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddTrackCommandTest {

    private Library library;
    private Track testTrack;
    private AddTrackCommand addCommand;

    @BeforeEach
    void setUp() {
        library = Library.getInstance();
        // Creiamo una traccia fittizia per il test
        testTrack = new Track("percorso/test.mp3", "Test Song", "Test Artist", "Test Album", "Pop", 2026, false, false, 180);
        addCommand = new AddTrackCommand(library, testTrack);
    }

    @Test
    void testExecuteAddsTrackToLibrary() {
        // Calcoliamo la dimensione iniziale (nel caso ci siano già brani di default)
        int initialSize = library.getTracks().size();

        // Eseguiamo il comando
        addCommand.execute();

        assertEquals(initialSize + 1, library.getTracks().size(), "La libreria dovrebbe avere una traccia in più");
        assertTrue(library.getTracks().contains(testTrack), "La libreria deve contenere la traccia appena aggiunta");

        // Clean-up per non sporcare altri test
        library.removeTrack(testTrack);
    }

    @Test
    void testUndoRemovesTrackFromLibrary() {
        // Preparazione: Aggiungiamo prima la traccia
        addCommand.execute();
        assertTrue(library.getTracks().contains(testTrack));

        // Esecuzione dell'Undo
        addCommand.undo();

        assertFalse(library.getTracks().contains(testTrack), "L'undo deve aver rimosso la traccia");
    }
}