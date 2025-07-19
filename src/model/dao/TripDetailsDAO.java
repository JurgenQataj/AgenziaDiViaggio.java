package model.dao;

import model.OvernightStay;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDetailsDAO implements BaseDAO {
    private final int tripId;

    public TripDetailsDAO(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public List<OvernightStay> execute() throws SQLException {
        List<OvernightStay> details = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL trip_details(?)}");
            cs.setInt(1, this.tripId);

            rs = cs.executeQuery();

            while (rs.next()) {
                // Legge l'hotelCode e gestisce correttamente il caso in cui sia NULL
                int hotelCodeInt = rs.getInt("albergo_codice");
                Integer hotelCode = rs.wasNull() ? null : hotelCodeInt;

                OvernightStay stay = new OvernightStay(
                        rs.getInt("id"),
                        rs.getDate("data_inizio"),
                        rs.getDate("data_fine"),
                        rs.getString("localita"),
                        hotelCode
                );
                details.add(stay);
            }
        } finally {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return details;
    }
}