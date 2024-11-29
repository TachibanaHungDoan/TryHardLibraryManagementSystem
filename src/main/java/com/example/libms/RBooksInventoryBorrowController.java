package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class RBooksInventoryBorrowController extends ReaderTemplateController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button dashBoardButton, allBooksButton, gamesButton, logOutButton;
    @FXML
    private Button returnedBooksSceneSwitchButton, returnBookButton;
    @FXML
    private TableView<RBorrowedBook> borrowedBooksTable;
    @FXML
    private TableColumn<RBorrowedBook, Integer> bookIDColumn;
    @FXML
    private TableColumn<RBorrowedBook, String> titleColumn;
    @FXML
    private TableColumn<RBorrowedBook, String> authorColumn;
    @FXML
    private TableColumn<RBorrowedBook, String> publisherColumn;
    @FXML
    private TableColumn<RBorrowedBook, String> isbnColumn;
    @FXML
    private TableColumn<RBorrowedBook, Date> publishedDateColumn;
    @FXML
    private TableColumn<RBorrowedBook, Date> borrowedDateColumn;

    private ObservableList<RBorrowedBook> borrowedBooksList = FXCollections.observableArrayList();
    private SoundButtonController soundButtonController = SoundButtonController.getInstance();
    private AlertShowing alertShowing = new AlertShowing();

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        loadBorrowedBooksDataFromDatabase();
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchToLoginView(logOutButton);
    }

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        switchToAllBooksView(allBooksButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        switchToGameView(gamesButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        switchToDashBoardView(dashBoardButton);
    }

    @FXML
    void returnBookButtonClicked(ActionEvent event) {
        RBorrowedBook selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            returnBook(selectedBook);
            loadBorrowedBooksDataFromDatabase();
        } else {
            soundButtonController.alertSoundPlay();
            alertShowing.showAlert("No Selection", "No Book Selected",
                    "Please select a book to return.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void returnedBooksSwitchSceneButtonClicked(ActionEvent event) throws IOException {
        soundButtonController.playButtonClickSound2();
        switchScene("ReaderView/rBooksInventoryReturn-view.fxml", returnedBooksSceneSwitchButton);
    }

    private void loadBorrowedBooksDataFromDatabase() {
        String query = "SELECT b.id, b.isbn, b.title, bk.author, bk.publisher,bk.publishedDate, b.borrowedDate "
                + "FROM borrowedBooks b "
                + "JOIN books bk ON b.isbn = bk.isbn "
                + "WHERE b.readerID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, LoggedInUser.getReaderID());
            ResultSet resultSet = statement.executeQuery();
            borrowedBooksList.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String isbn = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                Date publishedDate = resultSet.getDate("publishedDate");
                java.sql.Date borrowedDate = resultSet.getDate("borrowedDate");
                RBorrowedBook book = new RBorrowedBook(id,title,isbn,author,publisher,publishedDate,borrowedDate);
                borrowedBooksList.add(book);
            }
            borrowedBooksTable.setItems(borrowedBooksList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBook(RBorrowedBook book) {
        String deleteSQL = "DELETE FROM borrowedBooks WHERE isbn = ? AND readerID = ?";
        String updateSQL = "UPDATE books SET remaining = remaining + 1, state = 'available' WHERE isbn = ?";
        String insertReturnSQL = "INSERT INTO readerReturnedBooks " +
                "(title, isbn, returnedDate, borrowedDay, lateFee, readerID) VALUES (?, ?, ?, ?, ?, ?)";
            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pstmtDelete = conn.prepareStatement(deleteSQL);
                    PreparedStatement pstmtUpdate = conn.prepareStatement(updateSQL);
                    PreparedStatement pstmtInsertReturn = conn.prepareStatement(insertReturnSQL)) {
                LocalDate borrowedLocalDate = new java.sql.Date(book.getBorrowedDate().getTime()).toLocalDate();
                LocalDate returnedDate = LocalDate.now();
                long borrowedDays = ChronoUnit.DAYS.between(borrowedLocalDate, returnedDate);
                double lateFee = borrowedDays > 30 ? (borrowedDays - 30) * 2 : 0;
                pstmtInsertReturn.setString(1, book.getTitle());
                pstmtInsertReturn.setString(2, book.getIsbn());
                pstmtInsertReturn.setDate(3, java.sql.Date.valueOf(returnedDate));
                pstmtInsertReturn.setLong(4, borrowedDays);
                pstmtInsertReturn.setDouble(5, lateFee);
                pstmtInsertReturn.setInt(6, LoggedInUser.getReaderID());
                int rowsInserted = pstmtInsertReturn.executeUpdate();
                System.out.println("Rows inserted into readerReturnedBooks: " + rowsInserted); // Ghi log số bản ghi được chèn
                pstmtUpdate.setString(1, book.getIsbn());
                pstmtUpdate.executeUpdate();
                pstmtDelete.setString(1, book.getIsbn());
                pstmtDelete.setInt(2, LoggedInUser.getReaderID());
                pstmtDelete.executeUpdate();
                alertShowing.showAlert("Success", "Book Returned", "You have successfully returned the book.", Alert.AlertType.INFORMATION);
            }catch (SQLException e) {
                e.printStackTrace();
                soundButtonController.alertSoundPlay();
                alertShowing.showAlert("Database Error", "Error Returning Book",
                    "There was an error returning the book. Please try again.", Alert.AlertType.ERROR);
            }
    }
}
