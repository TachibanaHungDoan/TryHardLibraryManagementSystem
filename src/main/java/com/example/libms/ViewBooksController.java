package com.example.libms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewBooksController {
    @FXML
    private Label bookTitleLabel, authorLabel, publisherLabel;
    @FXML
    private Label publishedDateLabel, ISBNLabel, editionLabel;
    @FXML
    private Label quantityLabel, stateLabel, remainingLabel;

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
