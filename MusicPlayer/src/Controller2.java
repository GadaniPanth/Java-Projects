import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Controller2 {
    Parent root;
    Stage stage;
    Scene scene;

    @FXML
    private Button cancel;

    @FXML
    private Button insert;

    @FXML
    private Label myLable;

    @FXML
    private TextField text;

    @FXML
    void cancelMusic(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Musicplayer.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    void insertMusicWn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Musicplayer.fxml"));
        if (f1 == null) {
            Alert alert = new Alert(AlertType.ERROR, "File Not Selected", ButtonType.OK);
            alert.setHeaderText("");
            alert.setContentText("File Not Selected");
            alert.showAndWait();
            return;
        }
        if (f1.exists() && f1.isFile()) {
            Controller.insertLast(f1);
        } else {
            Alert alert1 = new Alert(AlertType.ERROR, "File Not Found", ButtonType.OK);
            alert1.setHeaderText("");
            alert1.setContentText("File Not Found");
            alert1.showAndWait();
            return;
        }
        root = loader.load();
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    static File f1;

    @FXML
    void onEnterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            insert.fire();
        }
    }

    @FXML
    void musicName(ActionEvent event) {
        try {
            f1 = new File("Addable\\" + text.getText() + ".mp3");
        } catch (Exception e) {

        }

    }
}