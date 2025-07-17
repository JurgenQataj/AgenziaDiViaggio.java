package model.dao;

import model.Itinerario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListItinerariDAO implements BaseDAO<List<Itinerario>> {

    @Override
    public List<Itinerario> execute() throws SQLException {
        // La procedura "list_itinerari" non esiste nel tuo dump,
        // quindi usiamo una semplice query SELECT.
        // Se preferisci una procedura, possiamo aggiungerla.
        final String query = "SELECT id, titolo, costo_persona FROM itinerario ORDER BY id;";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);
            List<Itinerario> itinerari = new ArrayList<>();

            while (rs.next()) {
                itinerari.add(new Itinerario(
                        rs.getInt("id"),
                        rs.getString("titolo"),
                        rs.getFloat("costo_persona")
                ));
            }
            return itinerari;
        }
    }
}