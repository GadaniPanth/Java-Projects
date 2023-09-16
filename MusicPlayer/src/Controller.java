import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;

public class Controller implements Initializable {
    static ArrayList<File> musicCollection = new ArrayList<>();
    int songNumber;
    int[] speed = { 25, 50, 75, 100, 125, 150, 175, 200 };
    Timer timer;
    TimerTask task;
    boolean running;
    Media media;
    static MediaPlayer mediaPlayer;
    ActionEvent event;
    static File directory;
    static File[] files;
    Parent root;
    Stage stage;
    Scene scene;
    Controller2 controller2;

    @FXML
    Button deleteButton;

    @FXML
    Button insertButton;

    @FXML
    Label musicLabel, nextSongDisplay;

    @FXML
    ProgressBar musicProgress;

    @FXML
    Button nextButton;

    @FXML
    AnchorPane pane;

    @FXML
    Button pauseButton;

    @FXML
    Button playButton;

    @FXML
    Button prevButton;

    @FXML
    ComboBox<String> speedBox;

    @FXML
    Slider volumeMusic;

    @FXML
    void volume(MouseEvent event) {
        volumeMusic.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeMusic.getValue() * 0.005);
            }

        });

    }

    void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                try {
                    running = true;
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = mediaPlayer.getTotalDuration().toSeconds();
                    musicProgress.setProgress(current / end);
                    if (current / end == 1) {
                        cancelTimer();
                    }
                } catch (Exception e) {
                    timer.cancel();
                }
            }

        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    void cancelTimer() {
        running = false;
        try {
            timer.cancel();
        } catch (Exception e) {
            return;
        }
    }

    @FXML
    void changeSpeed(ActionEvent event) {
        if (speedBox.getValue() == null) {
            mediaPlayer.setRate(1);
            return;
        }
        mediaPlayer
                .setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
    }

    @FXML
    void deleteMusic(ActionEvent event) throws Exception {
        if (musicCollection.isEmpty()) {
            musicLabel.setText("Player is Empty");
            return;
        }
        mediaPlayer.stop();
        File delFile = musicCollection.get(songNumber);
        if (delFile.exists()) {
            musicCollection.remove(delFile);
            songNumber--;
            nextMusic(event);
        }
    }

    @FXML
    void insertMusic(ActionEvent event) throws Exception {
        mediaPlayer.stop();
        root = FXMLLoader.load(getClass().getResource("Musicplayer2.fxml"));
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        lastElement();
    }

    @FXML
    void nextMusic(ActionEvent event) {
        this.event = event;
        if (musicCollection.isEmpty()) {
            musicLabel.setText("Player is Empty");
            return;
        }
        mediaPlayer.stop();
        if (running) {
            cancelTimer();
        }
        if (songNumber + 1 >= musicCollection.size()) {
            songNumber = 0;
        } else {
            songNumber++;
        }
        media = new Media(musicCollection.get(songNumber).toPath().toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        nextMusicName();
        musicProgress.setProgress(0.0);
        playMusic(event);
    }

    @FXML
    void pauseMusic(ActionEvent event) {
        if (musicCollection.isEmpty()) {
            musicLabel.setText("Player is Empty");
            return;
        }
        cancelTimer();
        mediaPlayer.pause();
    }

    void nextMusicName() {
        try {
            File tempFile = musicCollection.get(songNumber);
            musicLabel.setText(tempFile.getName());
            if (musicCollection.size() <= 1) {
                nextSongDisplay.setText("No Music Available");
                return;
            }
            int i = songNumber;
            nextSongDisplay.setText(musicCollection.get(i + 1).getName());
        } catch (IndexOutOfBoundsException e) {
            nextSongDisplay.setText(musicCollection.get(0).getName());
        }
    }

    @FXML
    void playMusic(ActionEvent event) {
        if (musicCollection.isEmpty()) {
            musicLabel.setText("Player is Empty");
            return;
        }
        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeMusic.getValue() * 0.005);
        mediaPlayer.play();
    }

    @FXML
    void prevMusic(ActionEvent event) {
        if (musicCollection.isEmpty()) {
            musicLabel.setText("Player is Empty");
            return;
        }
        mediaPlayer.stop();
        try {
            songNumber--;
            media = new Media(musicCollection.get(songNumber).toPath().toUri().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (IndexOutOfBoundsException e) {
            songNumber = musicCollection.size() - 1;
            media = new Media(musicCollection.get(songNumber).toPath().toUri().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        nextMusicName();
        musicProgress.setProgress(0.0);
        playMusic(event);
    }

    void lastElement() {
        songNumber = musicCollection.size() - 1;
    }

    static void insertLast(File file) {
        musicCollection.add(file);
    }

    void deleteParticular(File file) {
        musicCollection.remove(file);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (musicCollection.isEmpty()) {
            directory = new File("Musics");
            files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    insertLast(file);
                }
            }
        }
        lastElement();
        media = new Media(musicCollection.get(songNumber).toPath().toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        nextMusicName();
        for (int i = 0; i < speed.length; i++) {
            speedBox.getItems().add(Integer.toString(speed[i]) + "%");
        }
        musicProgress.setStyle("-fx-accent: #00ff00;");
        speedBox.setOnAction(this::changeSpeed);
    }
}
