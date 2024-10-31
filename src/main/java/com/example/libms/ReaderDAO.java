package com.example.libms;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO implements DAOInterface <Reader>{

    public static ReaderDAO getInstance(){
        return new ReaderDAO();
    }

    @Override
    public int insert(Reader reader) throws SQLException {
        String sql = "INSERT INTO readers (readerName, gender, phoneNumber, email)" +
                "VALUES (?, ?, ?, ?),";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, reader.getReaderName());
            st.setString(2, reader.getGender().name());
            st.setInt(3, reader.getPhoneNumber());
            st.setString(4, reader.getEmail());

            int rowsInserted = st.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reader.setReaderID(generatedKeys.getInt(1));
                    }
                }
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Reader reader) {
        return 0;
    }

    @Override
    public int delete(Reader reader) {
        return 0;
    }

    @Override
    public List<Reader> getAllBooks() {
        return null;
    }

    @Override
    public Reader selectById(Reader reader) {
        return null;
    }

    @Override
    public ArrayList<Reader> selectByCondition(String condition) {
        return null;
    }
}
