package controller;

import exceptions.DatabaseException;
import model.Booking;
import model.LoginCredentials;
import model.Role;
import model.Trip;
import model.dao.*;
import view.ClienteView;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ClienteController implements Controller {

    private final ClienteView clienteView;

    private final LoginCredentials credentials;

    public ClienteController(LoginCredentials credentials) {
        this.credentials = credentials;
        this.clienteView = new ClienteView();
    }

    @Override
    public void start() {
        try {
            ConnectionFactory.changeRole(Role.CLIENTE);
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel cambio di ruolo", e);
        }

        int choice;
        do {
            choice = clienteView.showMenu();
            try {
                switch (choice) {
                    case 1 -> listTrips();
                    case 2 -> listTripDetails();
                    case 3 -> bookTrip();
                    case 4 -> cancelBooking();
                    case 5 -> clienteView.showMessage("Sessione terminata");
                    default -> throw new RuntimeException("Opzione non valida");
                }
            } catch (DatabaseException e) {
                System.err.println(e.getMessage());
            }
        } while (choice != 5);
    }

    private void listTrips() throws DatabaseException {
        Date date = clienteView.getDate();
        List<Trip> trips = new ListTripsDAO().execute(date);
        clienteView.printObjects(trips);
    }

    private void listTripDetails() throws DatabaseException {
        String tripTitle = clienteView.getTripTitle();
        TripDetailsDAO detailsDAO = new TripDetailsDAO();
        clienteView.printObjects(detailsDAO.execute(tripTitle));
    }

    private void bookTrip() throws DatabaseException {
        String trip = clienteView.getTripTitle();
        int participants = clienteView.getParticipants();
        Booking booking = new Booking(trip, credentials.getUsername(), participants);
        BookDAO bookDAO = new BookDAO();
        booking = bookDAO.execute(booking);
        clienteView.showMessage(String.format("Codice prenotazione: %d", booking.getCode()));
    }

    private void cancelBooking() throws DatabaseException {
        int code = clienteView.getBookingCode();
        CancelBookingDAO cancelBookingDAO = new CancelBookingDAO();
        cancelBookingDAO.execute(code);
    }
}
