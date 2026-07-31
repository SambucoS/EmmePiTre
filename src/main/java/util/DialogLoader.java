package util;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Helper per il caricamento di finestre modali da file FXML.
 * Centralizza il boilerplate ripetuto (FXMLLoader + Stage modale + Scene +
 * showAndWait), così i controller chiamanti restano concisi.
 *
 * @version 1.0
 */
public final class DialogLoader {

    private DialogLoader() {
    }

    /**
     * Carica un FXML in una finestra modale bloccante.
     *
     * @param fxmlPath    percorso assoluto della risorsa FXML (es. "/views/x.fxml")
     * @param title       titolo della finestra
     * @param width       larghezza della scena
     * @param height      altezza della scena
     * @param initializer azione opzionale per configurare il controller PRIMA che
     *                    la finestra venga mostrata (può essere null)
     * @param <C>         tipo del controller associato all'FXML
     * @return il controller dell'FXML caricato, oppure null se il caricamento fallisce
     */
    public static <C> C showModal(String fxmlPath, String title, double width, double height,
                                  Consumer<C> initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogLoader.class.getResource(fxmlPath));
            Parent root = loader.load();

            C controller = loader.getController();
            if (initializer != null) {
                initializer.accept(controller);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(root, width, height);
            Theme.apply(scene); // la modale segue il tema corrente (chiaro/scuro)
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore durante il caricamento di " + fxmlPath);
            return null;
        }
    }
}
