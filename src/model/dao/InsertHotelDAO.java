package model.dao;

import model.Hotel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertHotelDAO implements BaseDAO<Void> {
    private final Hotel hotel;

    public InsertHotelDAO(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public Void execute() throws SQLException {

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL new_hotel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

            cs.setString(1, hotel.getNome());
            cs.setString(2, hotel.getReferente());
            cs.setInt(3, hotel.getCapienza());
            cs.setString(4, hotel.getVia());
            cs.setString(5, hotel.getCivico());
            cs.setString(6, hotel.getCp());
            cs.setString(7, hotel.getEmail());
            cs.setString(8, hotel.getTelefono());
            cs.setString(9, hotel.getFax());
            cs.setString(10, hotel.getLocalita().getNome());
            cs.setDouble(11, hotel.getCostoNottePersona());

            cs.execute();

        }
        return null;
    }
}