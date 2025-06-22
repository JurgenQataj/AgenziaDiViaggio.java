package model.dao;

import exceptions.DatabaseException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AssignBusDAO implements BaseDAO<List<String>> {

    @Override
    public List<String> execute(Object... params) throws DatabaseException {
        String tripTitle = (String) params[0];
        List<String> plates = List.of(Arrays.toString(Arrays.copyOfRange(params, 1, params.length - 1)));

        try {
            Connection connection = ConnectionFactory.getConnection();
            CallableStatement stmt = connection.prepareCall("{call assign_bus(?,?)}");
            stmt.setString(1, tripTitle);
            for (String plate : plates) {
                stmt.setString(2, plate);
                stmt.executeUpdate();
            }
            return plates;
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Errore nell'assegnazione dei bus: %s", e.getMessage()), e);
        }
    }
}