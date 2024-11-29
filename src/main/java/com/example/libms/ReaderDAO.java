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
        String updateQuery = "UPDATE readers SET readerName = ?, gender = ?, phoneNumber = ?, email = ? WHERE readerID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(updateQuery)) {
            st.setString(1, reader.getReaderName());
            st.setString(2, reader.getGender().name());
            st.setInt(3, reader.getPhoneNumber());
            st.setString(4, reader.getEmail());
            st.setInt(5, reader.getReaderID());
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(Reader reader) {
        String deleteQuery = "DELETE FROM readers WHERE readerID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, reader.getReaderID());
            return deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Reader> getAllItems() {
        List<Reader> readers = new ArrayList<>();
        String getQuery = "SELECT * FROM readers";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(getQuery);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                int readerID = rs.getInt("readerID");
                String readerName = rs.getString("readerName");
                String genderString = rs.getString("Gender");
                Reader.ReaderGender gender = Reader.ReaderGender.valueOf(genderString.toLowerCase());
                int phoneNumber = rs.getInt("PhoneNumber");
                String email = rs.getString("Email");

                Reader reader = new Reader(readerID, readerName, gender, phoneNumber, email);
                readers.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;
    }

    @Override
    public Reader selectById(Reader reader) {
        return null;
    }

    @Override
    public ArrayList<Reader> selectByCondition(String condition) {
        return null;
    }

    @Override
    public int getTotalItems() {
        String getTotalQuery = "SELECT COUNT(*) FROM readers";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement st = con.prepareStatement(getTotalQuery);
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
}
