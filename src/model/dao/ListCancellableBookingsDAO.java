package model.dao;

import model.BookingDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per recuperare l'elenco delle prenotazioni annullabili per un dato cliente.
 * Utilizza la stored procedure list_cancellable_bookings.
 */
public class ListCancellableBookingsDAO implements BaseDAO<List<BookingDetails>> {

    private final String clientEmail;

    public ListCancellableBookingsDAO(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @Override
    public List<BookingDetails> execute() {
        List<BookingDetails> bookings = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL list_cancellable_bookings(?)}");
            cs.setString(1, this.clientEmail);
            rs = cs.executeQuery();

            while (rs.next()) {
                int codice = rs.getInt("codice");
                String titoloViaggio = rs.getString("titolo_viaggio");
                Date dataPartenza = rs.getDate("data_partenza");
                int numeroPersone = rs.getInt("numero_persone");

                // ↓↓↓↓↓↓↓↓↓↓↓↓ ECCO LA CORREZIONE FINALE ↓↓↓↓↓↓↓↓↓↓↓↓
                // I parametri ora sono nell'ordine corretto: (int, int, String, Date)
                BookingDetails booking = new BookingDetails(codice, numeroPersone, titoloViaggio, dataPartenza);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(cs);
            close(conn);
        }

        return bookings;
    }

    // Metodi di utilità per la chiusura sicura delle risorse
    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) { /* Ignora */ }
        }
    }

    private static void close(CallableStatement cs) {
        if (cs != null) {
            try {
                cs.close();
            } catch (SQLException e) { /* Ignora */ }
        }
    }

    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) { /* Ignora */ }
        }
    }
}