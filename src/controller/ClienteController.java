package controller;

import exceptions.DatabaseException;
import model.LoginCredentials;
import model.Role;
import model.Trip;
import model.dao.BookDAO;
import model.dao.CancelBookingDAO;
import model.dao.ConnectionFactory;
import model.dao.ListTripsDAO;
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
        try {
            ConnectionFactory.changeRole(Role.CLIENTE);
        } catch (Exception e) {
            // Se il cambio ruolo fallisce, è un errore critico
            throw new RuntimeException("Errore critico nell'impostare il ruolo del cliente", e);
        }

        int choice = 0;
        do {
            try {
                choice = clienteView.showMenu();
                switch (choice) {
                    case 1 -> listAvailableTrips();
                    case 2 -> bookTrip();
                    case 3 -> cancelBooking();
                    case 4 -> clienteView.showMessage("Sessione terminata. Arrivederci!");
                    default -> clienteView.showMessage("Opzione non valida.");
                }
                // Questo blocco ora è corretto, perché i metodi chiamati sopra
                // dichiarano che possono lanciare queste eccezioni.
            } catch (SQLException e) {
                clienteView.printError(new DatabaseException("Errore del database: " + e.getMessage()));
            } catch (IOException e) {
                clienteView.printError(new DatabaseException("Errore di input/output: " + e.getMessage()));
            }
        } while (choice != 4);
    }

    // Le firme dei metodi ora includono "throws SQLException"

    private void listAvailableTrips() throws SQLException {
        clienteView.showMessage("\n--- Lista dei Viaggi Disponibili ---");
        List<Trip> trips = new ListTripsDAO(Date.valueOf(LocalDate.now())).execute();

        if (trips.isEmpty()) {
            clienteView.showMessage("Nessun viaggio disponibile al momento.");
        } else {
            clienteView.showMessage(String.format("%-5s %-40s %-15s %-15s %-10s", "ID", "TITOLO", "PARTENZA", "RIENTRO", "COSTO"));
            clienteView.printObjects(trips);
        }
    }

    private void bookTrip() throws IOException, SQLException {
        listAvailableTrips();
        int tripId = clienteView.getTripId();
        int participants = clienteView.getParticipants();

        String clientEmail = this.credentials.getUsername();

        int bookingCode = new BookDAO(tripId, clientEmail, participants).execute();

        clienteView.showMessage(String.format("=> Prenotazione effettuata con successo! Il tuo codice prenotazione è: %d", bookingCode));
        clienteView.showMessage("Conserva questo codice per un'eventuale cancellazione.");
    }

    private void cancelBooking() throws IOException, SQLException {
        int bookingCode = clienteView.getBookingCode();
        new CancelBookingDAO(bookingCode).execute();
        clienteView.showMessage("Prenotazione con codice " + bookingCode + " cancellata correttamente.");
    }
}