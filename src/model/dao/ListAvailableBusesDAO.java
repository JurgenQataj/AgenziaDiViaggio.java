package model.dao;

import model.Autobus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Questo DAO recupera la lista degli autobus che sono disponibili
 * per un dato viaggio, escludendo quelli già assegnati a viaggi
 * con date sovrapposte.
 */
public class ListAvailableBusesDAO {
    private final int tripId;

    public ListAvailableBusesDAO(int tripId) {
        this.tripId = tripId;
    }

    /**
     * Esegue la stored procedure 'list_available_buses_for_trip'.
     * @return Una lista di oggetti Autobus disponibili.
     * @throws SQLException
     */
    public List<Autobus> execute() throws SQLException {
        List<Autobus> busList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL list_available_buses_for_trip(?)}")) {

            cs.setInt(1, this.tripId);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Autobus bus = new Autobus(
                            rs.getString("targa"),
                            rs.getInt("capienza"),
                            rs.getDouble("costo_giornaliero")
                    );
                    busList.add(bus);
                }
            }
        }
        return busList;
    }
}