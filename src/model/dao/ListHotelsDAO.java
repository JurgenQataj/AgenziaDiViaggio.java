package model.dao;

import exceptions.DatabaseException;
import model.Hotel;
import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHotelsDAO implements BaseDAO<List<Hotel>> {
    @Override
    public List<Hotel> execute(Object... params) throws DatabaseException {

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call list_hotels()}");

            ResultSet rs = stmt.executeQuery();
            List<Hotel> hotels = new ArrayList<>();
            Place place;
            Hotel hotel;
            while (rs.next()) {
                place = new Place(rs.getString("localita"), rs.getString("paese"));
                hotel = new Hotel(
                        rs.getString("Albergo.nome"),
                        rs.getString("referente"),
                        rs.getInt("capienza"),
                        rs.getString("via"),
                        rs.getString("civico"),
                        rs.getString("cp"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("fax")
                );
                hotel.setCode(rs.getInt("codice"));
                hotel.setPlace(place);
                hotels.add(hotel);
            }
            return hotels;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'esecuzione dell'interrogazione: %s", e.getMessage()), e);
        }
    }
}
