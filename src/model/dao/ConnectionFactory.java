package model.dao;

import model.Role;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final Properties props = new Properties();
    private static Role currentRole = Role.CLIENTE; // Inizia sempre con il ruolo a permessi minimi

    // Blocco statico che viene eseguito una sola volta, quando la classe viene caricata
    static {
        // --- INIZIO CORREZIONE ---
        // Carica il file db.properties come una risorsa dal classpath.
        // Questo è il modo robusto e corretto di farlo.
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (input == null) {
                System.err.println("ERRORE: Impossibile trovare il file db.properties nel classpath.");
            } else {
                props.load(input);
            }

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento del file db.properties");
            e.printStackTrace();
        }
        // --- FINE CORREZIONE ---
    }

    /**
     * Metodo statico per cambiare il ruolo corrente.
     * Questo determina quali credenziali verranno usate per le connessioni successive.
     * @param role Il nuovo ruolo (CLIENTE o SEGRETERIA).
     */
    public static void changeRole(Role role) {
        currentRole = role;
    }

    /**
     * Restituisce una connessione al database usando le credenziali del ruolo corrente.
     */
    public static Connection getConnection() throws SQLException {
        String dbUrl = props.getProperty("db.url");
        if (dbUrl == null || dbUrl.isEmpty()) {
            // Questo controllo aggiuntivo ci aiuta a dare un errore più chiaro
            throw new SQLException("La URL del database non è stata trovata in db.properties.");
        }

        String user;
        String password;

        if (currentRole == Role.SEGRETERIA) {
            user = props.getProperty("db.user.segreteria");
            password = props.getProperty("db.password.segreteria");
        } else { // Di default, o se il ruolo è CLIENTE
            user = props.getProperty("db.user.cliente");
            password = props.getProperty("db.password.cliente");
        }

        return DriverManager.getConnection(dbUrl, user, password);
    }
}