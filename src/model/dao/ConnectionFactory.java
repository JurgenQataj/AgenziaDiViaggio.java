package model.dao;

import model.Role;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties props = new Properties();
    private static Role currentRole = Role.CLIENTE; // Il ruolo di default all'avvio

    // Blocco statico per caricare il file .properties una sola volta
    static {
        try {
            props.load(new FileInputStream("resources/db.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Errore critico: impossibile caricare il file db.properties", e);
        }
    }

    /**
     * Imposta il ruolo corrente che la factory userà per le prossime connessioni.
     * Viene chiamato dai controller dopo un login успешный.
     * @param role Il ruolo da impostare (CLIENTE o SEGRETERIA).
     */
    public static void changeRole(Role role) {
        if (role != null) {
            currentRole = role;
        }
    }

    /**
     * Crea e restituisce una NUOVA connessione al database usando le credenziali
     * appropriate per il ruolo attualmente attivo.
     * @return Una nuova connessione al database.
     * @throws SQLException Se la connessione fallisce.
     */
    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db_url");
        String user;
        String password;

        // Sceglie le credenziali corrette in base al ruolo
        if (currentRole == Role.SEGRETERIA) {
            user = props.getProperty("db_user_segreteria");
            password = props.getProperty("db_password_segreteria");
        } else { // Default al ruolo CLIENTE
            user = props.getProperty("db_user_cliente");
            password = props.getProperty("db_password_cliente");
        }

        // Controlla che le proprietà siano state caricate correttamente
        if (url == null || user == null || password == null) {
            throw new SQLException("Uno o più parametri di connessione (url, user, password) sono nulli. Controlla il file db.properties.");
        }

        // Restituisce una connessione nuova ogni volta
        return DriverManager.getConnection(url, user, password);
    }
}