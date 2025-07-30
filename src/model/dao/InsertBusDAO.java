package model.dao;

import model.Autobus;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertBusDAO implements BaseDAO<Void> {
    private final Autobus autobus;

    public InsertBusDAO(Autobus autobus) {
        this.autobus = autobus;
    }

    @Override
    public Void execute() throws SQLException {

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL create_bus(?, ?, ?)}")) {

            cs.setString(1, autobus.getTarga());
            cs.setInt(2, autobus.getCapienza());
            cs.setDouble(3, autobus.getCostoGiornaliero());

            cs.execute();

        }

        return null;
    }
}