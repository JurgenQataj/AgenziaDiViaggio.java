package model.dao;

import model.Role;
import model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistrationDAO {
    private final User user;

    // Il costruttore ora accetta un singolo oggetto User
    public RegistrationDAO(User user) {
        this.user = user;
    }

    public void execute() throws SQLException {
        String sql = "{CALL registration(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, user.getEmail());
            cs.setString(2, user.getNome());
            cs.setString(3, user.getCognome());
            cs.setString(4, user.getPassword());
            cs.setString(5, user.getTelefono());
            cs.setString(6, Role.CLIENTE.name()); // I nuovi utenti sono sempre CLIENTE

            cs.executeUpdate();
        }
    }
}