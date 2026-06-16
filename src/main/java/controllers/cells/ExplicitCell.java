package controllers.cells;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import models.Library;
import models.Track;

/**
 * Cella della colonna "Explicit": mostra un badge "E" attivo/disattivo e
 * permette di alternare lo stato explicit della traccia con un click.
 */
public class ExplicitCell extends TableCell<Track, Boolean> {

    @Override
    protected void updateItem(Boolean isExplicit, boolean empty) {
        super.updateItem(isExplicit, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            Track currentTrack = getTableRow().getItem();
            Label explicitBadge = new Label("E");
            explicitBadge.setCursor(Cursor.HAND);

            String baseStyle = "-fx-background-color: -app-muted; -fx-text-fill: -app-bg; -fx-padding: 1 5 1 5; -fx-background-radius: 3; -fx-font-size: 10px;";

            if (currentTrack.isExplicit()) {
                explicitBadge.setStyle(baseStyle + "-fx-font-weight: bold; -fx-opacity: 1.0;");
            } else {
                explicitBadge.setStyle(baseStyle + "-fx-font-weight: normal; -fx-opacity: 0.4;");
            }

            explicitBadge.setOnMouseClicked(event -> Library.getInstance().toggleExplicit(currentTrack));

            setGraphic(explicitBadge);
        }
    }
}
