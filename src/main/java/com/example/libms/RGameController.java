package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RGameController extends ReaderTemplateController {
    @FXML
    private Button dashBoardButton, allBooksButton, booksInventoryButton, logOutButton;
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button newGameButton, hintButton;
    @FXML
    private FlowPane buttons;
    @FXML
    private ImageView hangmanImage;
    @FXML
    private Text text, hintText;

    private int mistakeCounts;
    private int correctCounts;
    private String authorWordToGuess;
    private String authorWord;
    private List<String> myLetters;
    private List<String> answer;
    private final List<String> images = Arrays.asList(
            "1.png", "2.png", "3.png", "4.png", "5.png", "6.png", "7.png"
    );

    @FXML
    public void initialize() {
        setUpScene(usernameLabel, timeLabel);
        newGameButton.setOnAction(event -> newGame());
        hintButton.setOnAction(event -> showHint());
        newGame(); // Bắt đầu game mới
    }

    @FXML
    void newGame() {
        mistakeCounts = 0;
        correctCounts = 0;
        authorWord = getRandomWord();
        authorWordToGuess = authorWord.toUpperCase().replaceAll("[^A-Z]", ""); // Lấy từ ngẫu nhiên từ database
        myLetters = Arrays.asList(authorWordToGuess.split(""));  // Chia từ thành các chữ cái
        answer = new ArrayList<>();
        for (int i = 0; i < myLetters.size(); i++) {
            answer.add("_");
        }
        text.setText(String.join(" ", answer));
        hintText.setText("");
        hangmanImage.setImage(new Image(String.valueOf(RGameController.class.getResource("Icon/appIcon.png"))));
        buttons.getChildren().forEach(node -> node.setDisable(false)); // Bật lại các nút
    }

    @FXML
    void showHint() {
        hintText.setText(getHintText());
    }

    @FXML
    void onClick(ActionEvent event) {
        String letter = ((Button) event.getSource()).getText();
        ((Button) event.getSource()).setDisable(true); // Vô hiệu nút sau khi chọn

        if (correctCounts == 0 && mistakeCounts == 0) {
            hangmanImage.setImage(null);
        }

        if (myLetters.contains(letter)) {
            for (int i = 0; i < myLetters.size(); i++) {
                if (myLetters.get(i).equals(letter)) {
                    answer.set(i, letter);
                    correctCounts++;
                }
            }
            text.setText(String.join(" ", answer)); // Cập nhật từ hiện tại
            if (correctCounts == myLetters.size()) {
                buttons.getChildren().forEach(node -> node.setDisable(true)); // Tắt các nút khi thắng
                winGameSound();
                Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
                winAlert.setHeaderText("Congratulations!\nYou have won the game by guessing the correct author who is " + authorWord + ".");
                winAlert.setContentText("Do you want to play again?");
                winAlert.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
                winAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        winAlert.close();
                        newGame();
                    } else if (response == ButtonType.CANCEL) {
                        try {
                            switchToDashBoardView(dashBoardButton);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        } else {
            mistakeCounts++;
            updateHangmanImage(); // Cập nhật hình ảnh
            if (mistakeCounts >= images.size()) {
                buttons.getChildren().forEach(node -> node.setDisable(true)); // Tắt các nút khi thua
                loseGameSound();
                Alert loseAlert = new Alert(Alert.AlertType.INFORMATION);
                loseAlert.setHeaderText("Sorry! You have lost this game\nThe author you have to guess is " + authorWord + ".");
                loseAlert.setContentText("Do you want to play again?");
                loseAlert.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
                loseAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        loseAlert.close();
                        newGame();
                    } else if (response == ButtonType.CANCEL) {
                        try {
                            switchToDashBoardView(dashBoardButton);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws Exception {
        switchToLoginView(logOutButton);
    }

    @FXML
    void allBooksButtonClicked(ActionEvent event) throws Exception {
        switchToAllBooksView(allBooksButton);
    }

    @FXML
    void booksInventoryButtonClicked(ActionEvent event) throws Exception {
        switchToBooksInventoryView(booksInventoryButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws Exception {
        switchToDashBoardView(dashBoardButton);
    }

    private void updateHangmanImage() {
        if (mistakeCounts > 0 && mistakeCounts <= images.size()) {
            String imagePath = images.get(mistakeCounts - 1);
            String path = "GameAssets/" + imagePath;
            Image image = new Image(String.valueOf(RGameController.class.getResource(path))); // Lấy hình ảnh từ resources
            hangmanImage.setImage(image); // Cập nhật hình ảnh
        }
    }

    private String getRandomWord() {
        Connection connection = DatabaseConnection.getConnection();
        String randomWord = "HANGMAN"; // Từ mặc định nếu không lấy được từ database
        try {
            String query = "SELECT author FROM books ORDER BY RAND() LIMIT 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                randomWord = resultSet.getString("author"); // Lấy từ ngẫu nhiên từ database
            }
            DatabaseConnection.closeStatement(statement);
            DatabaseConnection.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }
        return randomWord;
    }

    private String getHintText() {
        String hintText;
        String hint = "";
        String query = "SELECT title FROM books WHERE author = '" + authorWord + "' LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                hint = resultSet.getString("title");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(hint);
        System.out.println(authorWord);
        System.out.println(authorWordToGuess);
        hintText = "Hint: The book titled " + hint + " is written by this author.";
        return hintText;
    }
}