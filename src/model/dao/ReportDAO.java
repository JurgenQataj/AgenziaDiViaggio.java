package model.dao;

import model.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class ReportDAO {
    private final int tripId;

    public ReportDAO(int tripId) {
        this.tripId = tripId;
    }

    public TripReport execute() throws SQLException {

        // --- FASE 1: CARICAMENTO AUTONOMO DI TUTTI I DATI ---
        // Questo DAO è indipendente. Carica da solo tutto ciò di cui ha bisogno,
        // usando gli altri DAO esistenti. In questo modo è impossibile avere dati nulli.

        List<Trip> allTrips = new ListTripsDAO(Date.valueOf("1970-01-01")).execute();

        // Uso i metodi corretti che ho letto dal tuo codice (es. getId())
        Optional<Trip> tripOptional = allTrips.stream()
                .filter(t -> t.getId() == this.tripId)
                .findFirst();

        if (tripOptional.isEmpty()) {
            System.err.println("ERRORE DAO: Viaggio con ID " + this.tripId + " non trovato.");
            return null;
        }
        Trip trip = tripOptional.get();

        // Queste liste vengono caricate come variabili locali, quindi non saranno mai null.
        List<OvernightStay> pernottamenti = new TripDetailsDAO(this.tripId).execute();
        List<Autobus> busAssegnati = new ListAssignedBusesDAO(this.tripId).execute();
        int partecipanti = new GetParticipantCountDAO(this.tripId).execute();
        List<Hotel> tuttiGliHotel = new ListHotelsDAO().execute();


        // --- FASE 2: CALCOLO DEL REPORT CON I DATI APPENA CARICATI ---

        BigDecimal costoPersona = new BigDecimal(String.valueOf(trip.getItinerario().getCostoPersona()));
        BigDecimal revenue = costoPersona.multiply(new BigDecimal(partecipanti));

        // Conversione delle date compatibile con la tua versione di Java
        long tripDays = ChronoUnit.DAYS.between(
                new Timestamp(trip.getStartDate().getTime()).toLocalDateTime().toLocalDate(),
                new Timestamp(trip.getEndDate().getTime()).toLocalDateTime().toLocalDate()
        ) + 1;

        BigDecimal busCosts = BigDecimal.ZERO;
        for (Autobus bus : busAssegnati) {
            BigDecimal costoGiornalieroBus = new BigDecimal(String.valueOf(bus.getCostoGiornaliero()));
            busCosts = busCosts.add(costoGiornalieroBus.multiply(new BigDecimal(tripDays)));
        }

        BigDecimal hotelCosts = BigDecimal.ZERO;
        // Il ciclo usa la lista locale 'pernottamenti', che non può essere null
        for (OvernightStay stay : pernottamenti) {
            if (stay.getHotelCode() != null && stay.getHotelCode() > 0) {
                Optional<Hotel> hotelOpt = tuttiGliHotel.stream()
                        .filter(h -> h.getCodice() == stay.getHotelCode())
                        .findFirst();
                if (hotelOpt.isPresent()) {
                    Hotel hotel = hotelOpt.get();

                    long stayNights = ChronoUnit.DAYS.between(
                            new Timestamp(stay.getStartDate().getTime()).toLocalDateTime().toLocalDate(),
                            new Timestamp(stay.getEndDate().getTime()).toLocalDateTime().toLocalDate()
                    );

                    if (stayNights > 0) {
                        BigDecimal costoNotteHotel = new BigDecimal(String.valueOf(hotel.getCostoNottePersona()));
                        BigDecimal costForStay = costoNotteHotel
                                .multiply(new BigDecimal(partecipanti))
                                .multiply(new BigDecimal(stayNights));
                        hotelCosts = hotelCosts.add(costForStay);
                    }
                }
            }
        }

        BigDecimal totalCosts = busCosts.add(hotelCosts);
        BigDecimal profitOrLoss = revenue.subtract(totalCosts);

        // Passiamo anche la lista 'pernottamenti' al costruttore di TripReport
        // per risolvere l'errore in fase di stampa (toString).
        return new TripReport(trip.getItinerario().getTitolo(), partecipanti, profitOrLoss.doubleValue(), pernottamenti);
    }
}