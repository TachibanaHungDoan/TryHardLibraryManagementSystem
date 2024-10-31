package com.example.libms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UpdateBooksController {

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField bookISBNTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private TextField editionTextField;

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

    private Book book;
    private final BookDAO bookDao = new BookDAO();
    private BooksController booksController;

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
    public void saveUpdatedBook() {
        // Update book object with data from text fields
        book.setTitle(bookTitleTextField.getText());
        book.setAuthor(authorTextField.getText());
        book.setPublisher(publisherTextField.getText());
        book.setIsbn(bookISBNTextField.getText());

        try {
            book.setPublishedDate(java.sql.Date.valueOf(publishedDateTextField.getText())); // Converts String to SQL Date
            book.setEdition(Integer.parseInt(editionTextField.getText()));
            book.setQuantity(Integer.parseInt(quantityTextField.getText()));
            book.setState(Book.BookState.valueOf(stateTextField.getText().toLowerCase())); // Assumes State is an Enum
            book.setRemaining(Integer.parseInt(remainingTextField.getText()));

            // Call the update method from BookDao
            int rowsUpdated = bookDao.update(book);

            if (rowsUpdated > 0) {
                System.out.println("Book updated successfully!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Success");
                alert.setHeaderText("Book Updated Successfully");
                alert.setContentText("The book was updated successfully.");
                alert.showAndWait();
                if (booksController != null) {
                    booksController.loadBooks();
                }
            } else {
                // Show alert for no update
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText("No Book Updated");
                alert.setContentText("No book was updated. Please check the input.");
                alert.showAndWait();
            }
        }catch(Exception e){
                e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setHeaderText("Error Updating Book");
            alert.setContentText("There was an error updating the book. Please check the input fields.");
            alert.showAndWait();
        }
    }
}
