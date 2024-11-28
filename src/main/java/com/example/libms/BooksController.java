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

public class BooksController extends AdminTemplateController {

    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button homeButton, readersButton, borrowedBooksButton, logOutButton;
    @FXML
    private Button addBookButton, updateBookButton, viewBookButton, deleteBookButton;
    @FXML
    private Label allBooksLabel;
    @FXML
    private TextField searchBar;
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

    private Book selectedBook;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        allBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        setBooksTable();
        searchBar.setOnKeyReleased(this::searchBooks);
        booksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedBook = newValue;
        });
    }

    @FXML
    void homeButtonClicked() throws IOException {
        switchToDashboardView(homeButton);
    }

    @FXML
    void readersButtonClicked() throws IOException {
        switchToReadersView(readersButton);
    }

    @FXML
    void borrowedBooksButtonClicked() throws IOException {
        switchToBorrowedBooksView(borrowedBooksButton);
    }

    @FXML
    void logOutButtonClicked() throws IOException {
        switchToLoginView(logOutButton);
    }

    /**
     * Handles the event when the "Add Book" button is clicked in the administrator's view.
     *
     * This method is responsible for opening*/
    @FXML
    void addBookButtonClicked() throws IOException {
        bookshelfSound();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView/addBooksInBooks-view.fxml"));
        DialogPane dialogPane = loader.load();

        AddBooksController controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Book Details");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK || result.get() == ButtonType.CANCEL)) {
            dialog.close();
            this.loadBooks();
            allBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        }
    }

    /**
     * Handles the event when the "Update Book" button is clicked.
     *
     * This method checks if a book is selected in the `booksTable`. If a book is
     * selected, a dialog is opened to update the book information using
     * `UpdateBooksController`. The method sets up the dialog and passes the
     * selected book to the `UpdateBooksController`. If the book update is
     * successful, it reloads the book list to reflect changes; otherwise, it
     * prompts the user to check the input.
     *
     * @throws IOException if there is an issue loading the FXML resource for the
     *                     dialog.
     */
    @FXML
    void updateBookButtonClicked() throws IOException {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookFlipSound();
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
               boolean success =  updateBookController.saveUpdatedBook();
               if(success) {
                   bookshelfSound();
                   this.loadBooks();
               }else {
                   dialog.showAndWait();
               }
            }
        } else {
            alertSoundPlay();
            showAlert("No selection", "No Book Selected",
                             "Please select a book to update", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles the event when the "View Book" button is clicked.
     *
     * This method opens a detailed view of the selected book's information in a dialog.
     * If a book is selected from the*/
    @FXML
    void viewBookButtonClicked() throws IOException {
        if (selectedBook != null) {
            bookFlipSound();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView/viewBooksInBooks-view.fxml"));
            DialogPane dialogPane = loader.load();

            ViewBooksController controller = loader.getController();
            controller.setBookData(selectedBook);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("View Book Details");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK || result.get() == ButtonType.CANCEL)) {
                dialog.close();
            }

        } else {
            alertSoundPlay();
            showAlert("No selection", "No Book Selected",
                                "Please select a book to view its details.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles the event when the "Delete Book" button is clicked.
     *
     * This method is responsible for deleting the selected book from the books table.
     * The user is prompted with a confirmation dialog to ensure the deletion of the book.
     * If confirmed, it deletes the book using the `BookDAO` and updates the table to
     * reflect the changes. If the deletion is successful, a success alert is shown;
     * otherwise, an error alert is presented. If no book is selected, a warning alert is displayed.
     */
    @FXML
    private void deleteBookButtonClicked() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            playButtonClickSound2();
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
                    showAlert("Delete Success", null,
                                        "The book was successfully deleted.", Alert.AlertType.INFORMATION);
                } else {
                    alertSoundPlay();
                    showAlert("Delete Failed", null,
                                        "Failed to delete the book. Please try again.", Alert.AlertType.ERROR);
                }
            }
        } else {
            alertSoundPlay();
            showAlert("No selection", null, "Please select a book to delete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void loadBooks() {
        bookList.clear();
        loadBooksDataFromDatabase();
    }

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

    /**
     * Loads all book data from the database and populates the list of books.
     * This method executes a SQL query to fetch all records from the "books"
     * table in the database, then iterates over the results to create
     * Book objects using the retrieved data. Each Book object is added to
     * a list, which is then set to the table view for display.
     *
     * The method uses a try-with-resources statement to ensure that the
     * database connection, prepared statement, and result set are closed
     * automatically after use.
     *
     * SQL exceptions encountered during the operation will result in a
     * runtime exception being thrown.
     *
     * The columns fetched from the database include:
     * - bookID: an identifier for the book
     * - title: the title of the book
     * - author: the author of the book
     * - publisher: the publisher of the book
     * - isbn: the ISBN number of the book
     * - publishedDate: the date when the book was published
     * - edition: the edition number of the book
     * - quantity: the total quantity of the book in stock
     * - state: the availability state of the book, either 'available' or 'unavailable'
     * - remaining: the remaining quantity of the book in stock after lending
     */
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
}
