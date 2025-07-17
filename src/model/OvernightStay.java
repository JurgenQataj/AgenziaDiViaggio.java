package model;

import java.util.Date;

public class OvernightStay {
    private int id;
    private Date startDate;
    private Date endDate;
    private Place place;
    private Hotel hotel; // Questo campo ora può essere NULL

    // Costruttore aggiornato per accettare un hotel nullo
    public OvernightStay(int id, Date startDate, Date endDate, Place place, Hotel hotel) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
        this.hotel = hotel;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() { return endDate; }

    public Place getPlace() { return place; }

    public Hotel getHotel() {
        return hotel;
    }

    // Metodo toString aggiornato per gestire hotel non assegnato
    @Override
    public String toString() {
        String hotelName = (hotel != null) ? hotel.getNome() : "Non ancora assegnato";
        return String.format("Pernottamento a: %-20s | Dal: %s | Al: %s | Albergo: %s",
                this.place.getName(),
                this.startDate.toString(),
                this.endDate.toString(),
                hotelName);
    }
}
