package model.dao;

import model.Place;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertPlaceDAO implements BaseDAO {
    private final Place place;

    public InsertPlaceDAO(Place place) {
        this.place = place;
    }

    @Override
    public Void execute() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionFactory.getConnection();
            cs = conn.prepareCall("{CALL new_location(?, ?)}");

            // Ora questo codice funzionerà perché Place ha i metodi getter
            cs.setString(1, place.getNome());
            cs.setString(2, place.getPaese());

            cs.execute();

        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return null;
    }
}