package model.dao;

import model.Itinerario;
import model.Tappa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class InsertItinerarioDAO implements BaseDAO<Void> {

    private final Itinerario itinerario;

    public InsertItinerarioDAO(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    @Override
    public Void execute() throws SQLException {

        String tappeStr = itinerario.getTappe().stream()
                .map(t -> t.getLocalita() + "," + t.getGiorni())
                .collect(Collectors.joining(";"));

        final String procedure = "{CALL create_itinerary(?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setString(1, itinerario.getTitolo());
            cs.setDouble(2, itinerario.getCostoPersona());
            cs.setString(3, tappeStr);

            cs.execute();
        }
        return null;
    }
}