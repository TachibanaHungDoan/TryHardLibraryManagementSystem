package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBooksController {
    @FXML
    private Button addButton;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField bookISBNTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private Button booksButton;

    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField editionTextField;

    @FXML
    private Button homeButton;

    @FXML
    private ImageView imageImageView;

    @FXML
    private Button importImageButton;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField publishedDateTextField;

    @FXML
    private TextField publisherTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private Button readersButton;

    @FXML
    private TextField remainingTextField;

    @FXML
    private TextField stateTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label messageLabel;

    private BookDAO bookdao = new BookDAO();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    void initialize() {
        SceneController.setUpScene(usernameLabel, timeLabel);
        importImageButton.setOnAction(event -> importImage());
        addButton.setOnAction(event -> addBook());
    }

    @FXML
    void booksButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void borrowedBooksButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void homeButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        SceneController.switchScene("login-view.fxml", logOutButton);
    }

    @FXML
    void readersButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void cancelButtonClicked() throws IOException, SQLException {
        SceneController.playButtonClickSound2();
        bookTitleTextField.clear();
        authorTextField.clear();
        publisherTextField.clear();
        bookISBNTextField.clear();
        publishedDateTextField.clear();
        editionTextField.clear();
        quantityTextField.clear();
        stateTextField.clear();
        remainingTextField.clear();
        SceneController.switchScene("AdminView/books-view.fxml", cancelButton);
    }

    //Lưu ý cần thêm xử lý ngoại lệ khi các ô bị thiếu.
    private void addBook() {
        String title = bookTitleTextField.getText();
        String author = authorTextField.getText();
        String publisher = publisherTextField.getText();

        String publishedDateStr = publishedDateTextField.getText();
        Date publishedDate = null;
        try {
            if (!publishedDateStr.isEmpty()) {
                publishedDate = dateFormat.parse(publishedDateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            SceneController.showAlert(null, null, "Invalid date format.", Alert.AlertType.ERROR);
            return; // Exit the method if date parsing fails
        }

        String isbn = bookISBNTextField.getText();
        int edition = Integer.parseInt(editionTextField.getText());
        int quantity = Integer.parseInt(quantityTextField.getText());
        //int state = Integer.parseInt(stateTextField.getText());

        String stateString = stateTextField.getText().toLowerCase();
        Book.BookState state;
        try {
            state = Book.BookState.valueOf(stateString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            SceneController.showAlert(null, null,
                            "Invalid State Filling. You must entered available or unavailable!", Alert.AlertType.ERROR);
            return;
        }

        int remaining = Integer.parseInt(remainingTextField.getText());

        Book book = new Book(0,title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);

        try {
            bookdao.insert(book);
            SceneController.showAlert(null,null,"Book added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            SceneController.showAlert(null, null, "Failed to add book.", Alert.AlertType.ERROR);
        }
    }

    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(imageImageView.getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageImageView.setImage(image);
        }
    }
}
