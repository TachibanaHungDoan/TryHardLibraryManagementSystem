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
    private boolean shouldQueryAPI = true;

    private BookDAO bookdao = new BookDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ContextMenu currentContextMenu;

    /**
     * Initializes the controller class by setting up event handlers and listeners for the UI components.
     * This method configures the following behavior:
     * - The importImageButton will trigger the image import function when clicked.
     * - The addButton will trigger the process of adding a book when clicked.
     * - The clearButton will clear all input fields when clicked.
     * - The searchBar has a text listener to provide book suggestions from a cache or via Google Books API.
     * The search bar listens for text changes and, if the new value is not empty, checks the cache for suggestions.
     * If suggestions are not found in the cache, a background thread fetches suggestions from the Google Books API.
     * Once fetched, suggestions are shown using a popup.
     */
    @FXML
    public void initialize() {
        importImageButton.setOnAction(event -> importImage());
        addButton.setOnAction(event -> addBook());
        clearButton.setOnAction(event -> clearButtonClicked());
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (shouldQueryAPI && !newValue.isEmpty()) {
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

    private void showSuggestionsPopup(List<BookSuggestion> suggestions) {
        if(currentContextMenu != null && currentContextMenu.isShowing()){
            currentContextMenu.hide();
        }
        ContextMenu contextMenu = new ContextMenu();
        int maxSuggestions = 3;
        for (int i = 0; i< Math.min(suggestions.size(),maxSuggestions); i++) {
            BookSuggestion suggestion = suggestions.get(i);
            MenuItem item = new MenuItem(suggestion.getTitle());
            item.setOnAction(_ -> {
                shouldQueryAPI = false;
                selectBookSuggestion(suggestion); });
            contextMenu.getItems().add(item);
        }
            if (searchBar.getScene() != null && searchBar.getScene().getWindow() != null) {
                currentContextMenu = contextMenu;
            contextMenu.show(searchBar, searchBar.getScene().getWindow().getX() + searchBar.getLayoutX(),
                    searchBar.getScene().getWindow().getY() + searchBar.getLayoutY() + searchBar.getHeight());
            }
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
        shouldQueryAPI = true;
    }

    /**
     * Adds a new book entry using the information provided from user input fields.
     * The method performs various checks before a book is added:
     * 1. Ensures all required fields are completed.
     * 2. Verifies the published date is in the correct format.
     * 3. Checks that the edition, quantity, and remaining fields are valid integers.
     * 4. Ensures that the remaining count is not greater than the available quantity.
     *
     **/
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


    /**
     * Selects a book suggestion and populates the corresponding text fields and image view
     * with the data from the specified BookSuggestion object.
     *
     * @param suggestion the BookSuggestion object containing book details such as title, author, publisher,
     *                   ISBN, published date, and thumbnail image. This method extracts these details from
     *                   the suggestion and displays them in the user interface.
     */
    private void selectBookSuggestion(BookSuggestion suggestion) {
        bookTitleTextField.setText(suggestion.getTitle());
        authorTextField.setText(suggestion.getAuthor());
        bookISBNTextField.setText(suggestion.getIsbn());
        publisherTextField.setText(suggestion.getPublisher());
        publishedDateTextField.setText((suggestion.getPublishedDate() != null ?
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
