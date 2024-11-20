package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BorrowedBooksController extends SceneController {
    @FXML
    private Label allBorrowedBooksLabel;

    @FXML
    private Button booksButton;

    @FXML
    private TableView<?> borrowedBooksTable;

    @FXML
    private Button homeButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button readersButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Label timeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
    }

    @FXML
    void booksButtonClicked() throws IOException {
        bookFlipSound();
        switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound1();
        switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void readersButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton
                , null, null
                ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

}
