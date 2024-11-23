package com.example.libms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class RDashBoardController extends SceneController {

    @FXML
    private Button allBooksButton;

    @FXML
    private Button booksInventoryButton;

    @FXML
    private Button bybrButton;

    @FXML
    private Button settingButton;

    @FXML
    private Label bybrLabel;

    @FXML
    private Button dashBoardButton;

    @FXML
    private Button gamesButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Label mbbLabel;

    @FXML
    private Button mbrButton;

    @FXML
    private Label quoteAuthorLabel;

    @FXML
    private Text quoteText;

    @FXML
    private Label timeLabel;

    @FXML
    private Label usernameLabel;


    private List<Quote> quotes = new ArrayList<>();
    private int currentQuoteIndex = 0;


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
    void rMBRButtonClicked(ActionEvent event) {

    }

    //Nút sách đã mượn gần đây.
    @FXML
    void rBYBRButtonClicked(ActionEvent event) {

    }
}
