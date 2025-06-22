package model.dao;

import exceptions.DatabaseException;
import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPlacesDAO implements BaseDAO<List<Place>>  {
    @Override
    public List<Place> execute(Object... params) throws DatabaseException {

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call list_places()}");

            ResultSet rs = stmt.executeQuery();
            List<Place> places = new ArrayList<>();
            Place place;
            while (rs.next()) {
                place = new Place(rs.getString("nome"), rs.getString("paese"));
                places.add(place);
            }
            return places;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'esecuzione dell'interrogazione: %s", e.getMessage()), e);
        }
    }
}