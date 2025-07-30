package model.dao;

import model.Place;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertPlaceDAO implements BaseDAO<Void> {
    private final Place place;

    public InsertPlaceDAO(Place place) {
        this.place = place;
    }

    @Override
    public Void execute() throws SQLException {

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL new_location(?, ?)}")) {

            cs.setString(1, place.getNome());
            cs.setString(2, place.getPaese());

            cs.execute();

        }

        return null;
    }
}