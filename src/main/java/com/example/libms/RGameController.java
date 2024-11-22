package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class RGameController extends SceneController {

    @FXML
    private Button allBooksButton;

    @FXML
    private Button booksInventoryButton;

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
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null, "Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        bookFlipSound();
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    @FXML
    void rBooksInventoryButtonClicked(ActionEvent event)  throws IOException {
        bookshelfSound();
        switchScene("ReaderView/rBooksInventory-view.fxml",booksInventoryButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound1();
        switchScene("ReaderView/rDashBoard-view.fxml",dashBoardButton);
    }

}
