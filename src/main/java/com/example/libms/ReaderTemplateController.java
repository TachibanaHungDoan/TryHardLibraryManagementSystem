package com.example.libms;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class ReaderTemplateController extends SceneController {
    private SoundButtonController soundButtonController = SoundButtonController.getInstance();

    @Override
    protected void setUpScene(Label usernameLabel, Label timeLabel) {
        String username = LoggedInUser.getUsername();
        usernameLabel.setText(username);
        setUpTimeLabel(timeLabel);
    }

    protected void switchToDashBoardView(Button dashBoardButton) throws IOException {
        soundButtonController.playButtonClickSound1();
        switchScene("ReaderView/rDashBoard-view.fxml", dashBoardButton);
    }

    protected void switchToGameView(Button gamesButton) throws IOException {
        soundButtonController.playButtonClickSound2();
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    protected void switchToLoginView(Button logOutButton) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null, "Do you want to log out?", Alert.AlertType.CONFIRMATION);
        soundButtonController.logOutSound();
    }

    protected void switchToAllBooksView(Button allBooksButton) throws IOException {
        soundButtonController.bookFlipSound();
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    protected void switchToBooksInventoryView(Button booksInventoryButton) throws IOException {
        soundButtonController.bookshelfSound();
        switchScene("ReaderView/rBooksInventoryBorrow-view.fxml", booksInventoryButton);
    }
}
