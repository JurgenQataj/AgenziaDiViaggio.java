package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;

// Mantengo il nome originale della classe come richiesto
public class InsertTripDAO implements BaseDAO<Integer> {
    private final int itinerarioId;
    private final Date startDate;
    private final Date endDate;

    public InsertTripDAO(int itinerarioId, Date startDate, Date endDate) {
        this.itinerarioId = itinerarioId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Integer execute() throws SQLException {
        // La procedura da chiamare è "create_trip" dal tuo nuovo dump SQL
        final String procedure = "{CALL create_trip(?, ?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.itinerarioId);
            cs.setDate(2, this.startDate);
            cs.setDate(3, this.endDate);
            cs.registerOutParameter(4, Types.INTEGER); // Parametro di output per il nuovo ID del viaggio

            cs.execute();
            return cs.getInt(4); // Ritorna l'ID del viaggio appena creato
        }
    }
}