package com.example.libms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static com.example.libms.LoggedInUser.getReaderID;

public class RDashBoardController extends SceneController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button allBooksButton, booksInventoryButton, gamesButton, logOutButton;
    @FXML
    private Button bybrButton, mbrButton, settingButton;
    @FXML
    private Label mbbLabel, bybrLabel;
    @FXML
    private Label quoteAuthorLabel;
    @FXML
    private Text quoteText;
    @FXML
    private PieChart pieChart;

    private final List<Quote> quotes = new ArrayList<>();
    private int currentQuoteIndex = 0;
    private final String mostBorrowedBookQuery = "SELECT * FROM books WHERE title = "
            + "'" + getMostBorrowedBookTitleFromDatabase() + "'" + " LIMIT 1";
    private final String bookYouRecentlyBorrowedQuery = "SELECT * FROM books WHERE title = "
            + "'" + getBookTitleYouRecentlyBorrowedFromDatabase() + "'" + " LIMIT 1";
    private final String totalBorrowedBooksQuery = "SELECT COUNT(*) AS total FROM borrowedbooks WHERE readerID = "
            + "'" + LoggedInUser.getReaderID() + "'";
    private final String totalReturnedBooksQuery = "SELECT COUNT(*) AS total FROM readerreturnedbooks WHERE readerID = "
            + "'" + LoggedInUser.getReaderID() +"'";

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        URL resourceUrl = RDashBoardController.class.getResource("/com/example/libms/Quote/Quotes.txt");
        if (resourceUrl != null) {
            loadQuotes(resourceUrl.getPath());
        } else {
            System.err.println("File not found: Quote/Quotes.txt");
        }
        startQuoteTimeline();
        showRandomQuote();
        setLabel(mbbLabel, getMostBorrowedBookTitleFromDatabase());
        setLabel(bybrLabel, getBookTitleYouRecentlyBorrowedFromDatabase());
        initPieChart();
    }

    private int getTotalCategoryFromDatabase(String query) {
        int totalCategory = 0;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                totalCategory = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalCategory;
    }

    private void initPieChart() {
        int totalBorrowedBooks = getTotalCategoryFromDatabase(totalBorrowedBooksQuery);
        int totalReturnedBooks = getTotalCategoryFromDatabase(totalReturnedBooksQuery);
        //System.out.println(totalBorrowedBooks + " " + totalReturnedBooks);
        pieChart.getData().clear();
        PieChart.Data totalBorrowedBooksData = new PieChart.Data("Borrowed Books", totalBorrowedBooks);
        PieChart.Data totalReturnedBooksData = new PieChart.Data("Returned Books", totalReturnedBooks);
        pieChart.getData().addAll(totalBorrowedBooksData, totalReturnedBooksData);
    }

    private Book correctBook(String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("bookID");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                String isbn = resultSet.getString("isbn");
                Date publishedDate = resultSet.getDate("publishedDate");
                int edition = resultSet.getInt("edition");
                int quantity = resultSet.getInt("quantity");

                String stateString = resultSet.getString("state");
                Book.BookState state = Book.BookState.valueOf(stateString.toLowerCase());

                int remaining = resultSet.getInt("remaining");
                return new Book(id, title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String getMostBorrowedBookTitleFromDatabase() {
        String mostBorrowedBook = "";
        String query = "SELECT title FROM borrowedbooks Group By isbn ORDER BY COUNT(*) DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                mostBorrowedBook = resultSet.getString("title");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mostBorrowedBook;
    }

    private String getBookTitleYouRecentlyBorrowedFromDatabase() {
        String bookYouRecentlyBorrowed = "";
        String query = "SELECT title FROM borrowedbooks " +
                "where readerID = " + LoggedInUser.getReaderID() + " ORDER BY id DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                bookYouRecentlyBorrowed = resultSet.getString("title");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookYouRecentlyBorrowed;
    }

    private void setLabel(Label label, String text) {
        if (text.isEmpty()) {
            label.setText("No books borrowed yet.");
        } else {
            label.setText(text);
        }
    }

    private void loadQuotes(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String quote = parts[0].trim();
                    String author = parts[1].trim();
                    quotes.add(new Quote(quote, author));
                } else {
                    System.out.println("INVALID QUOTE FORMAT: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startQuoteTimeline() {
        if (quotes.isEmpty()) return;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(20), e -> updateQuote()));
        timeline.setCycleCount(Timeline.INDEFINITE); // Lặp vô hạn
        timeline.play();
        updateQuote(); // Hiển thị quote đầu tiên ngay khi khởi động
    }

    private void updateQuote() {
        if (quotes.isEmpty()) return;
        Quote quote = quotes.get(currentQuoteIndex);
        quoteText.setText(quote.getQuote());
        quoteAuthorLabel.setText("-" + quote.getAuthor() + "-");
        currentQuoteIndex = (currentQuoteIndex + 1) % quotes.size();
    }

    Random random = new Random();

    private void showRandomQuote() {
        if (!quotes.isEmpty()) {
            int randomIndex = random.nextInt(quotes.size());
            Quote randomQuote = quotes.get(randomIndex);
            quoteText.setText(randomQuote.getQuote());
            quoteAuthorLabel.setText("-" + randomQuote.getAuthor() + "-");
        }
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null, "Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        bookFlipSound();
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    @FXML
    void rBooksInventoryButtonClicked(ActionEvent event) throws IOException {
        bookshelfSound();
        switchScene("ReaderView/rBooksInventoryBorrow-view.fxml", booksInventoryButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    //nút sách mượn nhiều nhất
    @FXML
    void rMBRButtonClicked(ActionEvent event) throws IOException {
        if (getMostBorrowedBookTitleFromDatabase().isEmpty()) {
            alertSoundPlay();
            showAlert(null, "No book to view", null, Alert.AlertType.WARNING);
        } else {
            bookFlipSound();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/viewBooks-view.fxml"));
            DialogPane dialogPane = loader.load();

            ViewBooksController controller = loader.getController();
            controller.setBookData(correctBook(mostBorrowedBookQuery));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("View Book Details");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK || result.get() == ButtonType.CANCEL)) {
                dialog.close();
            }
        }
    }


    //Nút sách đã mượn gần đây.
    @FXML
    void rBYBRButtonClicked(ActionEvent event) throws IOException {
        if (getBookTitleYouRecentlyBorrowedFromDatabase().isEmpty()) {
            alertSoundPlay();
            showAlert(null, "No book to view", null, Alert.AlertType.WARNING);
        } else {
            bookFlipSound();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/viewBooks-view.fxml"));
            DialogPane dialogPane = loader.load();

            ViewBooksController controller = loader.getController();
            controller.setBookData(correctBook(bookYouRecentlyBorrowedQuery));

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("View Book Details");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK || result.get() == ButtonType.CANCEL)) {
                dialog.close();
            }
        }
    }

    @FXML
    void settingButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/rAccountSettings-view.fxml"));
        DialogPane dialogPane = loader.load();

        RSettingController controller = loader.getController();

        Dialog<Void> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Settings");
        dialog.showAndWait();
    }
}
