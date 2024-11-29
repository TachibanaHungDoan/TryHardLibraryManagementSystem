package com.example.libms;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class AdminTemplateController extends SceneController {
    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private BackgroundMusic backgroundMusic = BackgroundMusic.getInstance();

    @Override
    protected void setUpScene(Label usernameLabel, Label timeLabel) {
        usernameLabel.setText("Admin");
        setUpTimeLabel(timeLabel);
    }

    protected void switchToDashboardView(Button homeButton) throws IOException {
        soundButtonController.playButtonClickSound1();
        switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    protected void switchToBooksView(Button booksButton) throws IOException {
        soundButtonController.bookFlipSound();
        switchScene("AdminView/books-view.fxml", booksButton);
    }

    protected void switchToReadersView(Button readersButton) throws IOException {
        soundButtonController.playButtonClickSound2();
        switchScene("AdminView/readers-view.fxml", readersButton);
    }

    protected void switchToBorrowedBooksView(Button borrowedBooksbutton) throws IOException {
        soundButtonController.bookFlipSound();
        switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksbutton);
    }

    protected void switchToLoginView(Button logOutButton) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton
                , null, null
                ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        soundButtonController.logOutSound();
        backgroundMusic.stopBackgroundMusic();
    }
}
