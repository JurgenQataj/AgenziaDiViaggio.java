package controller;

import model.Booking;
import model.Trip;
import model.dao.BookDAO;
import model.dao.CancelBookingDAO;
import model.dao.ListCancellableBookingsDAO;
import model.dao.ListTripsDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClienteController {
    private final Scanner scanner;
    private final String userEmail;
    private final AppController appController;

    public ClienteController(Scanner scanner, String userEmail, AppController appController) {
        this.scanner = scanner;
        this.userEmail = userEmail;
        this.appController = appController;
    }

    public void start() {
        boolean running = true;
        while (running) {
            showClienteMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                // --- INIZIO CORREZIONE ---
                switch (choice) {
                    case 1 -> listAvailableTrips();
                    case 2 -> bookTrip();
                    case 3 -> cancelBooking();
                    case 4 -> { // Usiamo le parentesi graffe per eseguire più istruzioni
                        appController.logout();
                        running = false; // Impostiamo running a false per uscire dal ciclo
                    }
                    default -> System.out.println("Scelta non valida. Riprova.");
                }
                // --- FINE CORREZIONE ---
            } catch (NumberFormatException e) {
                System.out.println("ERRORE: Inserisci un numero valido.");
            } catch (SQLException e) {
                System.out.println("ERRORE DATABASE: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERRORE INASPETTATO: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showClienteMenu() {
        System.out.println("\n--- MENU CLIENTE --- Benvenuto " + userEmail + "!");
        System.out.println("1) Visualizza tutti i viaggi disponibili");
        System.out.println("2) Prenota un viaggio");
        System.out.println("3) Cancella una prenotazione");
        System.out.println("4) Logout");
        System.out.print("Scegli un'opzione: ");
    }

    private void listAvailableTrips() throws SQLException {
        System.out.println("\n--- Elenco Viaggi Futuri Disponibili ---");
        List<Trip> trips = new ListTripsDAO(Date.valueOf(LocalDate.now())).execute();
        if (trips.isEmpty()) {
            System.out.println("Nessun viaggio futuro trovato.");
        } else {
            trips.forEach(System.out::println);
        }
    }

    private void bookTrip() throws SQLException {
        System.out.println("\n--- Esegui una Nuova Prenotazione ---");
        System.out.print("Inserisci l'ID del viaggio che vuoi prenotare: ");
        int tripId = Integer.parseInt(scanner.nextLine());
        System.out.print("Per quante persone vuoi prenotare? ");
        int participants = Integer.parseInt(scanner.nextLine());

        int bookingCode = new BookDAO(tripId, this.userEmail, participants).execute();
        System.out.printf("=> Prenotazione effettuata con successo! Il tuo codice di prenotazione è: %d%n", bookingCode);
    }

    private void cancelBooking() throws SQLException {
        System.out.println("\n--- Cancella una Prenotazione ---");
        System.out.println("Ricerca delle tue prenotazioni ancora cancellabili in corso...");

        List<Booking> cancellableBookings = new ListCancellableBookingsDAO(this.userEmail).execute();

        if (cancellableBookings.isEmpty()) {
            System.out.println("Non hai nessuna prenotazione che possa essere cancellata.");
            return;
        }

        System.out.println("Ecco le tue prenotazioni che puoi ancora cancellare:");
        cancellableBookings.forEach(System.out::println);

        System.out.print("\nInserisci il codice della prenotazione da cancellare: ");
        int code = Integer.parseInt(scanner.nextLine());

        new CancelBookingDAO(code).execute();
        System.out.println("Prenotazione con codice " + code + " cancellata con successo.");
    }
}