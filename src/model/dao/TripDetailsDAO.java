package model.dao;

import exceptions.DatabaseException;
import model.OvernightStay;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDetailsDAO implements BaseDAO<List<OvernightStay>> {
    private final int tripId;

    public TripDetailsDAO(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public List<OvernightStay> execute() throws DatabaseException {
        List<OvernightStay> details = new ArrayList<>();
        final String procedure = "{CALL trip_details(?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.tripId);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
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
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il recupero dei dettagli del viaggio.", e);
        }
        return details;
    }
}