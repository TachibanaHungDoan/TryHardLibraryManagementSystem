package com.example.libms;

import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashBoardController extends SceneController {
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
    private BarChart<String, Number> bnrBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        totalBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        totalReadersLabel.setText(String.valueOf(getTotalReadersFromDatabase()));
        borrowedBooksLabel.setText(String.valueOf(getTotalBorrowedBooksFromDatabase())); //Sau sẽ sử dụng hàm lấy số sách đã mượn từ CSDL
        initializeBarChart();
    }

    private void initializeBarChart() {
        xAxis.setLabel("Category");
        yAxis.setLabel("Count");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Library Statistics");
        int numberOfBooks = getTotalBooksFromDatabase();
        int numberOfReaders = getTotalReadersFromDatabase();
        int booksBorrowed = getTotalBorrowedBooksFromDatabase(); // Sau sẽ thêm hàm lấy số sách mượn từ CSDL
        series.getData().add(new XYChart.Data<>("Total Books", numberOfBooks));
        series.getData().add(new XYChart.Data<>("Total Readers", numberOfReaders));
        series.getData().add(new XYChart.Data<>("Total Borrowed Books", booksBorrowed));
        bnrBarChart.getData().add(series);

        /*Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Đảm bảo Node đã được tạo
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: green;");  // Đổi màu cho các cột
                }
            }
        });*/
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
    private int getTotalBorrowedBooksFromDatabase() {
        int totalBorrowedBooks = 0;
        String query = "SELECT COUNT(id) AS total FROM borrowedbooks";
        // Adjust table and column name as per your schema
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                totalBorrowedBooks = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalBorrowedBooks;
    }

    @FXML
    void booksButtonClicked() throws IOException {
        bookFlipSound();
        switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void readersButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void borrowedBooksButtonClicked(ActionEvent event) throws IOException {
        bookFlipSound();
        switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton
                                            , null, null
                                            ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }
}
