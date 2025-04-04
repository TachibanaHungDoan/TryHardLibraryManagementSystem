package com.example.libms;

import java.sql.*;

public class DatabaseConnection {
    public static Connection getConnection() {
        final String DATABASE_NAME = "libriscopedb";
        Connection c = null;
        try {
            // Replace with your actual database name
            String databaseUser = "root";  // Default user for XAMPP is root
            String databasePassword = "";  // Default password is empty for XAMPP
            String url = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;  // Adjust if you are using a different port
            //Class.forName("com.mysql.cj.jdbc.Driver");  // Ensure the MySQL driver is loaded
            c = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database connection successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection failed: " + e.getMessage());
        }

        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}