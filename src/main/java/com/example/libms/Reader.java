package com.example.libms;

public class Reader {

    public enum ReaderGender {
        male,
        female
    }

    private int readerID;
    private String readerName;
    private ReaderGender gender;
    private int phoneNumber;
    private String email;

    public Reader(int readerID, String readerName, ReaderGender gender, int phoneNumber, String email) {
        this.readerID = readerID;
        this.readerName = readerName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
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

    public ReaderGender getGender() {
        return gender;
    }

    public void setGender(ReaderGender gender) {
        this.gender = gender;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
