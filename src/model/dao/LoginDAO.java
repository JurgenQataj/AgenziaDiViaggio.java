package model.dao;

import exceptions.DatabaseException;
import model.LoginCredentials;
import model.Role;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class LoginDAO {

    private final LoginCredentials credentials;

    public LoginDAO(LoginCredentials credentials) {
        this.credentials = credentials;
    }

    public Role execute() throws DatabaseException {

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL login(?, ?, ?)}")) {

            cs.setString(1, credentials.getEmail());
            cs.setString(2, credentials.getPassword());
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();

            int roleCode = cs.getInt(3);

            if (roleCode == 0) {
                return Role.CLIENTE;
            } else if (roleCode == 1) {
                return Role.SEGRETERIA;
            }
            return null; // Login fallito

        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il tentativo di login.", e);
        }
    }
}