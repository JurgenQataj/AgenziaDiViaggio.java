package model.dao;

import exceptions.DatabaseException;
import model.Hotel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertHotelDAO implements BaseDAO<Hotel> {
    @Override
    public Hotel execute(Object... params) throws DatabaseException {
        Hotel hotel = (Hotel) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call new_hotel(?,?,?,?,?,?,?,?,?,?)}");
            stmt.setString(1, hotel.getName());
            stmt.setString(2, hotel.getReferee());
            stmt.setInt(3, hotel.getCapacity());
            stmt.setString(4, hotel.getStreet());
            stmt.setString(5, hotel.getCivic());
            stmt.setString(6, hotel.getCp());
            stmt.setString(7, hotel.getEmail());
            stmt.setString(8, hotel.getTelephone());
            stmt.setString(9, hotel.getFax());
            stmt.setString(10, hotel.getPlace().getName());
            stmt.executeUpdate();
            return hotel;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'inserimento dell'albergo: %s", e.getMessage()), e);
        }
    }
}