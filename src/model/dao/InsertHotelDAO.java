package model.dao;

import model.Hotel;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertHotelDAO implements BaseDAO<Void> {

    private final Hotel hotel;

    /**
     * Questo è il nuovo costruttore che risolve l'errore.
     * Accetta un oggetto Hotel e lo salva per usarlo nel metodo execute().
     * @param hotel L'oggetto Hotel con i dati da inserire.
     */
    public InsertHotelDAO(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public Void execute() throws SQLException {
        // Chiama la procedura "new_hotel" dal tuo dump SQL
        final String procedure = "{CALL new_hotel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(procedure)) {

            // Usa i dati dall'oggetto hotel salvato
            cs.setString(1, this.hotel.getNome());
            cs.setString(2, this.hotel.getReferente());
            cs.setInt(3, this.hotel.getCapienza());
            cs.setString(4, this.hotel.getVia());
            cs.setString(5, this.hotel.getCivico());
            cs.setString(6, this.hotel.getCp());
            cs.setString(7, this.hotel.getEmail());
            cs.setString(8, this.hotel.getTelefono());
            cs.setString(9, this.hotel.getFax());
            cs.setString(10, this.hotel.getPlace().getName()); // Prende il nome dalla località associata
            cs.setFloat(11, this.hotel.getCostoNottePersona());

            cs.execute();
        }
        return null;
    }
}