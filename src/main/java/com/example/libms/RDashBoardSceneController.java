package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;

public class RDashBoardSceneController extends SceneController {

    @FXML
    private Button allBooksButton;

    @FXML
    private Button booksInventoryButton;

    @FXML
    private Button bybrButton;

    @FXML
    private Label bybrLabel;

    @FXML
    private Button dashBoardButton;

    @FXML
    private Button gamesButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Label mbbLabel;

    @FXML
    private Button mbrButton;

    @FXML
    private Label quoteAuthorLabel;

    @FXML
    private Text quoteText;

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
                null, null,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

}
