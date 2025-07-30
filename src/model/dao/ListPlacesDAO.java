package model.dao;

import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPlacesDAO implements BaseDAO<List<Place>> {

    public ListPlacesDAO() {

    }

    @Override
    public List<Place> execute() throws SQLException {
        final String procedure = "{CALL list_places()}";
        List<Place> places = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Place place = new Place(
                        rs.getString("nome"),
                        rs.getString("paese")
                );
                places.add(place);
            }
        }
        return places;
    }
}