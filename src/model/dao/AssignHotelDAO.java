package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class AssignHotelDAO implements BaseDAO<Void> {
    private final int overnightStayId;
    private final int hotelCode;

    public AssignHotelDAO(int overnightStayId, int hotelCode) {
        this.overnightStayId = overnightStayId;
        this.hotelCode = hotelCode;
    }

    @Override
    public Void execute() throws SQLException {
        final String procedure = "{CALL assign_hotel(?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setInt(1, this.overnightStayId);
            cs.setInt(2, this.hotelCode);
            cs.execute();
        }
        return null; // Il metodo non restituisce nulla
    }
}