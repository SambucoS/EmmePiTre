package controllers.cells;

import javafx.scene.control.TableCell;
import models.Track;

/**
 * Cella della colonna "Length": formatta la durata della traccia da secondi
 * al formato mm:ss.
 *
 * @version 1.0
 */
public class DurationCell extends TableCell<Track, Integer> {

    /**
     * Ridisegna la cella mostrando la durata in formato mm:ss.
     *
     * @param totalSeconds la durata della traccia in secondi, oppure {@code null}
     * @param empty        {@code true} se la cella non corrisponde ad alcuna riga di dati
     */
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
