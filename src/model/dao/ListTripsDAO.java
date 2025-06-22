package model.dao;

import exceptions.DatabaseException;
import model.Trip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListTripsDAO implements BaseDAO<List<Trip>> {
    @Override
    public List<Trip> execute(Object... params) throws DatabaseException {
        Date date = (Date) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call list_trips(?)}");
            stmt.setDate(1, date);

            ResultSet rs = stmt.executeQuery();
            Trip trip;
            List<Trip> trips = new ArrayList<>();
            while (rs.next()) {
                trip = new Trip(
                        rs.getString("titolo"),
                        rs.getDate("data_partenza").toLocalDate(),
                        rs.getDate("data_rientro").toLocalDate(),
                        rs.getDouble("costo")
                );
                trips.add(trip);
            }
            return trips;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'esecuzione dell'interrogazione: %s", e.getMessage()), e);
        }
    }
}