package model.dao;

import exceptions.DatabaseException;
import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class GetPlaceDAO implements BaseDAO<Place> {
    @Override
    public Place execute(Object... params) throws DatabaseException {
        String placeName = (String) params[0];
        String country;

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call get_place(?,?)}");
            stmt.setString(1, placeName);
            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.executeQuery();
            country = stmt.getString(2);
            return new Place(placeName, country);
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'esecuzione dell'interrogazione: %s", e.getMessage()), e);
        }
    }
}
