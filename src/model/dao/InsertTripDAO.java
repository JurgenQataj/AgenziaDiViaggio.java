package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO per creare un nuovo viaggio a partire da un itinerario-modello e una data di partenza.
 * Implementa l'interfaccia BaseDAO e delega la logica alla stored procedure 'create_trip'.
 */
public class InsertTripDAO implements BaseDAO<Integer> { // <-- CORRETTO: Si usa 'implements' perché BaseDAO è un'interfaccia.

    private final int itinerarioId;
    private final Date dataPartenza;

    /**
     * Costruttore per il DAO.
     * @param itinerarioId L'ID dell'itinerario da cui creare il viaggio.
     * @param dataPartenza La data di inizio del viaggio.
     */
    public InsertTripDAO(int itinerarioId, Date dataPartenza) {
        this.itinerarioId = itinerarioId;
        this.dataPartenza = dataPartenza;
    }

    /**
     * Esegue la chiamata alla stored procedure 'create_trip', implementando
     * il metodo definito nell'interfaccia BaseDAO.
     *
     * @return L'ID del viaggio creato, o -1 in caso di fallimento.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Integer execute() throws SQLException {
        int viaggioId = -1;
        final String procedure = "{CALL create_trip(?, ?)}";

        // Gestisce la connessione internamente, come richiesto dal pattern.
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.itinerarioId);
            cs.setDate(2, this.dataPartenza);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    viaggioId = rs.getInt("viaggio_id");
                }
            }
        }
        return viaggioId;
    }
}