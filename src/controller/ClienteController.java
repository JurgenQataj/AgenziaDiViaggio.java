package controller;

import exceptions.DatabaseException;
import model.*;
import model.dao.*;
import view.ClienteView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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
        int choice = 0;
        // La clausola catch è stata corretta per non catturare più SQLException,
        // che non viene mai lanciata direttamente nel blocco try.
        try {
            do {
                choice = clienteView.showMenu();
                switch (choice) {
                    case 1 -> listTrips();
                    case 2 -> listTripDetails();
                    case 3 -> bookTrip();
                    case 4 -> cancelBooking();
                    case 5 -> clienteView.showMessage("Sessione terminata.");
                    default -> clienteView.showMessage("Opzione non valida.");
                }
            } while (choice != 5);
        } catch (DatabaseException | IOException e) { // RIMOSSO SQLException da qui
            System.err.println("Si è verificato un errore: " + e.getMessage());
        }
    }

    // Le firme dei metodi che interagiscono con i DAO mantengono throws DatabaseException
    private void listTrips() throws DatabaseException, IOException {
        Date date = clienteView.getDate();
        List<Trip> trips = new ListTripsDAO().execute(date);
        clienteView.printObjects(trips);
    }

    private void listTripDetails() throws DatabaseException, IOException {
        String tripTitle = clienteView.getTripTitle();
        List<OvernightStay> details = new TripDetailsDAO().execute(tripTitle);
        clienteView.printObjects(details);
    }

    private void bookTrip() throws DatabaseException, IOException {
        String trip = clienteView.getTripTitle();
        int participants = clienteView.getParticipants();
        Booking booking = new Booking(trip, credentials.getUsername(), participants);
        BookDAO bookDAO = new BookDAO();
        booking = bookDAO.execute(booking);
        clienteView.showMessage(String.format("Codice prenotazione: %d", booking.getCode()));
    }

    private void cancelBooking() throws DatabaseException, IOException {
        int code = clienteView.getBookingCode();
        CancelBookingDAO cancelBookingDAO = new CancelBookingDAO();
        cancelBookingDAO.execute(code);
        clienteView.showMessage("Prenotazione cancellata con successo.");
    }
}
