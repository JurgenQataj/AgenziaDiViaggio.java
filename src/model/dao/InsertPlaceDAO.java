package model.dao;

import model.Place;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertPlaceDAO implements BaseDAO<Void> {

    private final Place place;

    /**
     * Questo è il nuovo costruttore che risolve l'errore.
     * Accetta un oggetto Place e lo salva per usarlo nel metodo execute().
     * @param place L'oggetto Place con i dati da inserire.
     */
    public InsertPlaceDAO(Place place) {
        this.place = place;
    }

    @Override
    public Void execute() throws SQLException {
        // Chiama la procedura "new_location" dal tuo dump SQL
        final String procedure = "{CALL new_location(?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            // Usa i dati dall'oggetto place salvato
            cs.setString(1, this.place.getName());
            cs.setString(2, this.place.getCountry());
            cs.execute();
        }
        return null; // Il metodo non restituisce nulla
    }
}