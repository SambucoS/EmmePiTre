package controllers.cells;

import java.util.function.Consumer;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import models.Playlist;

/**
 * Cella della sidebar delle playlist: mostra il nome della playlist e un menu
 * contestuale con "Modifica" ed "Elimina". Le azioni sono delegate al chiamante
 * tramite i due callback passati nel costruttore.
 */
public class PlaylistSidebarCell extends ListCell<Playlist> {

    private final Consumer<Playlist> onModify;
    private final Consumer<Playlist> onDelete;
    private final Consumer<Playlist> onPlay;


    public PlaylistSidebarCell(Consumer<Playlist> onModify, Consumer<Playlist> onDelete, Consumer<Playlist> onPlay) {
        this.onModify = onModify;
        this.onDelete = onDelete;
        this.onPlay = onPlay;
    }

    @Override
    protected void updateItem(Playlist playlist, boolean empty) {
        super.updateItem(playlist, empty);

        if (empty || playlist == null) {
            setText(null);
            setContextMenu(null);
        } else {

            Label playlistNameLbl = new Label(playlist.getName());

            Button playButton = new Button("▶");
            playButton.setOnAction(event -> onPlay.accept(playlist));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox container = new HBox(5);
            container.getChildren().addAll( playlistNameLbl, spacer, playButton);

            setText(null);
            setGraphic(container);


            MenuItem modifyItem = new MenuItem("Modifica playlist");
            MenuItem deleteItem = new MenuItem("Elimina playlist");

            modifyItem.setOnAction(event -> onModify.accept(playlist));
            deleteItem.setOnAction(event -> onDelete.accept(playlist));

            setContextMenu(new ContextMenu(modifyItem, deleteItem));
        }
    }
}
