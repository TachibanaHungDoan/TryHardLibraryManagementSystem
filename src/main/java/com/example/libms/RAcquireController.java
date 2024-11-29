package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class RAcquireController {
    @FXML
    private Button cancelButton, confirmButton, dFCButton;
    @FXML
    private Label totalBooksLabel, dueDateLabel;
    @FXML
    private TableView<Book> cartTable;
    @FXML
    private TableColumn<Book, Integer> bookIDColumn;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, String> bookPublisherColumn;
    @FXML
    private TableColumn<Book, String> bookISBNColumn;

    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private AlertShowing alertShowing = new AlertShowing();

    @FXML
    void initialize() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookPublisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookISBNColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        cartTable.setItems(Cart.getBooksInCart());
        totalBooksLabel.setText(String.valueOf(Cart.getBooksInCart().size()));
        dueDateLabel.setText(setDueDate());
    }

    @FXML
    void cancelButtonClicked(ActionEvent Event) throws IOException {
        soundButtonController.playButtonClickSound2();
        /*switchScene("ReaderView/rAllBooks-view.fxml", cancelButton);*/
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action when the confirm button is clicked. This method processes the list of books in the
     * cart and carries out the borrowing procedure by checking the availability of each book, ensuring that
     * the user hasn't already borrowed the book, and updating the necessary database records. If successful,
     * it clears the cart and updates the UI accordingly.
     *
     * @param event The event that triggered the action.
     */
    @FXML
    void confirmButtonClicked(ActionEvent event) {
        List<Book> booksInCart = Cart.getBooksInCart();
        if (booksInCart.isEmpty()) {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Cart is empty", "No books to confirm", "Please add books to the cart before confirming.", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkStateSQL = "SELECT state, remaining FROM books WHERE isbn = ?";
            String checkBorrowedSQL = "SELECT COUNT(*) FROM borrowedBooks WHERE isbn = ? AND readerID = ?";
            String insertSQL = "INSERT INTO borrowedBooks (isbn, title, readerID, readerName, borrowedDate, returnDate, borrowedDay, lateFee) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String updateSQL = "UPDATE books SET remaining = remaining - 1, state = CASE WHEN remaining = 0 THEN 'unavailable' ELSE state END WHERE isbn = ?";

            for (Book book : booksInCart) {
                try (PreparedStatement pstmCheckState = conn.prepareStatement(checkStateSQL);
                     PreparedStatement pstmCheckBorrowed = conn.prepareStatement(checkBorrowedSQL);
                     PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL);
                     PreparedStatement pstmtUpdate = conn.prepareStatement(updateSQL)) {

                    pstmCheckState.setString(1, book.getIsbn());
                    ResultSet rs = pstmCheckState.executeQuery();
                    if (rs.next()) {
                        String state = rs.getString("state");
                        int remaining = rs.getInt("remaining");
                        if ("unvailable".equals(state) || remaining <= 0) {
                            soundButtonController.alertSoundPlay();
                            alertShowing.showAlert("Book Unavailable","Book Not Available","The book " + book.getTitle() + " is not available for borrowing.",Alert.AlertType.WARNING);
                            return;
                        }
                    }
                    pstmCheckBorrowed.setString(1,book.getIsbn());
                    pstmCheckBorrowed.setInt(2,LoggedInUser.getReaderID());
                    ResultSet rsBorrowed = pstmCheckBorrowed.executeQuery();
                    if (rsBorrowed.next() && rsBorrowed.getInt(1) > 0) {
                        soundButtonController.alertSoundPlay();
                        alertShowing.showAlert("Duplicate Borrowing","Book Already Borrowed","You have already borrowed the book " + book.getTitle() + ".", Alert.AlertType.WARNING);
                        Cart.removeBookFromCart(book);
                        cartTable.setItems(Cart.getBooksInCart());
                        totalBooksLabel.setText(String.valueOf(Cart.getBooksInCart().size()));
                        return;
                    }
                    pstmtInsert.setString(1, book.getIsbn());
                    pstmtInsert.setString(2, book.getTitle());
                    pstmtInsert.setInt(3, LoggedInUser.getReaderID());
                    pstmtInsert.setString(4, LoggedInUser.getReaderName());

                    LocalDate borrowedDate = LocalDate.now();
                    LocalDate returnDate = borrowedDate.plus(1, ChronoUnit.MONTHS);

                    pstmtInsert.setDate(5, java.sql.Date.valueOf(borrowedDate));
                    pstmtInsert.setDate(6, java.sql.Date.valueOf(returnDate));
                    pstmtInsert.setInt(7, (int) ChronoUnit.DAYS.between(borrowedDate, returnDate));
                    pstmtInsert.setInt(8, 0); // Late fee set to 0

                    pstmtInsert.executeUpdate();

                    // Update remaining in books
                    pstmtUpdate.setString(1, book.getIsbn());
                    pstmtUpdate.executeUpdate();
                }
            }

            Cart.clearCart();
            cartTable.setItems(Cart.getBooksInCart());
            totalBooksLabel.setText(String.valueOf(Cart.getBooksInCart().size()));
            soundButtonController.bookFlipSound();
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
            alertShowing.showAlert("Success", "Books Borrowed", "Books have been successfully borrowed.", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            e.printStackTrace();
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("Database Error", "Error Borrowing Books", "There was an error borrowing the books. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void deleteFromCartButtonClicked(ActionEvent event) {
        Book selectedBook = cartTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            soundButtonController.playButtonClickSound2();
            Cart.removeBookFromCart(selectedBook);
            cartTable.setItems(Cart.getBooksInCart());
            totalBooksLabel.setText(String.valueOf(Cart.getBooksInCart().size()));
        } else {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("No selection", "No Book Selected", "Please select a book to remove from the cart.", Alert.AlertType.WARNING);
        }
    }

    private String setDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusMonths(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dueDateDate = Date.from(dueDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        return dateFormat.format(dueDateDate);
    }
}
