package com.example.libms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
    private static final ObservableList<Book> booksInCart = FXCollections.observableArrayList();

    public static ObservableList<Book> getBooksInCart() {
        return booksInCart;
    }

    public static void addBookToCart(Book book) {
        booksInCart.add(book);
    }

    public static void removeBookFromCart(Book book) {
        booksInCart.remove(book);
    }

    public static void clearCart() {
        booksInCart.clear();
    }
}
