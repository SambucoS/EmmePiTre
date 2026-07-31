package commands;

/**
 * Rappresenta il contratto del pattern Command per le azioni annullabili
 * dell'applicazione. Ogni azione dell'utente che modifica lo stato (libreria,
 * playlist, tracce) viene incapsulata in un'implementazione di questa
 * interfaccia, cosi' da poter essere eseguita e successivamente annullata in
 * modo uniforme dal {@link CommandManager}.
 *
 * @version 1.0
 */
public interface Command {

    /**
     * Esegue materialmente l'azione rappresentata dal comando.
     */
    void execute();

    /**
     * Annulla l'azione precedentemente eseguita da {@link #execute()},
     * riportando lo stato coinvolto alla condizione antecedente.
     */
    void undo();
}
