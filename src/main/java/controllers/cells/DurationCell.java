package controllers.cells;

import javafx.scene.control.TableCell;
import models.Track;

/**
 * Cella della colonna "Length": formatta la durata della traccia da secondi
 * al formato mm:ss.
 */
public class DurationCell extends TableCell<Track, Integer> {

    @Override
    protected void updateItem(Integer totalSeconds, boolean empty) {
        super.updateItem(totalSeconds, empty);
        if (empty || totalSeconds == null) {
            setText(null);
        } else {
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            setText(String.format("%02d:%02d", minutes, seconds));
        }
    }
}
