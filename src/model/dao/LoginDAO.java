package model.dao;

import model.LoginCredentials;
import model.Role;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class LoginDAO implements BaseDAO<Role> {

    private final LoginCredentials credentials;

    /**
     * Questo è il nuovo costruttore che risolve l'errore.
     * Accetta le credenziali di login.
     * @param credentials L'oggetto contenente username e password.
     */
    public LoginDAO(LoginCredentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Questo metodo execute() ora corrisponde a quello richiesto
     * dall'interfaccia BaseDAO.
     */
    @Override
    public Role execute() throws SQLException {
        // Chiama la procedura "login" dal tuo dump SQL
        final String procedure = "{CALL login(?, ?, ?)}";
        int roleId = -1;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setString(1, this.credentials.getUsername());
            cs.setString(2, this.credentials.getPassword());
            cs.registerOutParameter(3, Types.INTEGER); // Il ruolo come parametro di output

            cs.execute();
            roleId = cs.getInt(3);
        }

        // Converte l'ID del ruolo nell'enum Role
        return switch (roleId) {
            case 0 -> Role.CLIENTE;
            case 1 -> Role.SEGRETERIA;
            default -> null; // Ritorna null se il login fallisce
        };
    }
}