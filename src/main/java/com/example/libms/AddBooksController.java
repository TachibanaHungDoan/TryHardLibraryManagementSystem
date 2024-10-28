package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class AddBooksController {
    @FXML
    private Button addButton;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField bookISBNTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private Button booksButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField editionTextField;

    @FXML
    private Button homeButton;

    @FXML
    private ImageView imageImageView;

    @FXML
    private Button importImageButton;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField publishedDateTextField;

    @FXML
    private TextField publisherTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private Button readersButton;

    @FXML
    private TextField remainingTextField;

    @FXML
    private TextField stateTextField;

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
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void borrowedBooksButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void homeButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        SceneController.switchScene("login-view.fxml", logOutButton);
    }

    @FXML
    void readersButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void cancelButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/books-view.fxml", cancelButton);
    }
}
