package emmepitre.com.emmepitre;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class PlayerLauncher extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/views/libraryView.fxml"))
            );

            Scene scene = new Scene(root, 600, 240);

            primaryStage.setTitle("Media Player");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

