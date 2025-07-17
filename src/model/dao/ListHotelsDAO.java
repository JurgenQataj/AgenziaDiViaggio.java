package model.dao;

import model.Hotel;
import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListHotelsDAO implements BaseDAO<List<Hotel>> {

    public ListHotelsDAO() {
        // Costruttore vuoto
    }

    @Override
    public List<Hotel> execute() throws SQLException {
        final String procedure = "{CALL list_hotels()}";
        List<Hotel> hotels = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                // ***** QUESTA È LA RIGA CORRETTA *****
                // Usiamo il nuovo costruttore di Place senza l'ID.
                Place place = new Place(
                        rs.getString("localita"),
                        rs.getString("paese")
                );

                Hotel hotel = new Hotel(
                        rs.getInt("codice"),
                        rs.getString("nome"),
                        rs.getString("referente"),
                        rs.getInt("capienza"),
                        rs.getString("via"),
                        rs.getString("civico"),
                        rs.getString("cp"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("fax"),
                        place,
                        rs.getFloat("costo_notte_persona")
                );
                hotels.add(hotel);
            }
        }
        return hotels;
    }
}