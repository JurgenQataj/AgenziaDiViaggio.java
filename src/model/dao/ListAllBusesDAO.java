package model.dao;

import model.Autobus; // Assicurati che il percorso del modello sia corretto
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per recuperare l'elenco di tutti gli autobus dal database
 * utilizzando la stored procedure list_all_buses.
 */
public class ListAllBusesDAO implements BaseDAO<List<Autobus>> {

    @Override
    public List<Autobus> execute() {
        List<Autobus> allBuses = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            // Ottiene una connessione dal ConnectionFactory
            conn = ConnectionFactory.getConnection();

            // Prepara la chiamata alla stored procedure
            cs = conn.prepareCall("{CALL list_all_buses()}");

            // Esegue la query
            rs = cs.executeQuery();

            // Scorre i risultati e li mappa in oggetti Autobus
            while (rs.next()) {
                String targa = rs.getString("targa");
                int capienza = rs.getInt("capienza");
                float costoGiornaliero = rs.getFloat("costo_giornaliero");

                Autobus bus = new Autobus(targa, capienza, costoGiornaliero);
                allBuses.add(bus);
            }

        } catch (SQLException e) {
            // In un'applicazione reale, qui andrebbe un sistema di logging
            e.printStackTrace();
        } finally {
            // Chiude tutte le risorse per prevenire resource leak
            close(rs);
            close(cs);
            close(conn);
        }

        return allBuses;
    }

    /**
     * Metodo di utilità per chiudere un ResultSet in modo sicuro.
     * @param rs Il ResultSet da chiudere.
     */
    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // Ignora l'eccezione, non possiamo fare molto qui
            }
        }
    }

    /**
     * Metodo di utilità per chiudere un CallableStatement in modo sicuro.
     * @param cs Lo statement da chiudere.
     */
    private static void close(CallableStatement cs) {
        if (cs != null) {
            try {
                cs.close();
            } catch (SQLException e) {
                // Ignora l'eccezione
            }
        }
    }

    /**
     * Metodo di utilità per chiudere una Connection in modo sicuro.
     * @param conn La connessione da chiudere.
     */
    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // Ignora l'eccezione
            }
        }
    }
}