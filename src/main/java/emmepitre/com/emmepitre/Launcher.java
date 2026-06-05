package emmepitre.com.emmepitre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Carica il file FXML della modale
            // Nota: Assicurati che il percorso rifletta la posizione reale del tuo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/managePlaylist.fxml"));
            VBox root = loader.load();

            // 2. Crea la scena con il nodo radice caricato
            Scene scene = new Scene(root);

            // 3. Configura lo stage (la finestra)
            primaryStage.setTitle("Test Modale - Aggiungi Traccia");
            primaryStage.setScene(scene);

            // Impedisce il ridimensionamento della finestra per mantenere il form pulito
            primaryStage.setResizable(false);

            // 4. Mostra la finestra
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Errore durante l'avvio della modale di test:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX
        launch(args);
    }
}