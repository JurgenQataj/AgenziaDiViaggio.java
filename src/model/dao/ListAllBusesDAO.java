// File: src/model/dao/ListAllBusesDAO.java
package model.dao;

import model.Autobus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListAllBusesDAO {

    public List<Autobus> execute() throws SQLException {
        List<Autobus> busList = new ArrayList<>();
        String sql = "SELECT targa, capienza, costo_giornaliero FROM autobus ORDER BY capienza DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Autobus bus = new Autobus(
                        rs.getString("targa"),
                        rs.getInt("capienza"),
                        rs.getDouble("costo_giornaliero")
                );
                busList.add(bus);
            }
        }
        return busList;
    }
}