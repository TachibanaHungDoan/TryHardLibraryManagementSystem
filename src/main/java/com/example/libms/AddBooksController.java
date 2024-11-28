package com.example.libms;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddBooksController extends SceneController {
    @FXML
    private Button addButton, clearButton, importImageButton;
    @FXML
    private TextField searchBar;
    @FXML
    private TextField bookTitleTextField, authorTextField, publisherTextField;
    @FXML
    private TextField publishedDateTextField, bookISBNTextField, editionTextField;
    @FXML
    private TextField stateTextField, quantityTextField, remainingTextField;
    @FXML
    private ImageView imageImageView;

    private BookDAO bookdao = new BookDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ContextMenu currentContextMenu;

    @FXML
    public void initialize() {
        importImageButton.setOnAction(event -> importImage());
        addButton.setOnAction(event -> addBook());
        clearButton.setOnAction(event -> clearButtonClicked());
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                List<BookSuggestion> cachedSuggestions = BookSearchCache.get(newValue);
                if (cachedSuggestions != null) {
                    showSuggestionsPopup(cachedSuggestions);
                } else {
                    new Thread(() -> {
                        List<BookSuggestion> suggestions = GoogleBooksAPI.searchBooks(newValue);
                        BookSearchCache.put(newValue, suggestions);
                        Platform.runLater(() -> showSuggestionsPopup(suggestions));
                    }).start();
                }
            }
        });
    }

    @FXML
    public void clearButtonClicked() {
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
        imageImageView.setImage(null);
        searchBar.clear();
    }

    @FXML
    public void addBook() {
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
            showAlert(null, null, "All fields must be filled.", Alert.AlertType.WARNING);
        }

        Date publishedDate;
        try {
            publishedDate = dateFormat.parse(publishedDateStr);
        } catch (ParseException e) {
            showAlert(null, "Invalid date format.", "Please enter Published Date with the format of Year-Month-Day", Alert.AlertType.ERROR);
            return;
        }

        int edition, quantity, remaining;
        try {
            edition = Integer.parseInt(editionStr);
            quantity = Integer.parseInt(quantityStr);
            remaining = Integer.parseInt(remainingStr);
        } catch (NumberFormatException e) {
            showAlert(null, null, "Edition, Quantity, and Remaining must be valid integers.", Alert.AlertType.ERROR);
            return;
        } // Check if remaining is greater than or equal to quantity
        if (remaining > quantity) {
            alertSoundPlay();
            showAlert(null, null, "Remaining cannot be greater than Quantity.", Alert.AlertType.WARNING);
            return;
        } // Automatically set the state based on remaining
        Book.BookState state;
        if (remaining == 0) {
            state = Book.BookState.unavailable;
        } else {
            state = Book.BookState.available;
        }
        Book book = new Book(0, title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);
        try {
            bookdao.insert(book);
            bookshelfSound();
            showAlert(null, null, "Book added successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            showAlert(null, null, "Failed to add book.", Alert.AlertType.ERROR);
        }
    }

    private void showSuggestionsPopup(List<BookSuggestion> suggestions) {
        if(currentContextMenu != null && currentContextMenu.isShowing()){
            currentContextMenu.hide();
        }
        ContextMenu contextMenu = new ContextMenu();
        int maxSuggestions = 4;
        for (int i = 0; i< Math.min(suggestions.size(),maxSuggestions); i++) {
            BookSuggestion suggestion = suggestions.get(i);
            MenuItem item = new MenuItem(suggestion.getTitle());
            item.setOnAction(_ -> selectBookSuggestion(suggestion));
            contextMenu.getItems().add(item);
        }
        currentContextMenu = contextMenu;
        contextMenu.show(searchBar, searchBar.getScene().getWindow().getX() + searchBar.getLayoutX(),
                searchBar.getScene().getWindow().getY() + searchBar.getLayoutY() + searchBar.getHeight());
    }

    private void selectBookSuggestion(BookSuggestion suggestion) {
        bookTitleTextField.setText(suggestion.getTitle());
        authorTextField.setText(suggestion.getAuthor());
        bookISBNTextField.setText(suggestion.getIsbn());
        publisherTextField.setText((suggestion.getPublishedDate() != null ?
                dateFormat.format(suggestion.getPublishedDate()) : ""));
        if (suggestion.getThumbnail() != null) {
            Image image = new Image(suggestion.getThumbnail());
            imageImageView.setImage(image);
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
