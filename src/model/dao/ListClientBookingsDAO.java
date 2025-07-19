package model.dao;

import model.BookingDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListClientBookingsDAO {
    private final String clientEmail;

    public ListClientBookingsDAO(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public List<BookingDetails> execute() throws SQLException {
        List<BookingDetails> bookings = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL list_bookings_for_client(?)}")) {

            cs.setString(1, this.clientEmail);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new BookingDetails(
                            rs.getInt("codice"),
                            rs.getInt("numero_persone"),
                            rs.getString("titolo_viaggio"),
                            rs.getDate("data_partenza")
                    ));
                }
            }
        }
        return bookings;
    }
}