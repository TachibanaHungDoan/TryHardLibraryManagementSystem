package com.example.libms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.libms.LoginController.userName;

public class SceneController {
    protected static void switchScene(String fxmlViewFile, Button button) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlViewFile));
        Parent root =fxmlLoader.load();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    protected static void switchScene(String scene, Label label) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(scene));
        Parent root =fxmlLoader.load();
        Stage stage = (Stage) label.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    protected static void openDialogPane(String fxmlViewFile, Button button) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(fxmlViewFile));
        DialogPane dialogPane = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Update Book");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(button.getScene().getWindow());
        dialogStage.setScene(new Scene(dialogPane));
        dialogStage.showAndWait();
    }

    protected static void setUpScene(Label usernameLabel, Label timeLabel) {
        usernameLabel.setText(userName);
        setUpTimeLabel(timeLabel);
    }

    private static void setUpTimeLabel (Label timeLabel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
