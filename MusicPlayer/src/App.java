import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        EventHandler<WindowEvent> closeEventHandler = event -> {
            event.consume();
            Controller.mediaPlayer.pause();
            Alert alert = new Alert(AlertType.ERROR, "Want to close App?", ButtonType.OK, ButtonType.CANCEL);
            alert.setTitle("Confirm Close");
            alert.setHeaderText("");
            alert.setResizable(false);
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    System.exit(0);
                }
            });
        };
        primaryStage.setOnCloseRequest(closeEventHandler);
        Parent root = FXMLLoader.load(getClass().getResource("Musicplayer.fxml"));
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(new Scene(root, 600.0, 241.0));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}