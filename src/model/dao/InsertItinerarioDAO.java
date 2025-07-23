package model.dao;

import model.Itinerario;
import model.Tappa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * DAO to insert a new itinerary, including its stages, into the database.
 * This class implements the BaseDAO interface.
 */
public class InsertItinerarioDAO implements BaseDAO<Void> {

    private final Itinerario itinerario;

    /**
     * Constructor for the DAO.
     * @param itinerario The complete Itinerario object to be saved.
     */
    public InsertItinerarioDAO(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    /**
     * Executes the call to the 'create_itinerary' stored procedure.
     * @return null upon successful execution.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Void execute() throws SQLException {
        // Formats the list of stages into a semicolon-separated string.
        // Example: "Varsavia,2;Berlino,3;Roma,2"
        String tappeStr = itinerario.getTappe().stream()
                .map(t -> t.getLocalita() + "," + t.getGiorni())
                .collect(Collectors.joining(";"));

        final String procedure = "{CALL create_itinerary(?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            cs.setString(1, itinerario.getTitolo());
            cs.setDouble(2, itinerario.getCostoPersona());
            cs.setString(3, tappeStr); // Passes the third argument to the DB.

            cs.execute();
        }
        return null;
    }
}