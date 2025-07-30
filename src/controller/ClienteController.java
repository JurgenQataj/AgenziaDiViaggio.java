package controller;

import View.ClienteView;
import model.BookingDetails;
import model.Role;
import model.Trip;
import model.dao.BookDAO;
import model.dao.CancelBookingDAO;
import model.dao.ConnectionFactory;
import model.dao.ListClientBookingsDAO;
import model.dao.ListTripsDAO;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteController implements Controller {

    private final ClienteView view;
    private final AppController appController; // Riferimento per il logout
    private final String clientEmail;

    public ClienteController(AppController appController, String clientEmail) {
        this.appController = appController;
        this.clientEmail = clientEmail;
        this.view = new ClienteView();
    }

    @Override
    public void start() {
        ConnectionFactory.changeRole(Role.CLIENTE);
        boolean running = true;
        while (running) {
            try {
                int choice = view.showMenu();
                switch (choice) {
                    case 1:
                        listAvailableTrips();
                        break;
                    case 2:
                        bookTrip();
                        break;
                    case 3:
                        cancelBooking();
                        break;
                    case 4:
                        appController.logout(); // Chiama il logout sull'AppController
                        running = false; // Esce dal suo ciclo
                        break;
                    default:
                        view.showMessage("Opzione non valida.");
                }
            } catch (Exception e) {
                view.printError(e);
            }
        }
    }

    private void listAvailableTrips() throws SQLException {
        view.showMessage("\n--- Viaggi Disponibili ---");
        List<Trip> trips = new ListTripsDAO(Date.valueOf(LocalDate.now())).execute();
        view.printObjects(trips);
    }

    private void bookTrip() throws SQLException, IOException {
        view.showMessage("\n--- Prenota un Viaggio ---");
        listAvailableTrips();

        int tripId = view.getTripIdToBook();
        int participants = view.getNumberOfParticipants();

        int bookingCode = new BookDAO(tripId, this.clientEmail, participants).execute();
        view.showMessage("Prenotazione effettuata con successo! Il tuo codice è: " + bookingCode);
    }

    private void cancelBooking() throws SQLException, IOException {
        view.showMessage("\n--- Cancella una Prenotazione ---");

        List<BookingDetails> myBookings = new ListClientBookingsDAO(this.clientEmail).execute();

        if (myBookings.isEmpty()) {
            view.showMessage("Non hai nessuna prenotazione attiva da poter cancellare.");
            return;
        }

        view.showMessage("Le tue prenotazioni attive:");
        view.printObjects(myBookings);

        int bookingCodeToCancel = view.getBookingCodeToCancel();
        if (bookingCodeToCancel <= 0) {
            view.showMessage("Cancellazione annullata.");
            return;
        }

        new CancelBookingDAO(bookingCodeToCancel).execute();
        view.showMessage("Prenotazione con codice " + bookingCodeToCancel + " cancellata con successo.");
    }
}