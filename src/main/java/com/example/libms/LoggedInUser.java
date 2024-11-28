package com.example.libms;

public class LoggedInUser {
    private static String username;
    private static int readerID;
    private static String readerName;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoggedInUser.username = username;
    }

    public static int getReaderID() {
        return readerID;
    }

    public static void setReaderID(int readerID) {
        LoggedInUser.readerID = readerID;
    }

    public static String getReaderName() {
        return readerName;
    }

    public static void setReaderName(String readerName) {
        LoggedInUser.readerName = readerName;
    }
}
