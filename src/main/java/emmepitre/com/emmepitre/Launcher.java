package emmepitre.com.emmepitre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/views/libraryView.fxml"))
        );

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Music Library");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}