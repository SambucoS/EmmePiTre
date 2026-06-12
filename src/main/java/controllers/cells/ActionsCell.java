package controllers.cells;

import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;
import models.Track;

/**
 * Cella della colonna "azioni" (tre puntini): mostra/nasconde il menu
 * contestuale della riga al click sinistro, con effetto toggle.
 */
public class ActionsCell extends TableCell<Track, Void> {

    private final Label dotsLabel = new Label("⋮");

    public ActionsCell() {
        dotsLabel.setCursor(Cursor.HAND);
        dotsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #888888; -fx-padding: 0 5 0 5;");

        dotsLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                event.consume(); // Blocca il click per non farlo "rimbalzare" sulla riga sotto

                TableRow<Track> row = getTableRow();
                if (row != null && !row.isEmpty() && row.getContextMenu() != null) {
                    ContextMenu menu = row.getContextMenu(); // Recuperiamo il menu nativo della riga

                    // Se il menu è aperto, chiudilo (effetto toggle), altrimenti mostralo
                    if (menu.isShowing()) {
                        menu.hide();
                    } else {
                        menu.show(dotsLabel, Side.BOTTOM, 0, 0);
                    }
                }
            }
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
            setGraphic(null);
        } else {
            setGraphic(dotsLabel);
        }
    }
}
