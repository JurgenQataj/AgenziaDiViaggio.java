package model.dao;

import exceptions.DatabaseException;
import model.Trip;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertTripDAO implements BaseDAO<Trip> {
    @Override
    public Trip execute(Object... params) throws DatabaseException {
        Trip trip = (Trip) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call create_trip(?,?,?,?)}");
            stmt.setString(1, trip.getTitle());
            stmt.setDate(2, trip.getStartDate());
            stmt.setDate(3, trip.getReturnDate());
            stmt.setDouble(4, trip.getPrice());
            stmt.executeUpdate();
            return trip;
        } catch (SQLException e) {
            throw  new DatabaseException(String.format("Errore nella creazione del viaggio: %s", e.getMessage()), e);
        }
    }
}
