package model.dao;

import model.Itinerario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertItinerarioDAO implements BaseDAO<Void> {

    private final Itinerario itinerario;

    public InsertItinerarioDAO(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    @Override
    public Void execute() throws SQLException {
        // Usiamo una semplice query SQL perché non c'è una procedura specifica per questo
        final String query = "INSERT INTO itinerario (titolo, costo_persona) VALUES (?, ?);";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, this.itinerario.getTitolo());
            pstmt.setFloat(2, this.itinerario.getCostoPersona());
            pstmt.executeUpdate();
        }
        return null;
    }
}