package model.dao;

import exceptions.DatabaseException;
import model.Booking;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class BookDAO implements BaseDAO<Booking> {
    @Override
    public Booking execute(Object... params) throws DatabaseException {
        Booking booking = (Booking) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call book(?,?,?,?)}");
            stmt.setString(1, booking.getTripTitle());
            stmt.setString(2, booking.getUser());
            stmt.setInt(3, booking.getParticipants());
            stmt.registerOutParameter(4, Types.NUMERIC);
            stmt.executeUpdate();
            booking.setCode(stmt.getInt("codice"));
            return booking;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore durante la prenotazione: %s", e.getMessage()), e);
        }
    }
}