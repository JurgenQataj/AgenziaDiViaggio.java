package model.dao;

import model.TripReport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO implements BaseDAO<TripReport> {
    private final int tripId;

    public ReportDAO(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public TripReport execute() throws SQLException {
        final String procedure = "{CALL report(?)}";
        TripReport report = null;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.tripId);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                report = new TripReport(
                        rs.getString("titolo_viaggio"),
                        rs.getInt("partecipanti"),
                        rs.getFloat("guadagno_o_perdita")
                );
            }
        }
        return report;
    }
}