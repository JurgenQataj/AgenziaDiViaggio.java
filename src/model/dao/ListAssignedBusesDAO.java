package model.dao;

import model.Autobus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListAssignedBusesDAO {
    private final int tripId;

    public ListAssignedBusesDAO(int tripId) {
        this.tripId = tripId;
    }

    public List<Autobus> execute() throws SQLException {
        List<Autobus> assignedBuses = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL list_assigned_buses_for_trip(?)}")) {

            cs.setInt(1, this.tripId);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Autobus bus = new Autobus(
                            rs.getString("targa"),
                            rs.getInt("capienza"),
                            rs.getDouble("costo_giornaliero")
                    );
                    assignedBuses.add(bus);
                }
            }
        }
        return assignedBuses;
    }
}