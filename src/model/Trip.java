package model;

import java.util.Date;
import java.util.List;

public class Trip {
    private int id;
    private Date startDate;
    private Date endDate;
    private Itinerario itinerario; // Riferimento all'itinerario-template
    private List<OvernightStay> overnightStays; // Uso il nome originale del tuo campo

    // Costruttore aggiornato
    public Trip(int id, Date startDate, Date endDate, Itinerario itinerario) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itinerario = itinerario;
    }

    // Getters e Setters
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

    public List<OvernightStay> getOvernightStays() {
        return overnightStays;
    }

    public void setOvernightStays(List<OvernightStay> overnightStays) {
        this.overnightStays = overnightStays;
    }

    // Metodo toString aggiornato per riflettere la nuova struttura
    @Override
    public String toString() {
        // Formattazione simile alla tua classe ListTripsDAO
        return String.format("%-5d %-40s %-15s %-15s %-10.2f",
                this.id,
                this.itinerario.getTitolo(),
                this.startDate.toString(),
                this.endDate.toString(),
                this.itinerario.getCostoPersona());
    }
}