package com.example.libms;

import java.util.Date;

public class BorrowedBook {
    private int id;
    private String title;
    private String isbn;
    private int readerID;
    private String readerName;
    private Date borrowedDate;
    private Date returnDate;
    private int borrowedDay;
    private double lateFee;

    public BorrowedBook(int id, String isbn, String title, int readerID, String readerName,
                        Date borrowedDate, Date returnDate, int borrowedDay, double lateFee) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.readerID = readerID;
        this.readerName = readerName;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
        this.borrowedDay = borrowedDay;
        this.lateFee = lateFee;
    }

    // Getters and setters
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getReaderID() {
        return readerID;
    }

    public void setReaderID(int readerID) {
        this.readerID = readerID;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getBorrowedDay() {
        return borrowedDay;
    }

    public void setBorrowedDay(int borrowedDay) {
        this.borrowedDay = borrowedDay;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }
}
