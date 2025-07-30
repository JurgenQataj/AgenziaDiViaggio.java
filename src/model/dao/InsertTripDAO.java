package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertTripDAO implements BaseDAO<Integer> { // <-- CORRETTO: Si usa 'implements' perché BaseDAO è un'interfaccia.

    private final int itinerarioId;
    private final Date dataPartenza;

    public InsertTripDAO(int itinerarioId, Date dataPartenza) {
        this.itinerarioId = itinerarioId;
        this.dataPartenza = dataPartenza;
    }

    @Override
    public Integer execute() throws SQLException {
        int viaggioId = -1;
        final String procedure = "{CALL create_trip(?, ?)}";

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