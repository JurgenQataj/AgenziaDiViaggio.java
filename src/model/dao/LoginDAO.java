package model.dao;

import model.LoginCredentials;
import model.Role;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class LoginDAO implements BaseDAO {
    private final LoginCredentials credentials;

    public LoginDAO(LoginCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Role execute() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;
        Role role = null; // Inizializza a null per gestire il login fallito

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL login(?, ?, ?)}");

            // Ora questo codice funzionerà perché LoginCredentials ha i metodi getter
            cs.setString(1, credentials.getEmail());
            cs.setString(2, credentials.getPassword());
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();

            int roleCode = cs.getInt(3); // Legge il parametro di output

            // Converte il codice numerico nel tipo Enum corretto
            if (roleCode == 0) {
                role = Role.CLIENTE;
            } else if (roleCode == 1) {
                role = Role.SEGRETERIA;
            }
            // Se roleCode è -1 (o altro), 'role' rimane null

        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return role;
    }
}