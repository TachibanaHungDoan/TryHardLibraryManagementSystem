package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RGameController {

    @FXML
    private Button allBooksButton;

    @FXML
    private Button logOutButton;

    @FXML
    private FlowPane buttons;

    @FXML
    private ImageView hangmanImage;

    @FXML
    private Text text;

    @FXML
    private Text winStatus;

    @FXML
    private Text realWord;

    private int mistakes;
    private int correct;
    private String myWord;
    private List<String> myLetters;
    private List<String> answer;

    private final List<String> images = Arrays.asList(
            "/com/example/libms/GameAssets/1.png", "/com/example/libms/GameAssets/2.png", "/com/example/libms/GameAssets/3.png",
            "/com/example/libms/GameAssets/4.png", "/com/example/libms/GameAssets/5.png", "/com/example/libms/GameAssets/6.png", "/com/example/libms/GameAssets/7.png"
    );

    @FXML
    public void initialize() {
        newGame(); // Bắt đầu game mới
    }

    @FXML
    public void newGame() {
        mistakes = 0;
        correct = 0;
        myWord = getRandomWord(); // Lấy từ ngẫu nhiên từ database
        myLetters = Arrays.asList(myWord.split(""));  // Chia từ thành các chữ cái
        answer = new ArrayList<>();
        for (int i = 0; i < myLetters.size(); i++) {
            answer.add("_");
        }
        text.setText(String.join(" ", answer)); // Cập nhật giao diện
        winStatus.setText("");
        realWord.setText("");
        hangmanImage.setImage(null);
        buttons.getChildren().forEach(node -> node.setDisable(false)); // Bật lại các nút
    }

    @FXML
    void onClick(ActionEvent event) {
        String letter = ((Button) event.getSource()).getText();
        ((Button) event.getSource()).setDisable(true); // Vô hiệu nút sau khi chọn

        if (myLetters.contains(letter)) {
            for (int i = 0; i < myLetters.size(); i++) {
                if (myLetters.get(i).equals(letter)) {
                    answer.set(i, letter);
                    correct++;
                }
            }
            text.setText(String.join(" ", answer)); // Cập nhật từ hiện tại
            if (correct == myLetters.size()) {
                winStatus.setText("You Win!");
                buttons.getChildren().forEach(node -> node.setDisable(true)); // Tắt các nút khi thắng
            }
        } else {
            mistakes++;
            updateHangmanImage(); // Cập nhật hình ảnh
            if (mistakes >= images.size()) {
                winStatus.setText("You Lose!");
                realWord.setText("The actual word was: " + myWord); // Hiển thị từ đúng khi thua
                buttons.getChildren().forEach(node -> node.setDisable(true)); // Tắt các nút khi thua
            }
        }
    }

    private void updateHangmanImage() {
        if (mistakes > 0 && mistakes <= images.size()) {
            String imagePath = images.get(mistakes - 1);
            Image image = new Image(getClass().getResourceAsStream(imagePath)); // Lấy hình ảnh từ resources
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
                randomWord = resultSet.getString("author").toUpperCase(); // Lấy từ ngẫu nhiên từ database
            }
            DatabaseConnection.closeStatement(statement);
            DatabaseConnection.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }
        return randomWord.replaceAll("[^A-Z]", ""); // Loại bỏ ký tự đặc biệt
    }
}