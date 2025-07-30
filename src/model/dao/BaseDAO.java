package model.dao;
import java.sql.SQLException;
import exceptions.DatabaseException;

public interface BaseDAO<T> {
    T execute() throws SQLException;
}