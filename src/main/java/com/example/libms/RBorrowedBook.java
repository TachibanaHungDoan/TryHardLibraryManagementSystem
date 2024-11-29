package com.example.libms;

import java.util.Date;

public class RBorrowedBook extends BorrowedBook {
    private String author;
    private String publisher;
    private Date publishedDate;

    @Override
    public java.sql.Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(java.sql.Date returnDate) {
        this.returnDate = returnDate;
    }

    private java.sql.Date returnDate;


    public RBorrowedBook(int id, String title, String isbn, String author, String publisher, Date publishedDate, java.sql.Date borrowedDate) {
        super(id, title, isbn, borrowedDate);
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
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

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
