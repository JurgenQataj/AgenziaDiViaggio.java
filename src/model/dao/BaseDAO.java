package model.dao;

import exceptions.DatabaseException;

import java.sql.SQLException;

public interface BaseDAO<T> {

    T execute(Object... params) throws DatabaseException;
}
