package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OvernightStay {
    private final int id;
    private final Date startDate;
    private final Date endDate;
    private final String localita;
    private final Integer hotelCode; // Usiamo Integer per permettere il valore null

    public OvernightStay(int id, Date startDate, Date endDate, String localita, Integer hotelCode) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.localita = localita;
        this.hotelCode = hotelCode;
    }

    public int getId() { return id; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public String getLocalita() { return localita; }
    public Integer getHotelCode() { return hotelCode; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String hotelInfo = (getHotelCode() != null) ? "Albergo Codice: " + getHotelCode() : "Albergo non assegnato";

        return String.format("Pernottamento #%d | dal %s al %s in '%s' | %s",
                getId(),
                sdf.format(getStartDate()),
                sdf.format(getEndDate()),
                getLocalita(),
                hotelInfo);
    }
}