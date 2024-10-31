package com.example.libms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "DELETE FROM books WHERE bookID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, book.getId()); // Assuming 'id' is the primary key for identifying the book record

            int rowsDeleted = st.executeUpdate();
            return rowsDeleted; // Returns the number of rows deleted, 0 if no rows were affected
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Indicate failure
        }
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books"; // Replace 'books' with your actual table name

        try (Connection conn = DatabaseConnection.getConnection(); // Ensure you have a Database class to handle connections
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Create Book object based on ResultSet
                int id = rs.getInt("bookID"); // Replace 'id' with your actual column name
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String isbn = rs.getString("isbn");
                Date publishedDate = rs.getDate("publishedDate"); // Adjust according to your DB column type
                int edition = rs.getInt("edition");
                int quantity = rs.getInt("quantity");
                String stateString = rs.getString("state"); // Assuming you store the state as a string in DB
                Book.BookState state = Book.BookState.valueOf(stateString); // Convert string to enum
                int remaining = rs.getInt("remaining");

                // Create Book instance
                Book book = new Book(id, title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining);
                books.add(book); // Add to list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books; // Return the list of books
    }


    @Override
    public Book selectById(Book book) {
        return null;
    }

    @Override
    public ArrayList<Book> selectByCondition(String condition) {
        return null;
    }
    public int deleteBookById(int bookID) {
        Connection conn = null;
        PreparedStatement deleteStmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String deleteQuery = "DELETE FROM books WHERE bookID = ?";
            deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, bookID);
            return deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DatabaseConnection.closeStatement(deleteStmt);
            DatabaseConnection.closeConnection(conn);
        }
    }

}