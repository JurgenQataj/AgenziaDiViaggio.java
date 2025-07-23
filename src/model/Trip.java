package model;

import java.util.Date;
import java.util.List;

/**
 * Rappresenta un'istanza concreta di un viaggio, con date specifiche e
 * informazioni operative come i pernottamenti e gli autobus assegnati.
 */
public class Trip {

    // --- Campi resi non-final per permettere l'uso dei setters ---
    private int id;
    private Date dataPartenza;
    private Date dataRientro;
    private Itinerario itinerario;
    private int itinerarioId;

    // --- Campi per il report ---
    private List<OvernightStay> overnightStays;
    private List<Autobus> assignedBuses;
    private int participantCount;

    /**
     * -------- COSTRUTTORE VUOTO AGGIUNTO (LA SOLUZIONE) --------
     * Costruttore vuoto necessario per creare un'istanza "segnaposto" nella View
     * e popolarla gradualmente prima di passarla al Controller.
     */
    public Trip() {
        // Lasciare vuoto. I valori verranno impostati tramite i metodi setter.
    }

    /**
     * Costruttore completo, utile per quando si recuperano tutti i dati
     * dal database in una sola volta.
     */
    public Trip(int id, Date dataPartenza, Date dataRientro, Itinerario itinerario) {
        this.id = id;
        this.dataPartenza = dataPartenza;
        this.dataRientro = dataRientro;
        this.itinerario = itinerario;
        if (itinerario != null) {
            this.itinerarioId = itinerario.getId();
        }
    }

    // --- GETTERS E SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataPartenza() {
        return dataPartenza;
    }

    public void setDataPartenza(Date dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public Date getDataRientro() {
        return dataRientro;
    }

    public void setDataRientro(Date dataRientro) {
        this.dataRientro = dataRientro;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    public int getItinerarioId() {
        // Se l'ID non è stato impostato direttamente, lo deriviamo dall'oggetto itinerario
        if (itinerarioId == 0 && itinerario != null) {
            return itinerario.getId();
        }
        return itinerarioId;
    }

    public void setItinerarioId(int itinerarioId) {
        this.itinerarioId = itinerarioId;
    }


    // --- Getters e Setters per i dati del report ---

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

    @Override
    public String toString() {
        return String.format("Viaggio #%d | %s | Partenza: %s | Rientro: %s | Costo: %.2f €",
                id,
                itinerario != null ? itinerario.getTitolo() : "N/D",
                new java.sql.Date(dataPartenza.getTime()),
                new java.sql.Date(dataRientro.getTime()),
                itinerario != null ? itinerario.getCostoPersona() : 0.0);
    }
}