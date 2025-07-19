package model.dao;

import model.Hotel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertHotelDAO implements BaseDAO {
    private final Hotel hotel;

    public InsertHotelDAO(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public Void execute() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL new_hotel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setString(1, hotel.getNome());
            cs.setString(2, hotel.getReferente());
            cs.setInt(3, hotel.getCapienza());
            cs.setString(4, hotel.getVia());
            cs.setString(5, hotel.getCivico());
            cs.setString(6, hotel.getCp());
            cs.setString(7, hotel.getEmail());
            cs.setString(8, hotel.getTelefono());
            cs.setString(9, hotel.getFax());
            cs.setString(10, hotel.getLocalitaNome()); // Usa il getter corretto
            cs.setDouble(11, hotel.getCostoNottePersona());

            cs.execute();

        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return null;
    }
}