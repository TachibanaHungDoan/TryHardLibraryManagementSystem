package com.example.libms;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DAOInterface <T> {
    public int insert(T t) throws SQLException;
    public int update(T t);
    public int delete(T t);
    public List<T> getAllBooks();
    public T selectById(T t);
    public ArrayList<T> selectByCondition(String condition);

}