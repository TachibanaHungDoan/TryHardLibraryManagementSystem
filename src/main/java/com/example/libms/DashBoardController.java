package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    @FXML
    void initialize() {
        SceneController.setUpScene(usernameLabel, timeLabel);
        totalBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        totalReadersLabel.setText(String.valueOf(getTotalReadersFromDatabase()));
    }

    private int getTotalBooksFromDatabase() {
        int totalBooks = 0;
        String query = "SELECT COUNT(bookID) AS total FROM books";
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

    private int getTotalReadersFromDatabase() {
        int totalReaders = 0;
        String query = "SELECT COUNT(readerID) AS total FROM readers";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalReaders = resultSet.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalReaders;
    }

    @FXML
    void booksButtonClicked() throws IOException {
        SceneController.bookFlipSound();
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
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
        SceneController.switchSceneWithAlert("LoginView/login-view.fxml", logOutButton
                                            , null, null
                                            ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        SceneController.logOutSound();
    }
}
