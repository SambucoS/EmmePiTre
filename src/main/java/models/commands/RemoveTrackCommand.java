package models.commands;

import interfaces.TrackList;
import models.Track;

public class RemoveTrackCommand implements Command {

    private final TrackList receiver;
    private final Track track;

    public RemoveTrackCommand(TrackList receiver, Track track) {
        this.receiver = receiver;
        this.track = track;
    }

    @Override
    public void execute() {
        receiver.removeTrack(track); // Il metodo "standard"
    }

    @Override
    public void undo() {
        receiver.addTrack(track); // Invertiamo l'operazione
    }
}