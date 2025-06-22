package model.dao;

import exceptions.DatabaseException;
import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertPlaceDAO implements BaseDAO<Place> {
    @Override
    public Place execute(Object... params) throws DatabaseException {
        Place place = (Place) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call new_location(?,?)}");
            stmt.setString(1, place.getName());
            stmt.setString(2, place.getCountry());
            stmt.executeUpdate();
            return place;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'inserimento della località: %s", e.getMessage()), e);
        }
    }
}
