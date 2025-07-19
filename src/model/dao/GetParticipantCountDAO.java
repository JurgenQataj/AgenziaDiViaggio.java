// File: src/model/dao/GetParticipantCountDAO.java
package model.dao;

import java.sql.*;

public class GetParticipantCountDAO {
    private final int tripId;

    public GetParticipantCountDAO(int tripId) {
        this.tripId = tripId;
    }

    public int execute() throws SQLException {
        int participantCount = 0;
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL get_participant_count_for_trip(?)}")) {

            cs.setInt(1, this.tripId);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    participantCount = rs.getInt("total_participants");
                }
            }
        }
        return participantCount;
    }
}