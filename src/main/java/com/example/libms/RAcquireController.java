package com.example.libms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class RAcquireController extends SceneController {
    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button dFCButton;

    @FXML
    private Label dueDateLabel;

    @FXML
    private Label totalBooksLabel;

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

    @FXML
    void initialize() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookPublisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookISBNColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        cartTable.setItems(Cart.getBooksInCart());
        totalBooksLabel.setText(String.valueOf(Cart.getBooksInCart().size()));
    }

    @FXML
    void cancelButtonClicked(ActionEvent event) throws IOException {
        playButtonClickSound2();
        /*switchScene("ReaderView/rAllBooks-view.fxml", cancelButton);*/
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmButtonClicked(ActionEvent event) {

    }

    @FXML
    void deleteFromCartButtonClicked(ActionEvent event) {
        Book selectedBook = cartTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            playButtonClickSound2();
            Cart.removeBookFromCart(selectedBook);
            cartTable.setItems(Cart.getBooksInCart());
            totalBooksLabel.setText(String.valueOf(Cart.getBooksInCart().size()));
        } else {
            alertSoundPlay();
            showAlert("No selection", "No Book Selected", "Please select a book to remove from the cart.", Alert.AlertType.WARNING);
        }
    }
}
