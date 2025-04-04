package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.Date;

public class UpdateBooksController {
    @FXML
    private TextField bookTitleTextField, authorTextField, publisherTextField;
    @FXML
    private TextField publishedDateTextField, bookISBNTextField, editionTextField;
    @FXML
    private TextField quantityTextField, stateTextField, remainingTextField;

    private Book book;
    private final BookDAO bookDao = new BookDAO();
    private BooksController booksController;

    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private AlertShowing alertShowing = new AlertShowing();

    public void setBookData(Book book) {
        this.book = book;
        bookTitleTextField.setText(book.getTitle());
        authorTextField.setText(book.getAuthor());
        publisherTextField.setText(book.getPublisher());
        bookISBNTextField.setText(book.getIsbn());
        publishedDateTextField.setText(book.getPublishedDate().toString());
        editionTextField.setText(String.valueOf(book.getEdition()));
        quantityTextField.setText(String.valueOf(book.getQuantity()));
        stateTextField.setText(book.getState().name());
        remainingTextField.setText(String.valueOf(book.getRemaining()));
    }

    public void setBooksController(BooksController booksController) {
        this.booksController = booksController;
    }

    // Method to save updated book data
    @FXML
    public boolean saveUpdatedBook() {
        // Update book object with data from text fields

        String title = bookTitleTextField.getText();
        String author = authorTextField.getText();
        String publisher = publisherTextField.getText();
        String publishedDateStr = publishedDateTextField.getText();
        String isbn = bookISBNTextField.getText();
        String editionStr = editionTextField.getText();
        String quantityStr = quantityTextField.getText();
        String remainingStr = remainingTextField.getText();
        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishedDateStr.isEmpty() || isbn.isEmpty() || editionStr.isEmpty() || quantityStr.isEmpty() || remainingStr.isEmpty()) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Update Error", "Invalid Input", "All fields must be filled.", Alert.AlertType.WARNING);
            return false;
        }
        Date publishedDate;
        try {
            publishedDate = java.sql.Date.valueOf(publishedDateStr);
            // Check if published date is in the future
            if (publishedDate.after(new Date())) {
                soundButtonController.alertSoundPlay();
                alertShowing.showAlert("Update Error", "Invalid Input", "Published date cannot be in the future.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (IllegalArgumentException e) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Update Error", "Invalid Input", "Invalid date format.", Alert.AlertType.ERROR);
            return false;
        }
        int edition, quantity, remaining;
        try {
            edition = Integer.parseInt(editionStr);
            quantity = Integer.parseInt(quantityStr);
            remaining = Integer.parseInt(remainingStr);
        } catch (NumberFormatException e) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Update Error", "Invalid Input", "Edition, Quantity, and Remaining must be valid integers.", Alert.AlertType.ERROR);
            return false;
        } // Check if remaining is greater than quantity
        if (remaining > quantity) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Update Error", "Invalid Input", "Remaining cannot be greater than Quantity.", Alert.AlertType.WARNING);
            return false;
        } // Automatically set the state based on remaining
        Book.BookState state;
        if (remaining == 0) {
            state = Book.BookState.unavailable;
        } else {
            state = Book.BookState.available;
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setIsbn(isbn);
        book.setPublishedDate(publishedDate);
        book.setEdition(edition);
        book.setQuantity(quantity);
        book.setState(state);
        book.setRemaining(remaining);
        try {
            // Call the update method from BookDao
            int rowsUpdated = bookDao.update(book);

            if (rowsUpdated > 0) {
                System.out.println("Book updated successfully!");
                alertShowing.showAlert("Update Success", "Book Updated Successfully", "The book was updated successfully", Alert.AlertType.INFORMATION);
                if (booksController != null) {
                    booksController.loadBooks();
                }
                return true;
            } else {
                soundButtonController.alertSoundPlay();
                alertShowing.showAlert("Update Failed", "No Book Updated", "Please check the input.", Alert.AlertType.ERROR);
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Update Error", "Error Updating Book", "Please check the input.", Alert.AlertType.ERROR);
            return false;
        }
    }
}
