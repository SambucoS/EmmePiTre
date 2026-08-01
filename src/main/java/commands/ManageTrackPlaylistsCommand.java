package commands;

import models.Playlist;
import models.Track;

import java.util.List;

/**
 * Comando (pattern Command) che assegna una singola traccia a piu' playlist
 * e/o la rimuove da altre in un'unica operazione atomica, cosi' come scelto
 * dall'utente nella modale "Aggiungi a playlist". Internamente compone un
 * {@link CompositeCommand} di {@link AddTrackToPlaylistCommand} e
 * {@link RemoveTrackFromPlaylistCommand}, cosi' che l'intero gruppo di
 * modifiche sia annullabile in un solo passo di Undo/Redo.
 *
 * @version 1.0
 */
public class ManageTrackPlaylistsCommand implements Command {

    private final CompositeCommand composite = new CompositeCommand();

    /**
     * Crea il comando con le playlist a cui aggiungere o da cui rimuovere la traccia.
     *
     * @param track    la {@link Track} coinvolta nella modifica
     * @param toAdd    le playlist a cui aggiungere la traccia
     * @param toRemove le playlist da cui rimuovere la traccia
     */
    public ManageTrackPlaylistsCommand(Track track, List<Playlist> toAdd, List<Playlist> toRemove) {
        for (Playlist p : toAdd) {
            composite.add(new AddTrackToPlaylistCommand(p, track));
        }
        for (Playlist p : toRemove) {
            composite.add(new RemoveTrackFromPlaylistCommand(p, track));
        }
    }

    /**
     * Aggiunge la traccia alle playlist di destinazione e la rimuove da quelle escluse.
     */
    @Override
    public void execute() {
        composite.execute();
    }

    /**
     * Inverte le operazioni: rimuove la traccia dalle playlist a cui era stata
     * aggiunta e la riaggiunge a quelle da cui era stata rimossa.
     */
    @Override
    public void undo() {
        composite.undo();
    }
}
