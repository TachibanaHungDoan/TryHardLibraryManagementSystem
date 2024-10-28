package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashBoardController {
    @FXML
    private Button booksButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button readersButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label totalReadersLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label borrowedBooksLabel;

    public void initialize() {
        int totalBooks = getTotalBooksFromDatabase();
        totalBooksLabel.setText(String.valueOf(totalBooks));
       //usernameLabel.setText(getUsername());
    }

    private int getTotalBooksFromDatabase() {
        int totalBooks = 0;
        String query = "SELECT COUNT(*) AS total FROM books";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalBooks = resultSet.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBooks;
    }

    public void booksButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/books-view.fxml"));
        Stage stage = (Stage) booksButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void homeButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminView/dashBoard-view.fxml"));
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

    public void logOutButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
