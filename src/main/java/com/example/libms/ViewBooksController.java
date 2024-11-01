package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewBooksController {

    @FXML
    private Label ISBNLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label bookTitleLabel;

    @FXML
    private Label editionLabel;

    @FXML
    private Label publishedDateLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label remainingLabel;

    @FXML
    private Label stateLabel;

    public void setBookData(Book book) {
        bookTitleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        publisherLabel.setText(book.getPublisher());
        ISBNLabel.setText(book.getIsbn());
        publishedDateLabel.setText(book.getPublishedDate().toString());
        editionLabel.setText(String.valueOf(book.getEdition()));
        quantityLabel.setText(String.valueOf(book.getQuantity()));
        stateLabel.setText(book.getState().toString());
        remainingLabel.setText(String.valueOf(book.getRemaining()));
    }
}
