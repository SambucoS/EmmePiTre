package controllers.cells;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import models.Library;
import models.Track;

/**
 * Cella della colonna "Preferiti": mostra una stella (piena/vuota) e permette
 * di alternare lo stato preferito della traccia con un click.
 */
public class FavouriteCell extends TableCell<Track, Boolean> {

    @Override
    protected void updateItem(Boolean isFav, boolean empty) {
        super.updateItem(isFav, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            Track currentTrack = getTableRow().getItem();
            Label starLabel = new Label();
            starLabel.setCursor(Cursor.HAND);

            if (currentTrack.isFavourite()) {
                starLabel.setText("★");
                starLabel.setStyle("-fx-text-fill: -app-accent; -fx-font-size: 18px;");
            } else {
                starLabel.setText("☆");
                starLabel.setStyle("-fx-text-fill: -app-muted; -fx-font-size: 18px;");
            }

            starLabel.setOnMouseClicked(event -> Library.getInstance().toggleFavourite(currentTrack));

            setGraphic(starLabel);
        }
    }
}
