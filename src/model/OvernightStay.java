package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OvernightStay {

    private Trip trip;
    private LocalDate startDate;
    private LocalDate endDate;
    private Place place;
    private Hotel hotel;

    public OvernightStay(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Trip getTrip() {
        return trip;
    }

    public Date getStartDate() {
        return Date.valueOf(startDate);
    }

    public Date getEndDate() {
        return Date.valueOf(endDate);
    }

    public Place getPlace() {
        return place;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return String.format("· %s - %s: %s, %s (%s)",
                startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                hotel.getName(),
                place.getName(),
                place.getCountry()
        );
    }
}
