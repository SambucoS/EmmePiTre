package commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import models.Playlist;
import models.Track;

/**
 * Test di AddTrackCommand (Command pattern).
 *
 * Come receiver {@link models.TrackList} si usa una {@link Playlist}: è un
 * TrackList puro, in memoria, senza I/O su disco (a differenza di Library),
 * quindi il comando è testabile in isolamento.
 *
 * @version 1.0
 */
public class AddTrackCommandTest {

    private Playlist receiver;
    private Track track;

    @BeforeEach
    void setUp() {
        receiver = new Playlist("Receiver");
        track = new Track("path", "Song", "Artist", "Album", "Pop", 2024, false, false, 180);
    }

    @Test
    public void execute_ShouldAddTrackToReceiver() {
        Command cmd = new AddTrackCommand(receiver, track);
        cmd.execute();

        assertAll("Dopo execute la traccia è nel receiver",
                () -> assertEquals(1, receiver.getSize()),
                () -> assertTrue(receiver.containsTrack(track))
        );
    }

    @Test
    public void undo_ShouldRemoveTrackFromReceiver() {
        Command cmd = new AddTrackCommand(receiver, track);
        cmd.execute();
        cmd.undo();

        assertAll("Dopo undo la traccia non è più nel receiver",
                () -> assertEquals(0, receiver.getSize()),
                () -> assertFalse(receiver.containsTrack(track))
        );
    }

    @Test
    public void executeThenUndo_ShouldRestoreInitialState() {
        Command cmd = new AddTrackCommand(receiver, track);
        assertTrue(receiver.isEmpty(), "Precondizione: receiver vuoto");

        cmd.execute();
        cmd.undo();

        assertTrue(receiver.isEmpty(), "execute seguito da undo deve ripristinare lo stato iniziale");
    }
}
