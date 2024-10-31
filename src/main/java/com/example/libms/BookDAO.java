package com.example.libms;

import java.sql.*;
import java.util.ArrayList;

public class BookDAO implements DAOInterface <Book> {
    public static BookDAO getInstance() {
        return new BookDAO();
    }
    @Override
    public int insert(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, book.getTitle());
            st.setString(2, book.getAuthor());
            st.setString(3, book.getPublisher());
            st.setString(4, book.getIsbn());
            st.setDate(5, book.getPublishedDate() != null ? new java.sql.Date(book.getPublishedDate().getTime()) : null);
            st.setInt(6, book.getEdition());
            st.setInt(7, book.getQuantity());
            //st.setInt(8, book.getState().ordinal());
            st.setString(8, book.getState().name());
            st.setInt(9, book.getRemaining());

            int rowsInserted = st.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getInt(1));
                    }
                }
                return 1; // Indicate success
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Indicate failure
    }

    @Override
    public int update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, isbn = ?, publishedDate = ?, edition = ?, " +
                "quantity = ?, state = ?, remaining = ? WHERE bookID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, book.getTitle());
            st.setString(2, book.getAuthor());
            st.setString(3, book.getPublisher());
            st.setString(4, book.getIsbn());
            st.setDate(5, book.getPublishedDate() != null ? new java.sql.Date(book.getPublishedDate().getTime()) : null);
            st.setInt(6, book.getEdition());
            st.setInt(7, book.getQuantity());
            st.setString(8, book.getState().name());
            st.setInt(9, book.getRemaining());
            st.setInt(10, book.getId()); // Assuming 'id' is the primary key for identifying the book record

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated; // Returns the number of rows updated, 0 if no rows were affected
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Indicate failure
        }
    }

    @Override
    public int delete(Book book) {
        return 0;
    }

    @Override
    public ArrayList<Book> selectAll() {
        return null;
    }

    @Override
    public Book selectById(Book book) {
        return null;
    }

    @Override
    public ArrayList<Book> selectByCondition(String condition) {
        return null;
    }
}