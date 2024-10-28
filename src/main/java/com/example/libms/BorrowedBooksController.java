package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    public void initialize() {
        SceneController.setUpScene(usernameLabel, timeLabel);
    }

    public void booksButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
    }

    public void homeButtonClicked(ActionEvent event) throws IOException {
        SceneController.switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    public void readersButtonClicked(ActionEvent event) throws IOException {
        SceneController.switchScene("AdminView/readers-view.fxml", readersButton);
    }

    public void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        SceneController.switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    public void logOutButtonClicked() throws IOException {
        SceneController.switchScene("login-view.fxml", logOutButton);
    }

}
