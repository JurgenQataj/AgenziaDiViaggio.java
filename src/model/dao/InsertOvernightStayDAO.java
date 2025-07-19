package model.dao;

import model.OvernightStay;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class InsertOvernightStayDAO implements BaseDAO {
    private final int viaggioId;
    private final OvernightStay overnightStay;

    public InsertOvernightStayDAO(int viaggioId, OvernightStay overnightStay) {
        this.viaggioId = viaggioId;
        this.overnightStay = overnightStay;
    }

    /**
     * Esegue la stored procedure 'insert_overnight_stay' per salvare un nuovo
     * pernottamento nel database.
     */
    @Override
    public Void execute() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL insert_overnight_stay(?, ?, ?, ?)}");

            cs.setInt(1, this.viaggioId);
            // Converte java.util.Date in java.sql.Date, richiesto dal driver JDBC
            cs.setDate(2, new Date(overnightStay.getStartDate().getTime()));
            cs.setDate(3, new Date(overnightStay.getEndDate().getTime()));

            // Usa il metodo getter corretto 'getLocalita()'
            cs.setString(4, overnightStay.getLocalita());

            cs.execute();

        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return null;
    }
}