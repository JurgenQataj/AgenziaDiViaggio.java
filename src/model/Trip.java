package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Trip(int id, Date dataPartenza, Date dataRientro, Itinerario itinerario) {

    public int getItinerarioId() {
        return (itinerario != null) ? itinerario.getId() : 0;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String partenzaStr = (dataPartenza != null) ? sdf.format(dataPartenza) : "N/D";
        String rientroStr = (dataRientro != null) ? sdf.format(dataRientro) : "N/D";
        String titolo = (itinerario != null) ? itinerario.getTitolo() : "N/D";
        double costo = (itinerario != null) ? itinerario.getCostoPersona() : 0.0;

        return String.format("Viaggio #%d | %s | Partenza: %s | Rientro: %s | Costo: %.2f €",
                id, titolo, partenzaStr, rientroStr, costo);
    }
}