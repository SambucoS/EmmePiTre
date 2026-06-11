package models.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {

    private CommandManager manager;

    // Una classe "finta" usata solo per testare il manager
    private class DummyCommand implements Command {
        boolean isExecuted = false;

        @Override
        public void execute() { isExecuted = true; }

        @Override
        public void undo() { isExecuted = false; }
    }

    @BeforeEach
    void setUp() {
        // Recuperiamo il singleton per ogni test
        manager = CommandManager.getInstance();

        // ATTENZIONE: Poiché è un Singleton, lo stato rimane tra un test e l'altro.
        // Simuliamo uno svuotamento chiamando "undo" finché non è vuoto,
        // oppure idealmente potresti aggiungere un metodo manager.clear() nella tua classe per i test.
        while (manager.canUndo()) {
            manager.undo();
        }
        // Ora lo stack Undo è vuoto, ed eseguendo un nuovo comando vuoteremo il Redo.
        manager.executeCommand(new DummyCommand());
        manager.undo();
    }

    @Test
    void testExecuteCommandAddsToUndoStackAndClearsRedo() {
        DummyCommand cmd = new DummyCommand();

        // Eseguiamo il comando
        manager.executeCommand(cmd);

        assertTrue(cmd.isExecuted, "Il comando deve essere stato eseguito");
        assertTrue(manager.canUndo(), "Dopo aver eseguito un comando, devo poter fare Undo");
        assertFalse(manager.canRedo(), "Dopo un nuovo comando, lo stack di Redo deve essere vuoto");
    }

    @Test
    void testUndoRevertsActionAndMovesToRedoStack() {
        DummyCommand cmd = new DummyCommand();
        manager.executeCommand(cmd); // Lo esegue

        // Azione: Annulliamo
        manager.undo();

        assertFalse(cmd.isExecuted, "L'azione deve essere stata annullata");
        assertFalse(manager.canUndo(), "Lo stack di Undo dovrebbe essere vuoto ora");
        assertTrue(manager.canRedo(), "Il comando annullato deve essere nel Redo stack");
    }

    @Test
    void testRedoReappliesActionAndMovesToUndoStack() {
        DummyCommand cmd = new DummyCommand();
        manager.executeCommand(cmd);
        manager.undo();

        // Azione: Ripristiniamo
        manager.redo();

        assertTrue(cmd.isExecuted, "L'azione deve essere stata ripristinata");
        assertTrue(manager.canUndo(), "Il comando ripristinato torna nello stack di Undo");
        assertFalse(manager.canRedo(), "Lo stack di Redo torna vuoto");
    }
}