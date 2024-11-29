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
    private TimeService timeService;

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
