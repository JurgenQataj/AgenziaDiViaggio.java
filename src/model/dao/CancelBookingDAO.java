package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CancelBookingDAO implements BaseDAO<Void> {

    private final int bookingCode;

    public CancelBookingDAO(int bookingCode) {
        this.bookingCode = bookingCode;
    }

    @Override
    public Void execute() throws SQLException {
        final String procedure = "{CALL cancel_booking(?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            // Usa il codice della prenotazione salvato
            cs.setInt(1, this.bookingCode);
            cs.execute();
        }
        return null;
    }
}