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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/createPlaylist.fxml"));
            VBox root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Test Modale - Crea Playlist");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Errore durante l'avvio della modale di test:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}