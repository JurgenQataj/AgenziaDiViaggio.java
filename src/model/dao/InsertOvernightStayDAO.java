package model.dao;

import model.OvernightStay;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertOvernightStayDAO implements BaseDAO<Void> {
    private final int tripId;
    private final OvernightStay overnightStay;

    public InsertOvernightStayDAO(int tripId, OvernightStay overnightStay) {
        this.tripId = tripId;
        this.overnightStay = overnightStay;
    }

    @Override
    public Void execute() throws SQLException {
        final String procedure = "{CALL insert_overnight_stay(?, ?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.tripId);
            cs.setDate(2, new java.sql.Date(this.overnightStay.getStartDate().getTime()));
            cs.setDate(3, new java.sql.Date(this.overnightStay.getEndDate().getTime()));
            cs.setString(4, this.overnightStay.getPlace().getName());

            cs.execute();
        }
        return null;
    }
}
