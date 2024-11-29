package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BorrowedBooksController extends AdminTemplateController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button homeButton, booksButton, readersButton, logOutButton;
    @FXML
    private Label allBorrowedBooksLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<BorrowedBook> borrowedBooksTable;
    @FXML
    private TableColumn<BorrowedBook, Integer> borrowedIDColumn;
    @FXML
    private TableColumn<BorrowedBook, String> bookISBNColumn;
    @FXML
    private TableColumn<BorrowedBook, String> bookTitleColumn;
    @FXML
    private TableColumn<BorrowedBook, Integer> readerIDColumn;
    @FXML
    private TableColumn<BorrowedBook, String> readerNameColumn;
    @FXML
    private TableColumn<BorrowedBook, Date> borrowedDateColumn;
    @FXML
    private TableColumn<BorrowedBook, Date> returnDateColumn;
    @FXML
    private TableColumn<BorrowedBook, Integer> borrowedDayColumn;
    @FXML
    private TableColumn<BorrowedBook, Double> lateFeeColumn;

    private ObservableList<BorrowedBook> borrowedBooksList;

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        borrowedIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookISBNColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        readerIDColumn.setCellValueFactory(new PropertyValueFactory<>("readerID"));
        readerNameColumn.setCellValueFactory(new PropertyValueFactory<>("readerName"));
        borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        borrowedDayColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDay"));
        lateFeeColumn.setCellValueFactory(new PropertyValueFactory<>("lateFee"));

        loadBorrowedBooksDataFromDatabase();
        addSearchFunctionality();
    }

    @FXML
    void booksButtonClicked() throws IOException {
        switchToBooksView(booksButton);
    }

    @FXML
    void homeButtonClicked(ActionEvent event) throws IOException {
        switchToDashboardView(homeButton);
    }

    @FXML
    void readersButtonClicked(ActionEvent event) throws IOException {
        switchToReadersView(readersButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchToLoginView(logOutButton);
    }

    private void loadBorrowedBooksDataFromDatabase() {
        String query = "SELECT id, isbn, title, readerID," +
                " readerName, borrowedDate, returnDate,borrowedDay, "
                +"lateFee FROM borrowedBooks";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            borrowedBooksList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String isbn  = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                int readerID = resultSet.getInt("readerID");
                String readerName = resultSet.getString("readerName");
                Date borrowedDate = resultSet.getDate("borrowedDate");
                java.sql.Date returnDate = resultSet.getDate("returnDate");
                int borrowedDay = resultSet.getInt("borrowedDay");
                double lateFee = resultSet.getDouble("lateFee");
                BorrowedBook borrowedBook = new BorrowedBook(id, isbn, title, readerID, readerName, borrowedDate, returnDate, borrowedDay, lateFee);
                borrowedBooksList.add(borrowedBook);
            }
            FilteredList<BorrowedBook> filteredList = new FilteredList<>(borrowedBooksList, p -> true);
            borrowedBooksTable.setItems(filteredList);
            borrowedBooksTable.setItems(borrowedBooksList); // Update the total number of borrowed books
            allBorrowedBooksLabel.setText(String.valueOf(borrowedBooksList.size()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSearchFunctionality() {
        FilteredList<BorrowedBook> filteredList = new FilteredList<>(borrowedBooksList, p -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(borrowedBook -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (borrowedBook.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches book title
                } else if (borrowedBook.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches book ISBN
                } else if (borrowedBook.getReaderName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        borrowedBooksTable.setItems(filteredList);
    }
}
