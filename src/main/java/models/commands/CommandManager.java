package models.commands;

import java.util.Stack;

public class CommandManager {

    private static CommandManager instance;

    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    private CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    // 1. Esecuzione di un NUOVO comando
    public void executeCommand(Command command) {
        command.execute();           // Esegue fisicamente l'azione
        undoStack.push(command);     // La salva nella cronologia
        redoStack.clear();           // Svuota i Redo (se fai una nuova azione, perdi il ramo dei Redo)
    }

    // 2. Annulla
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop(); // Tira fuori l'ultima azione
            command.undo();                    // La inverte
            redoStack.push(command);           // La sposta nei Redo pronti per essere ripristinati
        }
    }

    // 3. Ripristina
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop(); // Tira fuori l'ultima azione annullata
            command.execute();                 // La riesegue
            undoStack.push(command);           // La rimette nella cronologia normale
        }
    }

    // --- Metodi Helper per la Grafica (per attivare/disattivare i bottoni) ---

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}