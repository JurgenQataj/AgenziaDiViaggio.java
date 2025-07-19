package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

// La classe ora segue il pattern del tuo progetto
public class RegistrationDAO implements BaseDAO {
    private final String email;
    private final String nome;
    private final String cognome;
    private final String password;
    private final String telefono;

    // I dati vengono passati nel costruttore
    public RegistrationDAO(String email, String nome, String cognome, String password, String telefono) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.telefono = telefono;
    }

    /**
     * Esegue la stored procedure 'registration' sul database.
     * Questo metodo ora implementa correttamente il contratto dell'interfaccia BaseDAO.
     */
    @Override
    public Void execute() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL registration(?, ?, ?, ?, ?, ?)}");

            cs.setString(1, this.email);
            cs.setString(2, this.nome);
            cs.setString(3, this.cognome);
            cs.setString(4, this.password);
            cs.setString(5, this.telefono);
            cs.setNull(6, Types.VARCHAR); // Ruolo gestito dalla procedura

            cs.execute();

        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return null; // Il metodo deve restituire qualcosa (anche se Void)
    }
}