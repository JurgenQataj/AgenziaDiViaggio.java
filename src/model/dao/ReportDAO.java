package model.dao;

import model.OvernightStay;
import model.TripReport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO per generare un report finanziario e di dettaglio per un viaggio specifico.
 */
public class ReportDAO implements BaseDAO<TripReport> {

    private final int tripId;

    public ReportDAO(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public TripReport execute() throws SQLException {
        TripReport report = null;
        final String procedure = "{CALL report(?)}";

        try (Connection conn = ConnectionFactory.getConnection()) {
            String titoloViaggio = "";
            int partecipanti = 0;
            double guadagnoOPerdita = 0.0;

            // 1. Esegui la procedura per ottenere i dati finanziari
            try (CallableStatement cs = conn.prepareCall(procedure)) {
                cs.setInt(1, this.tripId);
                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        titoloViaggio = rs.getString("titolo_viaggio");
                        partecipanti = rs.getInt("partecipanti");
                        guadagnoOPerdita = rs.getDouble("guadagno_o_perdita");
                    }
                }
            }

            // 2. Recupera la lista dei pernottamenti
            List<OvernightStay> pernottamenti = new TripDetailsDAO(this.tripId).execute();

            // 3. Crea l'oggetto TripReport completo con tutti i dati
            report = new TripReport(titoloViaggio, partecipanti, guadagnoOPerdita, pernottamenti);
        }
        return report;
    }
}