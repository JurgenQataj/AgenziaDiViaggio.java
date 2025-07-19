// ↓↓↓↓↓↓↓↓↓↓↓↓ ECCO LA RIGA MANCANTE E FONDAMENTALE ↓↓↓↓↓↓↓↓↓↓↓↓
package model.dao;
// ↑↑↑↑↑↑↑↑↑↑↑↑ FINE DELLA CORREZIONE ↑↑↑↑↑↑↑↑↑↑↑↑

import model.Itinerario;
// Non serve importare BaseDAO o ConnectionFactory se sono nello stesso pacchetto

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per recuperare l'elenco di tutti gli itinerari dal database
 * utilizzando la stored procedure list_itineraries.
 */
public class ListItinerariDAO implements BaseDAO<List<Itinerario>> {

    @Override
    public List<Itinerario> execute() {
        List<Itinerario> itineraries = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL list_itineraries()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titolo = rs.getString("titolo");
                float costoPersona = rs.getFloat("costo_persona");

                Itinerario itinerario = new Itinerario(id, titolo, costoPersona);
                itineraries.add(itinerario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(cs);
            close(conn);
        }

        return itineraries;
    }

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