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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;
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
    private ToggleButton toggleMusicButton;
    @FXML
    private Label totalBooksLabel, totalReadersLabel, borrowedBooksLabel;
    @FXML
    private BarChart<String, Number> bnrBarChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private final String TOTAL_BORROWEDBOOKS_QUERY = "SELECT COUNT(id) AS total FROM borrowedbooks";

    private BackgroundMusic backgroundMusic = BackgroundMusic.getInstance();

    @FXML
    void initialize() {
        toggleMusicButton.setOnAction(e -> backgroundMusic.toggleBackgroundMusic("/com/example/libms/Sound/perfectBackgroundSound.mp3"));
        int totalBooksBorrowed = getTotalCategoryFromDatabase(TOTAL_BORROWEDBOOKS_QUERY);
        setUpScene(usernameLabel, timeLabel);
        totalBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        totalReadersLabel.setText(String.valueOf(getTotalReadersFromDatabase()));
        borrowedBooksLabel.setText(String.valueOf(totalBooksBorrowed));
        initializeBarChart(getTotalBooksFromDatabase(), getTotalReadersFromDatabase(), totalBooksBorrowed);
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
    }

    private int getTotalBooksFromDatabase() {
        BookDAO bookDAO = new BookDAO();
        return bookDAO.getTotalItems();
    }

    private int getTotalReadersFromDatabase() {
        ReaderDAO readerDAO = new ReaderDAO();
        return readerDAO.getTotalItems();
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
