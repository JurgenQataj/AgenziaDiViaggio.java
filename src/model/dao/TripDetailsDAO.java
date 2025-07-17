package model.dao;

import model.Hotel;
import model.OvernightStay;
import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripDetailsDAO implements BaseDAO<List<OvernightStay>> {
    private final int tripId;

    public TripDetailsDAO(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public List<OvernightStay> execute() throws SQLException {
        final String procedure = "{CALL trip_details(?)}";
        List<OvernightStay> details = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.tripId);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                // ***** THIS IS THE CORRECTED LINE *****
                // We now use the new constructor for Place, without the ID.
                Place place = new Place(rs.getString("localita"), rs.getString("paese"));

                Hotel hotel = null;
                String hotelName = rs.getString("albergo");
                if (hotelName != null && !rs.wasNull()) {
                    // Create a partial Hotel object, just with the name
                    hotel = new Hotel(0, hotelName, "", 0, "", "", "", "", "", "", null, 0);
                }

                OvernightStay overnightStay = new OvernightStay(
                        rs.getInt("id"),
                        rs.getDate("data_inizio"),
                        rs.getDate("data_fine"),
                        place,
                        hotel
                );
                details.add(overnightStay);
            }
        }
        return details;
    }
}