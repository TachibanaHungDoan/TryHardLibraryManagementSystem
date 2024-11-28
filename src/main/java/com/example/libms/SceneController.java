package com.example.libms;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class SceneController {
    private final Map<String, AudioClip> soundCache = new HashMap<>();
    private TimeService timeService;

    private void playSound(String soundFileName) {
        AudioClip sound = soundCache.computeIfAbsent(soundFileName, _ -> new AudioClip
                (Objects.requireNonNull(SceneController.class.getResource("Sound/" + soundFileName)).toExternalForm()));
        sound.play();
    }

    protected void alertSoundPlay() {
        playSound("alert.mp3");
    }

    protected void playButtonClickSound1() {
        playSound("buttonClickSound1.mp3");
    }

    protected void playButtonClickSound2() {
        playSound("buttonClickSound2.mp3");
    }

    protected void bookFlipSound() {
        playSound("bookFlipSound.mp3");
    }

    protected void bookshelfSound() {
        playSound("bookshelfSound.mp3");
    }

    protected void deleteConfirm() {
        playSound("DeleteConfirm.mp3");
    }

    protected void logOutSound() {
        playSound("logOutSound.mp3");
    }

    protected void winGameSound() {
        playSound("victory.mp3");
    }

    protected void loseGameSound() {
        playSound("loss.mp3");
    }

    protected void switchScene(String fxmlViewFile, Button button) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlViewFile));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    protected void switchSceneWithAlert(String fxmlViewFile, Button button, String title, String headerText, String message, Alert.AlertType alertType) throws IOException {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(okButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlViewFile));
            Parent root =fxmlLoader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    protected void showAlert(String title, String headerText, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    protected void setUpScene(Label usernameLabel, Label timeLabel) {
        usernameLabel.setText(LoggedInUser.getUsername());
        setUpTimeLabel(timeLabel);
    }

    protected void setUpTimeLabel (Label timeLabel) {
        if (timeService == null) {
            timeService = new TimeService(timeLabel);
            timeService.start();
        }
        timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
