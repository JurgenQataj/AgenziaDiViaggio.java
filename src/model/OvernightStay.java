package model;

import java.util.Date;

public class OvernightStay {
    private final int id;
    private final Date startDate;
    private final Date endDate;
    private final String localita; // La località è una String, come nel DB
    private final Integer hotelCode;

    /**
     * Costruttore completo per creare un oggetto Pernottamento.
     * Accetta i dati così come vengono letti dal database.
     */
    public OvernightStay(int id, Date startDate, Date endDate, String localita, Integer hotelCode) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.localita = localita;
        this.hotelCode = hotelCode;
    }

    /**
     * Costruttore semplificato per creare nuove tappe (l'hotel è sconosciuto).
     */
    public OvernightStay(Date startDate, Date endDate, String localita) {
        this(0, startDate, endDate, localita, null);
    }

    // Getters
    public int getId() { return id; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public String getLocalita() { return localita; }
    public Integer getHotelCode() { return hotelCode; }

    @Override
    public String toString() {
        String hotelInfo = (hotelCode != null) ? "Albergo Codice: " + hotelCode : "Albergo non assegnato";
        return String.format("Pernottamento #%d | dal %s al %s in '%s' | %s",
                id,
                new java.sql.Date(startDate.getTime()),
                new java.sql.Date(endDate.getTime()),
                localita,
                hotelInfo);
    }
}