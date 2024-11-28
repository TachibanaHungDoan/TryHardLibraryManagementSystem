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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class RBooksInventoryBorrowController extends SceneController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button dashBoardButton, allBooksButton, gamesButton, logOutButton;
    @FXML
    private Button returnedBooksSceneSwitchButton, returnBookButton;
    @FXML
    private TableView<BorrowedBookEx> borrowedBooksTable;
    @FXML
    private TableColumn<BorrowedBookEx, Integer> bookIDColumn;
    @FXML
    private TableColumn<BorrowedBookEx, String> titleColumn;
    @FXML
    private TableColumn<BorrowedBookEx, String> authorColumn;
    @FXML
    private TableColumn<BorrowedBookEx, String> publisherColumn;
    @FXML
    private TableColumn<BorrowedBookEx, String> isbnColumn;
    @FXML
    private TableColumn<BorrowedBookEx, Date> publishedDateColumn;
    @FXML
    private TableColumn<BorrowedBookEx, Date> borrowedDateColumn;

    private ObservableList<BorrowedBookEx> borrowedBooksList = FXCollections.observableArrayList();

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
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void rAllBooksButtonClicked(ActionEvent event) throws IOException {
        bookFlipSound();
        switchScene("ReaderView/rALlBooks-view.fxml", allBooksButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound1();
        switchScene("ReaderView/rDashBoard-view.fxml",dashBoardButton);
    }

    @FXML
    void returnBookButtonClicked(ActionEvent event) {
        BorrowedBookEx selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            returnBook(selectedBook);
            loadBorrowedBooksDataFromDatabase();
        } else {
            alertSoundPlay();
            showAlert("No Selection", "No Book Selected",
                    "Please select a book to return.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void returnedBooksSwitchSceneButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
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
                BorrowedBookEx book = new BorrowedBookEx(id,title,isbn,author,publisher,publishedDate,borrowedDate);
                borrowedBooksList.add(book);
            }
            borrowedBooksTable.setItems(borrowedBooksList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBook(BorrowedBookEx book) {
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
                showAlert("Success", "Book Returned", "You have successfully returned the book.", Alert.AlertType.INFORMATION);
            }catch (SQLException e) {
                e.printStackTrace();
                alertSoundPlay();
                showAlert("Database Error", "Error Returning Book",
                    "There was an error returning the book. Please try again.", Alert.AlertType.ERROR);
            }
    }
}
