package model.dao;

import model.Itinerario;
import model.Trip;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListTripsDAO implements
        BaseDAO<List<Trip>> {
    private final Date startDate;

    public ListTripsDAO(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public List<Trip> execute() throws SQLException {
        final String procedure = "{CALL list_trips(?)}";
        List<Trip> trips = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setDate(1, this.startDate);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                // 1. Crea l'oggetto Itinerario con i dati recuperati
                Itinerario itinerario = new Itinerario(
                        0, // L'id dell'itinerario non è nel result set della proc, ma non è critico per la visualizzazione
                        rs.getString("titolo"),
                        rs.getFloat("costo_persona")
                );

                // 2. Crea l'oggetto Trip usando l'itinerario
                Trip trip = new Trip(
                        rs.getInt("id"),
                        rs.getDate("data_partenza"),
                        rs.getDate("data_rientro"),
                        itinerario
                );
                trips.add(trip);
            }
        }
        return trips;
    }
}