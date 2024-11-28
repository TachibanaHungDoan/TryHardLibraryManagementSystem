package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RAllBooksController extends SceneController {
    @FXML
    private Label usernameLabel, timeLabel;
    @FXML
    private Button dashBoardButton, booksInventoryButton, gamesButton, logOutButton;
    @FXML
    private Button acquireButton, addToCartButton, viewBookButton;
    @FXML
    private TableColumn<Book, String> ISBNColumn;
    @FXML
    private Label allBooksLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> bookIDColumn;
    @FXML
    private TableColumn<Book, Integer> editionColumn;
    @FXML
    private TableColumn<Book, Date> publishedDateColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TableColumn<Book, Integer> remainingColumn;
    @FXML
    private TableColumn<Book, Enum> stateColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private Book selectedBook;

    @FXML
    void initialize() {
        setUpScene(usernameLabel, timeLabel);
        allBooksLabel.setText(String.valueOf(getTotalBooksFromDatabase()));
        booksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedBook = newValue;
        });
        setBooksTable();
    }

    @FXML
    void rBooksInventoryButtonClicked(ActionEvent event) throws IOException {
        bookshelfSound();
        switchScene("ReaderView/rBooksInventoryBorrow-view.fxml", booksInventoryButton);
    }

    @FXML
    void rGamesButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        switchScene("ReaderView/rGame-view.fxml", gamesButton);
    }

    @FXML
    void rHomeButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound1();
        switchScene("ReaderView/rDashBoard-view.fxml", dashBoardButton);
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) throws IOException {
        switchSceneWithAlert("LoginView/login-view.fxml", logOutButton,
                null, null, "Do you want to log out?", Alert.AlertType.CONFIRMATION);
        logOutSound();
    }

    @FXML
    void rViewBookButtonClicked(ActionEvent event) throws IOException {
        if (selectedBook != null) {
            bookFlipSound();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/viewBooks-view.fxml"));
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

    @FXML
    void rAcquireButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        /*switchScene("ReaderView/rAcquireInAllBooks-view.fxml",acquireButton);*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReaderView/rAcquireInAllBooks-view.fxml"));
        DialogPane dialogPane = loader.load();

        RAcquireController controller = loader.getController();

        Dialog<Void> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Cart");
        dialog.showAndWait();
        loadBooks();
    }

    @FXML
    void addToCartButtonClicked(ActionEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            if (Cart.getBooksInCart().contains(selectedBook)) {
                alertSoundPlay();
                showAlert("Duplicate Book", "Book Already in Cart",
                        "This book is already in your cart.", Alert.AlertType.WARNING);
            } else {
                bookshelfSound();
                Cart.addBookToCart(selectedBook);
                showAlert("Successful", "Successful adding to Cart",
                        "press Acquire to see your Cart", Alert.AlertType.INFORMATION);
            }
        }
        else {
            alertSoundPlay();
            showAlert("No selection", "No Book Selected",
                    "Please select a book to add to the cart.", Alert.AlertType.WARNING);
        }
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
        searchBar.setOnKeyReleased(this::searchBooks);
    }

    public void loadBooksDataFromDatabase() {
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

    public void loadBooks() {
        bookList.clear();
        loadBooksDataFromDatabase();
    }
}
