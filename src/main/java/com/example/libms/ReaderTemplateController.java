package com.example.libms;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class ReaderTemplateController extends SceneController {
    @Override
    protected void setUpScene(Label usernameLabel, Label timeLabel) {
        String username = getUserName();
        usernameLabel.setText(username);
        setUpTimeLabel(timeLabel);
    }

    protected void switchToDashBoardView(Button dashBoardButton) throws IOException {
        playButtonClickSound1();
        switchScene("ReaderView/rDashBoard-view.fxml", dashBoardButton);
    }

    protected void switchToGameView(Button gamesButton) throws IOException {
        playButtonClickSound2();
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    protected void switchToLoginView(Button logOutButton) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null, "Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    protected void switchToAllBooksView(Button allBooksButton) throws IOException {
        bookFlipSound();
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    protected void switchToBooksInventoryView(Button booksInventoryButton) throws IOException {
        bookshelfSound();
        switchScene("ReaderView/rBooksInventoryBorrow-view.fxml", booksInventoryButton);
    }
}
