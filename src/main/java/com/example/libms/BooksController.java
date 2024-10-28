package com.example.libms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.example.libms.LoginController.userName;

public class BooksController {
    @FXML
    private Button addBookButton;

    @FXML
    private Label allBooksLabel;

    @FXML
    private Button booksButton;

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, Integer> bookIDColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, Date> publishedDateColumn;
    @FXML
    private TableColumn<Book, Integer> editionColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TableColumn<Book, Integer> stateColumn;
    @FXML
    private TableColumn<Book, Integer> remainingColumn;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
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
    private TextField searchBar;

    @FXML
    private Label timeLabel;

    @FXML
    private Button updateBookButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button viewBookButton;


    public void initialize() {
        // Create a DateTimeFormatter
        usernameLabel.setText(userName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Set up the Timeline to update every second
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    // Get the current time and format it
                    String currentTime = LocalDateTime.now().format(formatter);
                    // Update the timeLabel
                    timeLabel.setText(currentTime);
                })
        );

        // Make the Timeline run indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        int totalBooks = getTotalBooksFromDatabase();
        allBooksLabel.setText(String.valueOf(totalBooks));
        // Liên kết các cột với các thuộc tính của Book
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));

        // Gọi hàm loadDataFromDatabase để lấy dữ liệu từ cơ sở dữ liệu
        loadDataFromDatabase();
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
    private void loadDataFromDatabase() {
        String query = "SELECT * FROM books";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                String isbn = resultSet.getString("isbn");
                Date publishedDate = resultSet.getDate("publishedDate");
                int edition = resultSet.getInt("edition");
                int quantity = resultSet.getInt("quantity");
                int state = resultSet.getInt("state");
                int remaining = resultSet.getInt("remaining");
                Book book = new Book(id, title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);
                bookList.add(book);
            }
            booksTable.setItems(bookList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
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

    public void logOutButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
