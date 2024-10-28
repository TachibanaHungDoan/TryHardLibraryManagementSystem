package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ReadersController {
    @FXML
    private Button addReaderButton;

    @FXML
    private Button booksButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button clearInformationButton;

    @FXML
    private Button deleteReaderButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField readerEmailTextField;

    @FXML
    private ComboBox<?> readerGenderChoiceBox;

    @FXML
    private TextField readerIDTextField;

    @FXML
    private TextField readerNameTextField;

    @FXML
    private TextField readerPhoneTextField;

    @FXML
    private Button readersButton;

    @FXML
    private TableView<?> readersTable;

    @FXML
    private Button returnBooksButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Label timeLabel;

    @FXML
    private Button updateReaderButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button viewReaderButton;

    public void booksButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/books-view.fxml"));
        Stage stage = (Stage) booksButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void homeButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashBoard-view.fxml"));
        Stage stage = (Stage) homeButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void readersButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/readers-view.fxml"));
        Stage stage = (Stage) readersButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/borrowedBooks-view.fxml"));
        Stage stage = (Stage) borrowedBooksButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void returnBooksButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("returnBooks-view.fxml"));
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
