package com.example.libms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksController {

    @FXML
    private Button addBookButton;

    @FXML
    private Label allBooksLabel;

    @FXML
    private Button booksButton;

    @FXML
    private TableView<?> booksTable;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button deleteBookButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button readersButton;

    @FXML
    private Button returnBooksButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Label timeLabel;

    @FXML
    private Button updateBookButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button viewBookButton;

    public void homeButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/dashboard-view.fxml"));
        Stage stage = (Stage) homeButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void booksButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/books-view.fxml"));
        Stage stage = (Stage) booksButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void readersButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/readers-view.fxml"));
        Stage stage = (Stage) readersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void borrowedBooksButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/borrowedBooks-view.fxml"));
        Stage stage = (Stage) borrowedBooksButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void returnBooksButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/returnBooks-view.fxml"));
        Stage stage = (Stage) returnBooksButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logOutButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
