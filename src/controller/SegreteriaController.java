package controller;

import exceptions.DatabaseException;
import model.*;
import model.dao.*;
import view.SegreteriaView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
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

        int choice;
        do {
            choice = segreteriaView.showMenu();
            try {
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
                    default -> throw new RuntimeException("Opzione non valida");
                }
            } catch (SQLException | DatabaseException | IOException e) {
                System.err.println(e.getMessage());
            }
        } while (choice != 13);
    }

    private void createTrip() throws SQLException, DatabaseException {
        Trip trip = segreteriaView.getTripValues();
        InsertTripDAO dao = new InsertTripDAO();
        dao.execute(trip);

        segreteriaView.showMessage("Creazione del viaggio avvenuta con successo.");
        int n = segreteriaView.getOvernightsNumber();
        for (int i = 0; i < n; i++) {
            insertOvernightStay(trip);
        }
    }

    private void insertOvernightStay(Trip trip) throws DatabaseException {
        OvernightStay overnightStay = segreteriaView.getOvernightData();
        overnightStay.setTrip(trip);
        String placeName = segreteriaView.getPlaceName();
        overnightStay.setPlace(new GetPlaceDAO().execute(placeName));
        InsertOvernightStayDAO insertOvernightStayDAO = new InsertOvernightStayDAO();
        insertOvernightStayDAO.execute(overnightStay);
    }

    private void insertNewPlace() throws DatabaseException {
        Place place = segreteriaView.getPlaceValues();
        new InsertPlaceDAO().execute(place);
    }

    private void insertNewHotel() throws DatabaseException {
        Hotel hotel = segreteriaView.getHotelValues();
        hotel.setPlace(new GetPlaceDAO().execute(segreteriaView.getPlaceName()));

        InsertHotelDAO insertHotelDAO = new InsertHotelDAO();
        insertHotelDAO.execute(hotel);
    }

    private void listTrips() throws DatabaseException {
        Date date = segreteriaView.getDate();
        List<Trip> trips = new ListTripsDAO().execute(date);
        segreteriaView.printObjects(trips);
    }

    private void listTripDetails() throws DatabaseException {
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

    private void generateReports() throws DatabaseException {
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
        Date date = segreteriaView.getDate();
        int code = segreteriaView.getHotelCode();

        AssignHotelDAO assignHotelDAO = new AssignHotelDAO();
        assignHotelDAO.execute(title, date, code);
    }

    private void bookTrip() throws DatabaseException {
        String trip = segreteriaView.getTripTitle();
        int participants = segreteriaView.getParticipants();
        Booking booking = new Booking(trip, credentials.getUsername(), participants);
        BookDAO bookDAO = new BookDAO();
        booking = bookDAO.execute(booking);
        segreteriaView.showMessage(String.format("Codice prenotazione: %d", booking.getCode()));
    }

    private void cancelBooking() throws DatabaseException {
        int code = segreteriaView.getBookingCode();
        CancelBookingDAO cancelBookingDAO = new CancelBookingDAO();
        cancelBookingDAO.execute(code);
    }
}
