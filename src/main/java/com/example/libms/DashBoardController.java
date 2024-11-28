package com.example.libms;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashBoardController extends AdminTemplateController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button booksButton, readersButton, borrowedBooksButton, logOutButton;
    @FXML
    private Label totalBooksLabel, totalReadersLabel, borrowedBooksLabel;
    @FXML
    private BarChart<String, Number> bnrBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private final String TOTAL_BOOKS_QUERY = "SELECT COUNT(bookID) AS total FROM books";
    private final String TOTAL_READERS_QUERY = "SELECT COUNT(readerID) AS total FROM readers";
    private final String TOTAL_BORROWEDBOOKS_QUERY = "SELECT COUNT(id) AS total FROM borrowedbooks";

    @FXML
    void initialize() {
        int totalBooks = getTotalCategoryFromDatabase(TOTAL_BOOKS_QUERY);
        int totalReaders = getTotalCategoryFromDatabase(TOTAL_READERS_QUERY);
        int totalBooksBorrowed = getTotalCategoryFromDatabase(TOTAL_BORROWEDBOOKS_QUERY);
        setUpScene(usernameLabel, timeLabel);
        totalBooksLabel.setText(String.valueOf(totalBooks));
        totalReadersLabel.setText(String.valueOf(totalReaders));
        borrowedBooksLabel.setText(String.valueOf(totalBooksBorrowed));
        initializeBarChart(totalBooks, totalReaders, totalBooksBorrowed);
    }

    @FXML
    void booksButtonClicked() throws IOException {
        switchToBooksView(booksButton);
    }

    @FXML
    void readersButtonClicked() throws IOException {
        switchToReadersView(readersButton);
    }

    @FXML
    void borrowedBooksButtonClicked() throws IOException {
        switchToBorrowedBooksView(borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchToLoginView(logOutButton);
    }

    private void initializeBarChart(int totalBooks, int totalReaders, int totalBooksBorrowed) {
        xAxis.setLabel("Category");
        yAxis.setLabel("Count");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Library Statistics");
        XYChart.Data<String, Number> totalBooksData = new XYChart.Data<>("Total Books", totalBooks);
        XYChart.Data<String, Number> totalReadersData = new XYChart.Data<>("Total Readers", totalReaders);
        XYChart.Data<String, Number> totalBooksBorrowedData = new XYChart.Data<>("Total Borrowed Books", totalBooksBorrowed);
        series.getData().add(totalBooksData);
        series.getData().add(totalReadersData);
        series.getData().add(totalBooksBorrowedData);
        bnrBarChart.getData().add(series);
        bnrBarChart.getStylesheets().add(getClass().getResource("des/dashboard-des.css").toExternalForm());

        Platform.runLater(() -> {
            Node totalBooksNode = totalBooksData.getNode();
            if (totalBooksNode != null) {
                totalBooksNode.getStyleClass().add("firstChart-bar");
            }
            Node totalReadersNode = totalReadersData.getNode();
            if (totalReadersNode != null) {
                totalReadersNode.getStyleClass().add("secondChart-bar");
            }
            Node booksBorrowedNode = totalBooksBorrowedData.getNode();
            if (booksBorrowedNode != null) {
                booksBorrowedNode.getStyleClass().add("thirdChart-bar");
            }
        });
        /*Platform.runLater(() -> {
            Node totalBooksNode = totalBooksData.getNode();
            if (totalBooksNode != null) {
                totalBooksNode.setStyle("-fx-bar-fill: #AAFFCC;");
            }
            Node totalReadersNode = totalReadersData.getNode();
            if (totalReadersNode != null) {
                totalReadersNode.setStyle("-fx-bar-fill: #FFCCAA;");
            }
            Node booksBorrowedNode = totalBooksBorrowedData.getNode();
            if (booksBorrowedNode != null) {
                booksBorrowedNode.setStyle("-fx-bar-fill: #1E5B53;");
            }
        });*/
    }

    private int getTotalCategoryFromDatabase(String query) {
        int total = 0;
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
