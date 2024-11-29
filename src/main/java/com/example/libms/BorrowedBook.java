package com.example.libms;

import java.util.Date;

public class BorrowedBook extends Book{
    private int readerID;
    private String readerName;
    private Date borrowedDate;
    private java.sql.Date returnDate;
    private int borrowedDay;
    private double lateFee;

    public BorrowedBook(int id, String isbn, String title, int readerID, String readerName,
                        Date borrowedDate, java.sql.Date returnDate, int borrowedDay, double lateFee) {
        super(id, isbn, title);
        this.readerID = readerID;
        this.readerName = readerName;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
        this.borrowedDay = borrowedDay;
        this.lateFee = lateFee;
    }

    public BorrowedBook(int id, String title , String isbn, java.sql.Date borrowedDate) {
        super(id, isbn, title);
        this.borrowedDate = borrowedDate;
    }

    public BorrowedBook(int id , String title , String isbn, java.sql.Date returnDate, long lateFee) {
        super(id, isbn, title);
        this.returnDate = returnDate;
        this.lateFee = lateFee;
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

    public void setBorrowedDate(java.sql.Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(java.sql.Date returnDate) {
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
