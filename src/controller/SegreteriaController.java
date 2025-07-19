package controller;

import model.*;
import model.dao.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SegreteriaController {

    private final Scanner scanner;
    private final AppController appController;

    public SegreteriaController(Scanner scanner, AppController appController) {
        this.scanner = scanner;
        this.appController = appController;
    }

    public void start() {
        ConnectionFactory.changeRole(Role.SEGRETERIA);
        boolean running = true;
        while (running) {
            showSegreteriaMenu(); // This will now show the full menu
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> createTrip();
                    case 2 -> createItinerario();
                    case 3 -> insertNewPlace();
                    case 4 -> insertNewHotel();
                    case 5 -> listTrips();
                    case 6 -> listTripDetails();
                    case 7 -> listPlaces();
                    case 8 -> listHotels();
                    case 9 -> generateReports();
                    case 10 -> assignBusToTrip();
                    case 11 -> assignHotelForOvernightStay();
                    case 12 -> bookTrip();
                    case 13 -> cancelBooking();
                    case 14 -> {
                        appController.logout();
                        running = false;
                    }
                    default -> System.out.println("Opzione non valida. Riprova.");
                }
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

    /**
     * VERSIONE COMPLETA DEL MENU
     */
    private void showSegreteriaMenu() {
        System.out.println("\n--- MENU SEGRETERIA ---");
        System.out.println("1) Crea un nuovo viaggio da un itinerario");
        System.out.println("2) Crea un nuovo itinerario-modello");
        System.out.println("3) Inserisci una nuova località");
        System.out.println("4) Inserisci un nuovo albergo");
        System.out.println("5) Elenca tutti i viaggi futuri");
        System.out.println("6) Elenca i dettagli di un viaggio");
        System.out.println("7) Elenca tutte le località");
        System.out.println("8) Elenca tutti gli alberghi");
        System.out.println("9) Genera report guadagni/perdite di un viaggio");
        System.out.println("10) Assegna autobus ad un viaggio");
        System.out.println("11) Assegna albergo a un pernottamento");
        System.out.println("12) Esegui una prenotazione per un cliente");
        System.out.println("13) Cancella una prenotazione");
        System.out.println("14) Logout");
        System.out.print("Scegli un'opzione: ");
    }

    private void createTrip() throws SQLException {
        System.out.println("\n--- Inizio Creazione Nuovo Viaggio ---");
        List<Itinerario> itinerari = new ListItinerariDAO().execute();
        System.out.println("Itinerari disponibili:");
        itinerari.forEach(System.out::println);
        System.out.print("Scegli l'ID dell'itinerario-modello: ");
        int itinerarioId = Integer.parseInt(scanner.nextLine());

        System.out.print("Inserisci la data di partenza (YYYY-MM-DD): ");
        Date sqlStartDate = Date.valueOf(scanner.nextLine());
        System.out.print("Inserisci la data di rientro (YYYY-MM-DD): ");
        Date sqlEndDate = Date.valueOf(scanner.nextLine());

        int nuovoViaggioId = new InsertTripDAO(itinerarioId, sqlStartDate, sqlEndDate).execute();
        System.out.println("=> Viaggio creato con successo con ID: " + nuovoViaggioId);

        System.out.print("Quante tappe (pernottamenti) vuoi aggiungere a questo viaggio? ");
        int numPernottamenti = Integer.parseInt(scanner.nextLine());

        if (numPernottamenti > 0) {
            List<Place> availablePlaces = new ListPlacesDAO().execute();
            for (int i = 0; i < numPernottamenti; i++) {
                System.out.println("\n-- Aggiunta Tappa " + (i + 1) + " --");
                System.out.println("Località disponibili:");
                availablePlaces.forEach(p -> System.out.println("- " + p.getNome()));
                System.out.print("Inserisci il nome della località per questa tappa: ");
                String nomeLocalita = scanner.nextLine();
                System.out.print("Data inizio pernottamento (YYYY-MM-DD): ");
                Date dataInizioP = Date.valueOf(scanner.nextLine());
                System.out.print("Data fine pernottamento (YYYY-MM-DD): ");
                Date dataFineP = Date.valueOf(scanner.nextLine());

                OvernightStay pernottamento = new OvernightStay(dataInizioP, dataFineP, nomeLocalita);
                new InsertOvernightStayDAO(nuovoViaggioId, pernottamento).execute();
                System.out.println("--> Tappa " + (i + 1) + " aggiunta con successo!");
            }
        }
        System.out.println("--- Creazione Viaggio Completata ---");
    }

    private void createItinerario() throws SQLException {
        System.out.println("\n--- Creazione Nuovo Itinerario-Modello ---");
        System.out.print("Inserisci il titolo del nuovo itinerario: ");
        String titolo = scanner.nextLine();
        System.out.print("Inserisci il costo per persona: ");
        double costo = Double.parseDouble(scanner.nextLine());
        Itinerario nuovoItinerario = new Itinerario(0, titolo, costo);
        new InsertItinerarioDAO(nuovoItinerario).execute();
        System.out.println("Itinerario-modello '" + nuovoItinerario.getTitolo() + "' creato con successo!");
    }

    private void insertNewPlace() throws SQLException {
        System.out.println("\n--- Inserimento Nuova Località ---");
        System.out.print("Nome della località: ");
        String nome = scanner.nextLine();
        System.out.print("Paese di appartenenza: ");
        String paese = scanner.nextLine();
        Place place = new Place(nome, paese);
        new InsertPlaceDAO(place).execute();
        System.out.println("Nuova località inserita con successo.");
    }

    private void insertNewHotel() throws SQLException {
        System.out.println("\n--- Inserimento Nuovo Albergo ---");
        List<Place> places = new ListPlacesDAO().execute();
        System.out.println("Località esistenti:");
        places.forEach(p -> System.out.println("- " + p.getNome()));

        Place selectedPlace = null;
        while (selectedPlace == null) {
            System.out.print("In quale località si trova l'albergo? ");
            String nomeLocalita = scanner.nextLine();

            Optional<Place> foundPlace = places.stream()
                    .filter(p -> p.getNome().equalsIgnoreCase(nomeLocalita))
                    .findFirst();

            if (foundPlace.isPresent()) {
                selectedPlace = foundPlace.get();
            } else {
                System.out.println("ERRORE: Località non trovata. Riprova.");
            }
        }

        System.out.print("Nome albergo: "); String nome = scanner.nextLine();
        System.out.print("Referente: "); String referente = scanner.nextLine();
        System.out.print("Capienza: "); int capienza = Integer.parseInt(scanner.nextLine());
        System.out.print("Via: "); String via = scanner.nextLine();
        System.out.print("Civico: "); String civico = scanner.nextLine();
        System.out.print("CAP: "); String cp = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Telefono: "); String telefono = scanner.nextLine();
        System.out.print("Fax (opzionale): "); String fax = scanner.nextLine();
        System.out.print("Costo per notte a persona: ");
        double costoNotte = Double.parseDouble(scanner.nextLine());

        Hotel hotel = new Hotel(0, nome, referente, capienza, via, civico, cp, email, telefono, fax, selectedPlace, costoNotte);
        new InsertHotelDAO(hotel).execute();
        System.out.println("Nuovo albergo inserito con successo.");
    }

    private void listTrips() throws SQLException {
        System.out.println("\n--- Elenco Viaggi Futuri ---");
        List<Trip> trips = new ListTripsDAO(Date.valueOf(LocalDate.now())).execute();
        if (trips.isEmpty()) {
            System.out.println("Nessun viaggio futuro trovato.");
        } else {
            trips.forEach(System.out::println);
        }
    }

    private void listTripDetails() throws SQLException {
        System.out.print("Inserisci l'ID del viaggio di cui vuoi vedere i dettagli: ");
        int tripId = Integer.parseInt(scanner.nextLine());
        List<OvernightStay> details = new TripDetailsDAO(tripId).execute();
        if (details.isEmpty()){
            System.out.println("Nessun dettaglio trovato per il viaggio con ID: " + tripId);
        } else {
            System.out.println("\nDettagli per il viaggio #" + tripId + ":");
            details.forEach(System.out::println);
        }
    }

    private void listPlaces() throws SQLException {
        System.out.println("\n--- Elenco Località ---");
        new ListPlacesDAO().execute().forEach(System.out::println);
    }

    private void listHotels() throws SQLException {
        System.out.println("\n--- Elenco Alberghi ---");
        new ListHotelsDAO().execute().forEach(System.out::println);
    }

    private void generateReports() throws SQLException {
        System.out.print("Inserisci l'ID del viaggio per cui generare il report: ");
        int tripId = Integer.parseInt(scanner.nextLine());
        TripReport report = new ReportDAO(tripId).execute();
        if (report != null) {
            System.out.println("\n--- Report per il Viaggio #" + tripId + " ---");
            System.out.println(report);
        } else {
            System.out.println("Impossibile generare il report per il viaggio con ID: " + tripId);
        }
    }

    private void assignBusToTrip() throws SQLException {
        System.out.print("Inserisci l'ID del viaggio a cui assegnare gli autobus: ");
        int tripId = Integer.parseInt(scanner.nextLine());
        System.out.print("Inserisci le targhe degli autobus da assegnare (separate da una virgola): ");
        String[] busPlates = scanner.nextLine().split(",");
        for(String targa : busPlates) {
            new AssignBusDAO(tripId, targa.trim()).execute();
            System.out.println("Autobus " + targa.trim() + " assegnato al viaggio " + tripId);
        }
        System.out.println("Assegnazione autobus completata.");
    }

    private void assignHotelForOvernightStay() throws SQLException {
        System.out.print("Inserisci l'ID del pernottamento a cui assegnare l'albergo: ");
        int pernottamentoId = Integer.parseInt(scanner.nextLine());
        System.out.print("Inserisci il codice dell'albergo da assegnare: ");
        int hotelCode = Integer.parseInt(scanner.nextLine());
        new AssignHotelDAO(pernottamentoId, hotelCode).execute();
        System.out.println("Albergo con codice " + hotelCode + " assegnato al pernottamento " + pernottamentoId);
    }

    private void bookTrip() throws SQLException {
        System.out.print("Inserire l'ID del viaggio da prenotare: ");
        int tripId = Integer.parseInt(scanner.nextLine());
        System.out.print("Inserire l'email del cliente: ");
        String clientEmail = scanner.nextLine();
        System.out.print("Inserire il numero di persone per la prenotazione: ");
        int participants = Integer.parseInt(scanner.nextLine());
        int bookingCode = new BookDAO(tripId, clientEmail, participants).execute();
        System.out.printf("Prenotazione effettuata. Il codice è: %d%n", bookingCode);
    }

    private void cancelBooking() throws SQLException {
        System.out.print("Inserisci il codice della prenotazione da cancellare: ");
        int code = Integer.parseInt(scanner.nextLine());
        new CancelBookingDAO(code).execute();
        System.out.println("Prenotazione con codice " + code + " cancellata.");
    }
}