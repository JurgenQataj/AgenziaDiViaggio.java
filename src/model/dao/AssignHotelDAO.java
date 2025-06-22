package model.dao;

import exceptions.DatabaseException;
import model.Hotel;
import model.Trip;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class AssignHotelDAO implements BaseDAO<Integer> {
    @Override
    public Integer execute(Object... params) throws DatabaseException {
        String trip = (String) params[0];
        Date startDate = (Date) params[1];
        int hotel = (int) params[2];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call assign_hotel(?,?,?)}");
            stmt.setString(1, trip);
            stmt.setDate(2, startDate);
            stmt.setInt(3, hotel);
            stmt.executeUpdate();
            return hotel;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'assegnamento dell'hotel: %s", e.getMessage()), e);
        }
    }
}
