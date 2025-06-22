package model.dao;

import exceptions.DatabaseException;
import model.Hotel;
import model.OvernightStay;
import model.Place;
import model.TripReport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO implements BaseDAO<TripReport> {

    @Override
    public TripReport execute(Object... params) throws DatabaseException {
        String trip = (String) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call report(?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setString(1, trip);

            ResultSet rs1 = stmt.executeQuery();
            TripReport report;
            if (rs1.first())
                report = new TripReport(trip, rs1.getInt("partecipanti"), rs1.getDouble("guadagno"));
            else
                throw new DatabaseException("Viaggio non trovato");
            stmt.getMoreResults();

            ResultSet rs2 = stmt.getResultSet();
            List<OvernightStay> overnightStays = new ArrayList<>();
            OvernightStay overnightStay;
            while (rs2.next()) {
                overnightStay = new OvernightStay(rs2.getDate("data_inizio").toLocalDate(), rs2.getDate("data_fine").toLocalDate());
                overnightStay.setPlace(new Place(rs2.getString("localita"), rs2.getString("paese")));
                overnightStay.setHotel(new Hotel(rs2.getString("nome")));

                overnightStays.add(overnightStay);
            }
            report.setOvernightStays(overnightStays);
            return report;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nella generazione del report: %s", e.getMessage()), e);
        }
    }
}