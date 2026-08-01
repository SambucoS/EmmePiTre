package commands;

import java.util.Stack;

/**
 * Gestore centrale (Singleton) del pattern Command: tiene traccia della
 * cronologia dei comandi eseguiti e permette di annullarli (Undo) o di
 * ripristinarli (Redo) tramite due pile dedicate.
 *
 * @version 1.0
 */
public class CommandManager {

    private static CommandManager instance;

    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    /**
     * Costruttore privato del Singleton: inizializza le pile di Undo e Redo
     * vuote. L'istanza va ottenuta tramite {@link #getInstance()}.
     */
    private CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Restituisce l'unica istanza del gestore, creandola al primo utilizzo.
     *
     * @return l'istanza {@link CommandManager} condivisa dall'applicazione
     */
    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    /**
     * Esegue un nuovo comando e lo registra nella cronologia degli Undo.
     * Svuota inoltre lo stack dei Redo, poiche' una nuova azione invalida
     * qualsiasi ramo di ripristino precedentemente accumulato.
     *
     * @param command il {@link Command} da eseguire e mettere in cronologia
     */
    public void executeCommand(Command command) {
        command.execute();           // Esegue fisicamente l'azione
        undoStack.push(command);     // La salva nella cronologia
        redoStack.clear();           // Svuota i Redo (se fai una nuova azione, perdi il ramo dei Redo)
    }

    /**
     * Annulla l'ultimo comando eseguito, se presente, spostandolo nello
     * stack dei Redo cosi' da poter essere eventualmente ripristinato.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop(); // Tira fuori l'ultima azione
            command.undo();                    // La inverte
            redoStack.push(command);           // La sposta nei Redo pronti per essere ripristinati
        }
    }

    /**
     * Ripristina l'ultimo comando annullato, se presente, rieseguendolo e
     * rimettendolo nello stack degli Undo.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop(); // Tira fuori l'ultima azione annullata
            command.execute();                 // La riesegue
            undoStack.push(command);           // La rimette nella cronologia normale
        }
    }

    // --- Metodi Helper per la Grafica (per attivare/disattivare i bottoni) ---

    /**
     * Indica se esiste almeno un comando annullabile.
     *
     * @return {@code true} se lo stack degli Undo non e' vuoto
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Indica se esiste almeno un comando ripristinabile.
     *
     * @return {@code true} se lo stack dei Redo non e' vuoto
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
