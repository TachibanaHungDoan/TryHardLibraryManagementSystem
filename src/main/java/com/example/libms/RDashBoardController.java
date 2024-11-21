package com.example.libms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RDashBoardController extends SceneController {

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

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    @FXML
    void rBooksInventoryButtonClicked(ActionEvent event) throws IOException {
        switchScene("ReaderView/rBooksInventory-view.fxml",booksInventoryButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    //nút sách mượn nhiều nhất
    @FXML
    void rMBRButtonClicked(ActionEvent event) {

    }

    //Nút sách đã mượn gần đây.
    @FXML
    void rBYBRButtonClicked(ActionEvent event) {

    }
}
