package model.dao;

import model.Itinerario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertItinerarioDAO implements BaseDAO {
    private final Itinerario itinerario;

    public InsertItinerarioDAO(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    /**
     * Esegue la stored procedure 'create_itinerary' per salvare un nuovo
     * itinerario-modello nel database.
     */
    @Override
    public Void execute() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConnectionFactory.getConnection();
            // Chiama la procedura con il nome corretto che abbiamo appena creato
            cs = conn.prepareCall("{CALL create_itinerary(?, ?)}");

            cs.setString(1, itinerario.getTitolo());
            cs.setDouble(2, itinerario.getCostoPersona());

            cs.execute();

        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return null;
    }
}