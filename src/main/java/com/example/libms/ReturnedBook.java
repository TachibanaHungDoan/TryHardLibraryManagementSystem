package com.example.libms;

import java.util.Date;

public class ReturnedBook  {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Date returnedDate;
    private int lateFee;

    public ReturnedBook(int id, String title, String author, String publisher, String isbn, Date returnedDate, int lateFee) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.returnedDate = returnedDate;
        this.lateFee = lateFee;
    }

    // Các phương thức getter và setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public int getLateFee() {
        return lateFee;
    }

    public void setLateFee(int lateFee) {
        this.lateFee = lateFee;
    }
}
