package emmepitre.com.emmepitre;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.Theme;
public class PlayerLauncher extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/views/libraryView.fxml")
            );

            Scene scene = new Scene(root, 800, 600);
            Theme.apply(scene); // applica tema e stylesheet prima del primo render

            primaryStage.setTitle("Media Player");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/app_icon.png")));
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); // avvio a schermo intero
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

