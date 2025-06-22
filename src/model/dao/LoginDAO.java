package model.dao;

import exceptions.DatabaseException;
import model.LoginCredentials;
import model.Role;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class LoginDAO implements BaseDAO<LoginCredentials> {

    @Override
    public LoginCredentials execute(Object... params) throws DatabaseException {
        String username = (String) params[0];
        String password = (String) params[1];
        int role;

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement loginStatement = connection.prepareCall("{call login(?,?,?)}");
            loginStatement.setString(1, username);
            loginStatement.setString(2, password);
            loginStatement.registerOutParameter(3, Types.NUMERIC);
            loginStatement.executeQuery();
            role = loginStatement.getInt(3);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore di accesso: %s", e.getMessage()), e);
        }

        return new LoginCredentials(username, password, Role.fromInt(role));
    }
}