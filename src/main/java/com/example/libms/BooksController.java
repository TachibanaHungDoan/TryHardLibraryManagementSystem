package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BooksController {
    @FXML
    private Button addBookButton;

    @FXML
    private Label allBooksLabel;

    @FXML
    private Button booksButton;

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, Integer> bookIDColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> ISBNColumn;
    @FXML
    private TableColumn<Book, Date> publishedDateColumn;
    @FXML
    private TableColumn<Book, Integer> editionColumn;
    @FXML
    private TableColumn<Book, String> quantityColumn;
    @FXML
    private TableColumn<Book, Integer> stateColumn;
    @FXML
    private TableColumn<Book, Integer> remainingColumn;
    @FXML
    private Button borrowedBooksButton;

    @FXML
    private Button deleteBookButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button readersButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Label timeLabel;

    @FXML
    private Button updateBookButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button viewBookButton;

    @FXML
    void initialize() {
        SceneController.setUpScene(usernameLabel, timeLabel);
        allBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        setBooksTable();
        searchBar.setOnKeyReleased(this::searchBooks);
    }
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    private void setBooksTable() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        ISBNColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));

        loadBooksDataFromDatabase();
    }

    private void searchBooks(KeyEvent event) {
        String keyword = searchBar.getText().toLowerCase();

        // Filter the list based on the keyword
        List<Book> filteredBooks = bookList.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword) ||
                        String.valueOf(book.getId()).toLowerCase().contains(keyword) ||
                        book.getIsbn().contains(keyword))
                .collect(Collectors.toList());

        booksTable.setItems(FXCollections.observableArrayList(filteredBooks));
    }

    private int getTotalBooksFromDatabase() {
        int totalBooks = 0;
        String query = "SELECT COUNT(*) AS total FROM books";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalBooks = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalBooks;
    }

    private void loadBooksDataFromDatabase() {
        String query = "SELECT * FROM books";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("bookID");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                String isbn = resultSet.getString("isbn");
                Date publishedDate = resultSet.getDate("publishedDate");
                int edition = resultSet.getInt("edition");
                int quantity = resultSet.getInt("quantity");

                String stateString = resultSet.getString("state");
                Book.BookState state = Book.BookState.valueOf(stateString.toLowerCase());

                int remaining = resultSet.getInt("remaining");
                Book book = new Book(id,title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);
                bookList.add(book);
            }
            booksTable.setItems(bookList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void homeButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/dashBoard-view.fxml", homeButton);
    }

    @FXML
    void booksButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/books-view.fxml", booksButton);
    }

    @FXML
    void readersButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/readers-view.fxml", readersButton);
    }

    @FXML
    void borrowedBooksButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/borrowedBooks-view.fxml", borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        SceneController.switchSceneWithAlert("login-view.fxml", logOutButton
                , null, null
                ,"Do you want to log out?", Alert.AlertType.CONFIRMATION);
    }

    @FXML
    void addBookButtonClicked() throws IOException {
        SceneController.switchScene("AdminView/addBooks-view.fxml", addBookButton);
    }

    @FXML
    void updateBookButtonClicked() throws IOException {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView/updateBooksInBooks-view.fxml"));
            DialogPane dialogPane = loader.load();

            UpdateBooksController updateBookController = loader.getController();
            updateBookController.setBookData(selectedBook);

            //pass the BooksController to the UpdateBooksController
            updateBookController.setBooksController(this);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Update Book");

            // Show dialog and wait for user action
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                updateBookController.saveUpdatedBook();
                this.loadBooks();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to update.");
            alert.showAndWait();
        }
        //SceneController.openDialogPane("AdminView/updateBooksInBooks-view.fxml", "UpdateBooks", updateBookButton);
    }

    @FXML
    void viewBookButtonClicked() throws IOException {
        SceneController.openDialogPane("AdminView/viewBooksInBooks-view.fxml", "ViewBook", viewBookButton);
    }
    @FXML
    private void deleteBookButtonClicked() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Book");
            confirmAlert.setHeaderText("Are you sure you want to delete the selected book?");
            confirmAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                BookDAO bookDAO = new BookDAO();

                // Assume selectedBook has an accessible field `id` as the primary key
                int deleteResult = bookDAO.deleteBookById(selectedBook.getId());

                if (deleteResult > 0) {
                    loadBooks();  // Reload table to show updated book list
                    allBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Delete Success");
                    successAlert.setContentText("The book was successfully deleted.");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Delete Failed");
                    errorAlert.setContentText("Failed to delete the book. Please try again.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setContentText("Please select a book to delete.");
            warningAlert.showAndWait();
        }
    }
    @FXML
    public void loadBooks() {
        bookList.clear();
        loadBooksDataFromDatabase();
    }
}
