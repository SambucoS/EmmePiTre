package models.commands;

import interfaces.TrackList;
import models.Track;

public class AddTrackCommand implements Command {

    private final TrackList receiver; // Può essere la Library o una specifica Playlist!
    private final Track track;

    public AddTrackCommand(TrackList receiver, Track track) {
        this.receiver = receiver;
        this.track = track;
    }

    @Override
    public void execute() {
        receiver.addTrack(track); // Il metodo "standard" che avevi già scritto
    }

    @Override
    public void undo() {
        receiver.removeTrack(track); // Invertiamo l'operazione
    }
}