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
import java.util.Optional;

import static com.example.libms.LoginController.userName;

public abstract class SceneController {

    /*protected static void playBackGroundMusic() {
        Media bgMusic = new Media(App.class.getResource("Sound").toExternalForm());
        MediaPlayer bgMusicPlayer = new MediaPlayer(bgMusic);
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.setAutoPlay(true);
    }*/

    protected static void alertSoundPlay() {
        AudioClip sound = new AudioClip(App.class.getResource("Sound/alert.mp3").toExternalForm());
        sound.play();
    }

    protected static void playButtonClickSound1() {
        AudioClip sound = new AudioClip(App.class.getResource("Sound/buttonClickSound1.mp3").toExternalForm());
        sound.play();
    }
    protected static void playButtonClickSound2() {
        AudioClip sound = new AudioClip(App.class.getResource("Sound/buttonClickSound2.mp3").toExternalForm());
        sound.play();
    }
    protected static void bookFlipSound() {
        AudioClip sound = new AudioClip(App.class.getResource("Sound/bookFlipSound.mp3").toExternalForm());
        sound.play();
    }
    protected static void bookshelfSound() {
        AudioClip sound = new AudioClip(App.class.getResource("Sound/bookshelfSound.mp3").toExternalForm());
        sound.play();
    }
    protected static void logOutSound() {
        AudioClip sound = new AudioClip(App.class.getResource("Sound/logOutSound.mp3").toExternalForm());
        sound.play();
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

    protected static void openDialogPane(String fxmlViewFile, String title, Button button) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlViewFile));
        DialogPane dialogPane = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Good");
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

    private static void setUpTimeLabel (Label timeLabel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event ->
                {

                    String currentTime = LocalTime.now().format(formatter);

                    timeLabel.setText(currentTime);
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
