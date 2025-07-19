package model;

import java.util.Date;
import java.util.List; // -> Importa la classe List

public class Trip {
    private final int id;
    private final Date startDate;
    private final Date endDate;
    private final Itinerario itinerario;

    // --- NUOVI CAMPI AGGIUNTI PER IL REPORT ---
    // Questi campi verranno popolati dal controller prima di generare il report.
    private List<OvernightStay> overnightStays;
    private List<Autobus> assignedBuses;
    private int participantCount;
    // -----------------------------------------


    public Trip(int id, Date startDate, Date endDate, Itinerario itinerario) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itinerario = itinerario;
    }

    // --- GETTERS ESISTENTI ---
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

    // --- NUOVI GETTERS E SETTERS AGGIUNTI ---
    public List<OvernightStay> getOvernightStays() {
        return overnightStays;
    }

    public void setOvernightStays(List<OvernightStay> overnightStays) {
        this.overnightStays = overnightStays;
    }



    public List<Autobus> getAssignedBuses() {
        return assignedBuses;
    }

    public void setAssignedBuses(List<Autobus> assignedBuses) {
        this.assignedBuses = assignedBuses;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
    // -----------------------------------------


    @Override
    public String toString() {
        return String.format("Viaggio #%d | %s | Partenza: %s | Rientro: %s | Costo: %.2f €",
                id,
                itinerario.getTitolo(),
                new java.sql.Date(startDate.getTime()),
                new java.sql.Date(endDate.getTime()),
                itinerario.getCostoPersona());
    }
}