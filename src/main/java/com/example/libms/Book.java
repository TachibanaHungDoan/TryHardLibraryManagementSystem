package com.example.libms;

import org.mockito.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

public class Book {

    public enum BookState {
        available,
        unavailable
    }

    protected int id;
    protected String title;
    private String author;
    private String publisher;
    protected String isbn;
    private Date publishedDate;
    private int edition;
    private int quantity;
    private BookState state;
    private int remaining;

    public Book(int id,String title, String author, String publisher, String isbn, Date publishedDate, int edition, int quantity, BookState state,int remaining) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
        this.edition = edition;
        this.quantity = quantity;
        this.state = state;
        this.remaining = remaining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}