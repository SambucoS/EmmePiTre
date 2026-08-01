package commands;

import models.TrackList;
import models.Track;

/**
 * Comando (pattern Command) che rappresenta l'aggiunta di una traccia a un
 * generico {@link TrackList}. Il receiver puo' essere sia la libreria
 * principale sia una singola playlist, poiche' entrambe implementano lo
 * stesso contratto.
 *
 * @version 1.0
 */
public class AddTrackCommand implements Command {

    private final TrackList receiver; // Può essere la Library o una specifica Playlist!
    private final Track track;

    /**
     * Crea il comando per l'aggiunta della traccia al receiver indicato.
     *
     * @param receiver il {@link TrackList} (libreria o playlist) su cui operare
     * @param track    la {@link Track} da aggiungere
     */
    public AddTrackCommand(TrackList receiver, Track track) {
        this.receiver = receiver;
        this.track = track;
    }

    /**
     * Aggiunge la traccia al receiver.
     */
    @Override
    public void execute() {
        receiver.addTrack(track); // Il metodo "standard" che avevi già scritto
    }

    /**
     * Annulla l'aggiunta rimuovendo la traccia dal receiver.
     */
    @Override
    public void undo() {
        receiver.removeTrack(track); // Invertiamo l'operazione
    }
}
