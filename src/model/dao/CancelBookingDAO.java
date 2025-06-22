package model.dao;

import exceptions.DatabaseException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CancelBookingDAO implements BaseDAO<Integer> {
    @Override
    public Integer execute(Object... params) throws DatabaseException {
        int code = (int) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call cancel_booking(?)}");
            stmt.setInt(1, code);
            stmt.executeUpdate();
            return code;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore durante la cancellazione della prenotazione: %s", e.getMessage()), e);
        }
    }
}
