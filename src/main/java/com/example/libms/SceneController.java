package com.example.libms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.libms.LoginController.userName;

public abstract class SceneController {

    /*protected static void playBackGroundMusic() {
        Media bgMusic = new Media(App.class.getResource("Sound").toExternalForm());
        MediaPlayer bgMusicPlayer = new MediaPlayer(bgMusic);
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.setAutoPlay(true);
    }*/

    private static final Map<String, AudioClip> soundCache = new HashMap<>();

    private static final Timeline timeLine = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            })
    );

    private static void playSound(String soundFileName) {
        AudioClip sound = soundCache.computeIfAbsent(soundFileName, file -> new AudioClip
                (SceneController.class.getResource("Sound/" + soundFileName).toExternalForm()));
        sound.play();
    }

    protected static void alertSoundPlay() {
        playSound("alert.mp3");
    }

    protected static void playButtonClickSound1() {
        playSound("buttonClickSound1.mp3");
    }

    protected static void playButtonClickSound2() {
        playSound("buttonClickSound2.mp3");
    }

    protected static void bookFlipSound() {
        playSound("bookFlipSound.mp3");
    }

    protected static void bookshelfSound() {
        playSound("bookshelfSound.mp3");
    }

    protected static void logOutSound() {
        playSound("logOutSound.mp3");
    }

    protected static void switchScene(String fxmlViewFile, Button button) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlViewFile));
        Parent root =fxmlLoader.load();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    protected static void switchSceneWithAlert(String fxmlViewFile, Button button, String title, String headerText, String message, Alert.AlertType alertType) throws IOException {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(okButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            switchScene(fxmlViewFile, button);
        }
    }

    protected static void showAlert(String title, String headerText, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    protected static void setUpScene(Label usernameLabel, Label timeLabel) {
        usernameLabel.setText(userName);
        setUpTimeLabel(timeLabel);
    }

    protected static void setUpTimeLabel (Label timeLabel) {
        timeLine.getKeyFrames().setAll(new KeyFrame(Duration.seconds(1), event -> {
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            timeLabel.setText(currentTime);
        }));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
}
