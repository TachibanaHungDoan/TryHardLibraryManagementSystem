package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class RBooksInventoryReturnController extends SceneController {

    @FXML
    private Button allBooksButton;

    @FXML
    private Button booksInventoryButton;

    @FXML
    private Button borrowedBooksScreenSwitchButton;

    @FXML
    private Button dashBoardButton;

    @FXML
    private Button gamesButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Label timeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
    }

    @FXML
    void borrowedBooksSwitchScenehButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("ReaderView/rBooksInventoryBorrow-view.fxml", borrowedBooksScreenSwitchButton);
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        bookFlipSound();
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound1();
        switchScene("ReaderView/rDashBoard-view.fxml",dashBoardButton);
    }

}
