package controllers.cells;

import java.util.function.Consumer;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import models.Playlist;

/**
 * Cella della sidebar delle playlist: mostra il nome della playlist e un menu
 * contestuale con "Modifica" ed "Elimina". Le azioni sono delegate al chiamante
 * tramite i due callback passati nel costruttore.
 */
public class PlaylistSidebarCell extends ListCell<Playlist> {

    private final Consumer<Playlist> onModify;
    private final Consumer<Playlist> onDelete;

    public PlaylistSidebarCell(Consumer<Playlist> onModify, Consumer<Playlist> onDelete) {
        this.onModify = onModify;
        this.onDelete = onDelete;
    }

    @Override
    protected void updateItem(Playlist playlist, boolean empty) {
        super.updateItem(playlist, empty);

        if (empty || playlist == null) {
            setText(null);
            setContextMenu(null);
        } else {
            // Mostra nella sidebar solo il nome della playlist.
            setText(playlist.getName());

            MenuItem modifyItem = new MenuItem("Modifica playlist");
            MenuItem deleteItem = new MenuItem("Elimina playlist");

            modifyItem.setOnAction(event -> onModify.accept(playlist));
            deleteItem.setOnAction(event -> onDelete.accept(playlist));

            setContextMenu(new ContextMenu(modifyItem, deleteItem));
        }
    }
}
