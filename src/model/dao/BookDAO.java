package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class BookDAO implements BaseDAO<Integer> {
    private final int tripId;
    private final String clientEmail;
    private final int participants;

    public BookDAO(int tripId, String clientEmail, int participants) {
        this.tripId = tripId;
        this.clientEmail = clientEmail;
        this.participants = participants;
    }

    @Override
    public Integer execute() throws SQLException {
        final String procedure = "{CALL book(?, ?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.tripId);
            cs.setString(2, this.clientEmail);
            cs.setInt(3, this.participants);
            cs.registerOutParameter(4, Types.INTEGER); // L'ID della prenotazione

            cs.execute();
            return cs.getInt(4); // Ritorna il codice della prenotazione
        }
    }
}