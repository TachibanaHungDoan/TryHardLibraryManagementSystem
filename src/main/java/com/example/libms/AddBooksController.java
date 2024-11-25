package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.util.List;

public class AddBooksController extends SceneController {
    @FXML
    private Button addButton;

    @FXML
    private TextField searchBar;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField bookISBNTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private Button clearButton;

    @FXML
    private TextField editionTextField;

    @FXML
    private ImageView imageImageView;

    @FXML
    private Button importImageButton;

    @FXML
    private TextField publishedDateTextField;

    @FXML
    private TextField publisherTextField;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TextField remainingTextField;

    @FXML
    private TextField stateTextField;

    private BookDAO bookdao = new BookDAO();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    void initialize() {
        importImageButton.setOnAction(event -> importImage());
        addButton.setOnAction(event -> addBook());
        clearButton.setOnAction(event -> clearButtonClicked());
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                List<BookSuggestion> suggestions = GoogleBooksAPI.searchBooks(newValue);

                Platform.runLater(() -> {
                    ContextMenu contextMenu = new ContextMenu();
                    for (BookSuggestion suggestion : suggestions) {
                        MenuItem item = new MenuItem(suggestion.getTitle());
                        item.setOnAction(event -> selectBookSuggestion(suggestion));
                        contextMenu.getItems().add(item);
                    }
                    contextMenu.show(searchBar, searchBar.getScene().getWindow().getX() + searchBar.getLayoutX(),
                            searchBar.getScene().getWindow().getY() + searchBar.getLayoutY() + searchBar.getHeight());
                });
            }
        });
    }
    private void selectBookSuggestion(BookSuggestion suggestion) {
        bookTitleTextField.setText(suggestion.getTitle());
        authorTextField.setText(suggestion.getAuthor());
        bookISBNTextField.setText(suggestion.getIsbn());
        publisherTextField.setText(suggestion.getPublisher());

        // Hiển thị ngày dưới dạng chuỗi
        if (suggestion.getPublishedDate() != null) {
            publishedDateTextField.setText(new SimpleDateFormat("yyyy-MM-dd").format(suggestion.getPublishedDate()));
        } else {
            publishedDateTextField.clear();
        }

        // Hiển thị ảnh (nếu có)
        if (suggestion.getThumbnail() != null) {
            Image image = new Image(suggestion.getThumbnail());
            imageImageView.setImage(image);
        }
    }

    @FXML
    void clearButtonClicked() {
        playButtonClickSound2();
        bookTitleTextField.clear();
        authorTextField.clear();
        publisherTextField.clear();
        bookISBNTextField.clear();
        publishedDateTextField.clear();
        editionTextField.clear();
        quantityTextField.clear();
        stateTextField.clear();
        remainingTextField.clear();
    }

    //Lưu ý cần thêm xử lý ngoại lệ khi các ô bị thiếu.
    private void addBook() {
        String title = bookTitleTextField.getText();
        String author = authorTextField.getText();
        String publisher = publisherTextField.getText();
        String publishedDateStr = publishedDateTextField.getText();
        String isbn = bookISBNTextField.getText();
        String editionStr = editionTextField.getText();
        String quantityStr = quantityTextField.getText();
        String remainingStr = remainingTextField.getText();
        // Check for empty fields
        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishedDateStr.isEmpty() || isbn.isEmpty() || editionStr.isEmpty() ||
                quantityStr.isEmpty() || remainingStr.isEmpty()) {
            alertSoundPlay();
            SceneController.showAlert(null, null, "All fields must be filled.", Alert.AlertType.WARNING);
        }
        Date publishedDate = null;
        try {
            publishedDate = dateFormat.parse(publishedDateStr);
        }catch (ParseException e) {
            SceneController.showAlert(null, null, "Invalid date format.", Alert.AlertType.ERROR);
            return; }
        int edition, quantity, remaining;
        try {
            edition = Integer.parseInt(editionStr);
            quantity = Integer.parseInt(quantityStr);
            remaining = Integer.parseInt(remainingStr);
        } catch (NumberFormatException e) {
            SceneController.showAlert(null, null, "Edition, Quantity, and Remaining must be valid integers.", Alert.AlertType.ERROR);
            return;
        } // Check if remaining is greater than or equal to quantity
        if (remaining > quantity) {
            alertSoundPlay();
            SceneController.showAlert(null, null, "Remaining cannot be greater than Quantity.", Alert.AlertType.WARNING);
            return;
        } // Automatically set the state based on remaining
        Book.BookState state;
        if (remaining == 0) {
            state = Book.BookState.unavailable;
        } else { state = Book.BookState.available;
        }
        Book book = new Book(0, title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);
        try {
            bookdao.insert(book);
            bookshelfSound();
            SceneController.showAlert(null,null,"Book added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
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
