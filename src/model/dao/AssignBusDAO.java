package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class AssignBusDAO implements BaseDAO<Void> {
    private final int tripId;
    private final String busPlate;

    public AssignBusDAO(int tripId, String busPlate) {
        this.tripId = tripId;
        this.busPlate = busPlate;
    }

    @Override
    public Void execute() throws SQLException {
        final String procedure = "{CALL assign_bus(?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.tripId);
            cs.setString(2, this.busPlate);
            cs.execute();
        }
        return null;
    }
}