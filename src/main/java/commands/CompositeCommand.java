package commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite pattern applicato al Command pattern: raggruppa piu' comandi in
 * un'unica unita', anch'essa un {@link Command}, cosi' che Undo/Redo trattino
 * l'intero gruppo come un solo passo atomico. I comandi figli vengono
 * eseguiti nell'ordine di aggiunta e annullati nell'ordine inverso, in modo
 * simmetrico rispetto all'esecuzione.
 *
 * @version 1.0
 */
public class CompositeCommand implements Command {

    private final List<Command> children = new ArrayList<>();

    /**
     * Aggiunge un comando figlio alla composizione. I comandi {@code null}
     * vengono ignorati.
     *
     * @param command il {@link Command} da aggiungere
     * @return questa stessa istanza, per permettere chiamate concatenate
     */
    public CompositeCommand add(Command command) {
        if (command != null) {
            children.add(command);
        }
        return this;
    }

    /**
     * Esegue tutti i comandi figli nell'ordine in cui sono stati aggiunti.
     */
    @Override
    public void execute() {
        for (Command c : children) {
            c.execute();
        }
    }

    /**
     * Annulla tutti i comandi figli in ordine inverso rispetto all'esecuzione.
     */
    @Override
    public void undo() {
        for (int i = children.size() - 1; i >= 0; i--) {
            children.get(i).undo();
        }
    }
}
