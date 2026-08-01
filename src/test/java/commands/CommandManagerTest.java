package commands;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test del CommandManager (Command pattern, gestione Undo/Redo).
 *
 * Il CommandManager è un Singleton con stack condivisi tra i test: per questo
 * le asserzioni sono scritte per essere indipendenti dall'ordine di esecuzione,
 * basandosi su contatori per-istanza dei comandi fittizi e su invarianti sempre
 * vere (es. executeCommand svuota sempre lo stack di redo).
 *
 * @version 1.0
 */
public class CommandManagerTest {

    /** Comando fittizio che conta quante volte viene eseguito/annullato. */
    private static class DummyCommand implements Command {
        int executed = 0;
        int undone = 0;

        @Override
        public void execute() {
            executed++;
        }

        @Override
        public void undo() {
            undone++;
        }
    }

    private final CommandManager cm = CommandManager.getInstance();

    @Test
    public void executeCommand_ShouldRunExecuteAndEnableUndo() {
        DummyCommand c = new DummyCommand();
        cm.executeCommand(c);

        assertAll(
                () -> assertEquals(1, c.executed, "execute() deve essere invocato una volta"),
                () -> assertEquals(0, c.undone),
                () -> assertTrue(cm.canUndo(), "Dopo un comando deve essere possibile annullare")
        );
    }

    @Test
    public void undo_ShouldRunUndoAndEnableRedo() {
        DummyCommand c = new DummyCommand();
        cm.executeCommand(c);
        cm.undo();

        assertAll(
                () -> assertEquals(1, c.undone, "undo() deve essere invocato una volta"),
                () -> assertTrue(cm.canRedo(), "Dopo un undo deve essere possibile ripristinare")
        );
    }

    @Test
    public void redo_ShouldReExecuteCommand() {
        DummyCommand c = new DummyCommand();
        cm.executeCommand(c);
        cm.undo();
        cm.redo();

        assertAll(
                () -> assertEquals(2, c.executed, "redo() deve rieseguire il comando"),
                () -> assertEquals(1, c.undone),
                () -> assertTrue(cm.canUndo())
        );
    }

    @Test
    public void executeCommand_ShouldClearRedoStack() {
        DummyCommand a = new DummyCommand();
        cm.executeCommand(a);
        cm.undo();
        assertTrue(cm.canRedo(), "Precondizione: c'è un comando da ripristinare");

        // Una nuova azione deve scartare il ramo di redo
        cm.executeCommand(new DummyCommand());
        assertFalse(cm.canRedo(), "Un nuovo comando deve svuotare lo stack di redo");
    }

    @Test
    public void undo_ShouldDoNothing_WhenUndoStackIsEmpty() {
        // Svuota lo stack di undo (sposta gli eventuali comandi residui nel redo)
        while (cm.canUndo()) {
            cm.undo();
        }
        assertFalse(cm.canUndo());

        assertDoesNotThrow(cm::undo, "undo() su stack vuoto non deve sollevare eccezioni");
        assertFalse(cm.canUndo());
    }

    @Test
    public void redo_ShouldDoNothing_WhenRedoStackIsEmpty() {
        // executeCommand svuota sempre il redo: garantisce la precondizione
        cm.executeCommand(new DummyCommand());
        assertFalse(cm.canRedo());

        assertDoesNotThrow(cm::redo, "redo() su stack vuoto non deve sollevare eccezioni");
        assertFalse(cm.canRedo());
    }
}
