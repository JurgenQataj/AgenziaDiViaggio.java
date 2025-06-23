package controller;

import exceptions.DatabaseException;
import model.*;
import model.dao.*;
import view.SegreteriaView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SegreteriaController implements Controller {

    private final SegreteriaView segreteriaView;
    private final LoginCredentials credentials;

    public SegreteriaController(LoginCredentials credentials) {
        this.credentials = credentials;
        this.segreteriaView = new SegreteriaView();
    }

    @Override
    public void start() {
        try {
            ConnectionFactory.changeRole(Role.SEGRETERIA);
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel cambio di ruolo", e);
        }

        int choice = 0;
        try {
            do {
                choice = segreteriaView.showMenu();
                // Questo è lo switch completo e corretto con tutti i case
                switch (choice) {
                    case 1 -> createTrip();
                    case 2 -> insertNewPlace();
                    case 3 -> insertNewHotel();
                    case 4 -> listTrips();
                    case 5 -> listTripDetails();
                    case 6 -> listPlaces();
                    case 7 -> listHotels();
                    case 8 -> generateReports();
                    case 9 -> assignBusToTrip();
                    case 10 -> assignHotelForOvernightStay();
                    case 11 -> bookTrip();
                    case 12 -> cancelBooking();
                    case 13 -> segreteriaView.showMessage("Sessione terminata");
                    default -> segreteriaView.showMessage("Opzione non valida");
                }
            } while (choice != 13);
        } catch (SQLException | DatabaseException | IOException e) {
            System.err.println("Si è verificato un errore critico: " + e.getMessage());
        }
    }

    private void createTrip() throws SQLException, DatabaseException, IOException {
        Trip trip = segreteriaView.getTripValues();
        new InsertTripDAO().execute(trip);
        segreteriaView.showMessage("Creazione del viaggio avvenuta con successo.");
        int n = segreteriaView.getOvernightsNumber();
        for (int i = 0; i < n; i++) {
            insertOvernightStay(trip);
        }
    }

    private void insertOvernightStay(Trip trip) throws DatabaseException, IOException {
        OvernightStay overnightStay = segreteriaView.getOvernightData();
        if (overnightStay == null) return;
        overnightStay.setTrip(trip);
        String placeName = segreteriaView.getPlaceName();
        Place place = new Place();
        place.setName(placeName);
        overnightStay.setPlace(place);
        new InsertOvernightStayDAO().execute(overnightStay);
        segreteriaView.showMessage("Pernottamento inserito con successo per la località: " + placeName);
    }

    private void insertNewPlace() throws DatabaseException, IOException {
        Place place = segreteriaView.getPlaceValues();
        new InsertPlaceDAO().execute(place);
    }

    private void insertNewHotel() throws DatabaseException, IOException {
        Hotel hotel = segreteriaView.getHotelValues();
        String placeName = segreteriaView.getPlaceName();
        Place place = new Place();
        place.setName(placeName);
        hotel.setPlace(place);
        new InsertHotelDAO().execute(hotel);
    }

    private void listTrips() throws DatabaseException, IOException {
        LocalDate localDate = segreteriaView.askForDate("Inserire la data per filtrare i viaggi (gg/mm/aaaa): ");
        Date date = Date.valueOf(localDate);
        List<Trip> trips = new ListTripsDAO().execute(date);
        segreteriaView.printObjects(trips);
    }

    private void listTripDetails() throws DatabaseException, IOException {
        String tripTitle = segreteriaView.getTripTitle();
        TripDetailsDAO detailsDAO = new TripDetailsDAO();
        segreteriaView.printObjects(detailsDAO.execute(tripTitle));
    }

    private void listPlaces() throws DatabaseException {
        segreteriaView.printObjects(new ListPlacesDAO().execute());
    }

    private void listHotels() throws DatabaseException {
        segreteriaView.printObjects(new ListHotelsDAO().execute());
    }

    private void generateReports() throws DatabaseException, IOException {
        ReportDAO reportDAO = new ReportDAO();
        TripReport report = reportDAO.execute(segreteriaView.getTripTitle());
        segreteriaView.showMessage(report.toString());
    }

    private void assignBusToTrip() throws IOException, DatabaseException {
        String tripTitle = segreteriaView.getTripTitle();
        List<String> busPlates = segreteriaView.getBusPlates();
        AssignBusDAO assignBusDAO = new AssignBusDAO();
        assignBusDAO.execute(tripTitle, busPlates.toArray());
    }

    private void assignHotelForOvernightStay() throws IOException, DatabaseException {
        String title = segreteriaView.getTripTitle();
        LocalDate localDate = segreteriaView.askForDate("Inserire la data del pernottamento (gg/mm/aaaa): ");
        Date date = Date.valueOf(localDate);
        int code = segreteriaView.getHotelCode();
        AssignHotelDAO assignHotelDAO = new AssignHotelDAO();
        assignHotelDAO.execute(title, date, code);
    }

    private void bookTrip() throws DatabaseException, IOException {
        String trip = segreteriaView.getTripTitle();
        int participants = segreteriaView.getParticipants();
        Booking booking = new Booking(trip, credentials.getUsername(), participants);
        BookDAO bookDAO = new BookDAO();
        booking = bookDAO.execute(booking);
        segreteriaView.showMessage(String.format("Codice prenotazione: %d", booking.getCode()));
    }

    private void cancelBooking() throws DatabaseException, IOException {
        int code = segreteriaView.getBookingCode();
        CancelBookingDAO cancelBookingDAO = new CancelBookingDAO();
        cancelBookingDAO.execute(code);
    }
}