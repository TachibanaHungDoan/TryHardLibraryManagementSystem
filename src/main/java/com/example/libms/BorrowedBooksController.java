package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BorrowedBooksController {
    @FXML
    private Label allBorrowedBooksLabel;

    @FXML
    private Button booksButton;

    @FXML
    private Button borrowedBooksButton;

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
        SceneController.setUpScene(usernameLabel, timeLabel);
    }

    @FXML
    void booksButtonClicked() throws IOException {
        SceneController.bookFlipSound();
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
        SceneController.playButtonClickSound1();
        SceneController.switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void readersButtonClicked(ActionEvent event) throws IOException {
        SceneController.playButtonClickSound2();
        SceneController.switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        SceneController.bookFlipSound();
        SceneController.switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        SceneController.switchSceneWithAlert("login-view.fxml", logOutButton
                , null, null
                ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        SceneController.logOutSound();
    }

}
