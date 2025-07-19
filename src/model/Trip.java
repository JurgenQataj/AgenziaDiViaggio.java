package model;

import java.util.Date;

public class Trip {
    private final int id;
    private final Date startDate;
    private final Date endDate;
    private final Itinerario itinerario; // Un viaggio è sempre basato su un itinerario

    public Trip(int id, Date startDate, Date endDate, Itinerario itinerario) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itinerario = itinerario;
    }

    // Getters per accedere ai dati del viaggio
    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    @Override
    public String toString() {
        // Formatta l'output per una facile lettura nella console
        return String.format("Viaggio #%d | %s | Partenza: %s | Rientro: %s | Costo: %.2f €",
                id,
                itinerario.getTitolo(),
                new java.sql.Date(startDate.getTime()), // Formattazione più pulita
                new java.sql.Date(endDate.getTime()),
                itinerario.getCostoPersona());
    }
}