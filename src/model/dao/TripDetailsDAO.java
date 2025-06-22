package model.dao;

import exceptions.DatabaseException;
import model.Hotel;
import model.OvernightStay;
import model.Place;
import model.Trip;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripDetailsDAO implements BaseDAO<List<OvernightStay>> {
    @Override
    public List<OvernightStay> execute(Object... params) throws DatabaseException {
        String trip = (String) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call trip_details(?)}");
            stmt.setString(1, trip);

            ResultSet rs = stmt.executeQuery();
            List<OvernightStay> tripDetails = new ArrayList<>();
            OvernightStay overnightStay;
            while (rs.next()) {
                overnightStay = new OvernightStay(
                        rs.getDate("data_inizio").toLocalDate(),
                        rs.getDate("data_fine").toLocalDate()
                );
                overnightStay.setPlace(
                        new Place(
                                rs.getString("localita"),
                                rs.getString("paese")
                        )
                );
                overnightStay.setHotel(new Hotel(rs.getString("albergo")));
                tripDetails.add(overnightStay);
            }
            return tripDetails;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'esecuzione dell'interrogazione: %s", e.getMessage()), e);
        }
    }
}
