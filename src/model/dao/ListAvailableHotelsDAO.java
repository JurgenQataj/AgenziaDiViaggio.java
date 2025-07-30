package model.dao;

import model.Hotel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListAvailableHotelsDAO {
    private final int tripId;
    private final String location;

    public ListAvailableHotelsDAO(int tripId, String location) {
        this.tripId = tripId;
        this.location = location;
    }

    public List<Hotel> execute() throws SQLException {
        List<Hotel> hotelList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL list_available_hotels_for_stay(?, ?)}")) {

            cs.setInt(1, this.tripId);
            cs.setString(2, this.location);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Hotel hotel = new Hotel(
                            rs.getInt("codice"),
                            rs.getString("nome"),
                            rs.getInt("capienza"),
                            rs.getDouble("costo_notte_persona")
                    );
                    hotelList.add(hotel);
                }
            }
        }
        return hotelList;
    }
}