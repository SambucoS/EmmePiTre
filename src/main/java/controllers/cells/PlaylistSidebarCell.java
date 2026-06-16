package controllers.cells;

import java.util.function.Consumer;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import models.Playlist;
import models.PlaylistManager;

/**
 * Cella della sidebar delle playlist: mostra il nome della playlist (con coroncina
 * se e' la piu' ascoltata) e un menu contestuale con "Modifica" ed "Elimina".
 * Il doppio click sul nome avvia la riproduzione della playlist.
 * Le azioni sono delegate al chiamante tramite i callback passati nel costruttore.
 */
public class PlaylistSidebarCell extends ListCell<Playlist> {

    private final Consumer<Playlist> onModify;
    private final Consumer<Playlist> onDelete;
    private final Consumer<Playlist> onPlay;

    public PlaylistSidebarCell(Consumer<Playlist> onModify, Consumer<Playlist> onDelete, Consumer<Playlist> onPlay) {
        this.onModify = onModify;
        this.onDelete = onDelete;
        this.onPlay = onPlay;

        // Doppio click sul nome della playlist -> avvia la riproduzione
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY
                    && event.getClickCount() == 2
                    && !isEmpty() && getItem() != null) {
                onPlay.accept(getItem());
            }
        });
    }

    @Override
    protected void updateItem(Playlist playlist, boolean empty) {
        super.updateItem(playlist, empty);
        setGraphic(null);

        if (empty || playlist == null) {
            setText(null);
            setContextMenu(null);
        } else {
            // Mostra il nome della playlist, con coroncina se e' la piu' ascoltata
            if (PlaylistManager.getInstance().isMostListenedPlaylist(playlist)) {
                setText(playlist.getName() + " 👑");
            } else {
                setText(playlist.getName());
            }

            MenuItem modifyItem = new MenuItem("Modifica playlist");
            MenuItem deleteItem = new MenuItem("Elimina playlist");

            modifyItem.setOnAction(event -> onModify.accept(playlist));
            deleteItem.setOnAction(event -> onDelete.accept(playlist));

            setContextMenu(new ContextMenu(modifyItem, deleteItem));
        }
    }
}
