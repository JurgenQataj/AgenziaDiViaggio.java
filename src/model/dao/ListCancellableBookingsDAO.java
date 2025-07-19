package model.dao;

import model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListCancellableBookingsDAO implements BaseDAO {
    private final String userEmail;

    public ListCancellableBookingsDAO(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Esegue una query per trovare tutte le prenotazioni di un utente specifico
     * che possono ancora essere cancellate (a più di 20 giorni dalla partenza).
     * @return Una lista di oggetti Booking cancellabili.
     * @throws SQLException in caso di errori di accesso al database.
     */
    public List<Booking> execute() throws SQLException {
        List<Booking> cancellableBookings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // La query unisce prenotazione e viaggio per filtrare in base alla data di partenza
        String query = "SELECT p.codice, p.numero_persone, p.cliente_email, p.viaggio_id " +
                "FROM prenotazione p " +
                "JOIN viaggio v ON p.viaggio_id = v.id " +
                "WHERE p.cliente_email = ? AND DATEDIFF(v.data_partenza, CURDATE()) >= 20";

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, this.userEmail);

            rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("codice"),
                        rs.getInt("numero_persone"),
                        rs.getString("cliente_email"),
                        rs.getInt("viaggio_id")
                );
                cancellableBookings.add(booking);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }

        return cancellableBookings;
    }
}