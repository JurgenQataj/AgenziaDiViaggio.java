package model.dao;

import exceptions.DatabaseException;
import model.OvernightStay;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertOvernightStayDAO implements BaseDAO<OvernightStay> {
    @Override
    public OvernightStay execute(Object... params) throws DatabaseException {
        OvernightStay overnightStay = (OvernightStay) params[0];

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call insert_overnight_stay(?,?,?,?)}");

            stmt.setString(1, overnightStay.getTrip().getTitle());
            stmt.setDate(2, overnightStay.getStartDate());
            stmt.setDate(3, overnightStay.getEndDate());
            stmt.setString(4, overnightStay.getPlace().getName());
            stmt.executeUpdate();
            return overnightStay;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'inserimento del pernottamento: %s", e.getMessage()), e);
        }
    }
}
