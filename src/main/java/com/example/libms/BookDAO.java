package com.example.libms;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookDAO implements DAOInterface <Book> {
    public static BookDAO getInstance() {
        return new BookDAO();
    }
    @Override
    public int insert(Book book) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        Statement st = con.createStatement();
        String sql = "INSERT INTO books (title, author, publisher, isbn, publishedDate, edition, quantity, state, remaining) " +
                "VALUES ('" + book.getTitle() + "', '"
                + book.getAuthor() + "', '"
                + book.getPublisher() + "', '"
                + book.getIsbn() + "', '"
                + new java.sql.Date(book.getPublishedDate().getTime()) + "', "
                + book.getEdition() + ", "
                + book.getQuantity() + ", "
                + book.getState() + ", "
                + book.getRemaining() + ");";
        int ketqua = st.executeUpdate(sql);
        DatabaseConnection.closeConnection(con);
        return 0;
    }

    @Override
    public int update(Book book) {
        return 0;
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