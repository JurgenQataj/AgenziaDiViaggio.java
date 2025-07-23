package controller;

import View.SegreteriaView;
import model.*;
import model.dao.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SegreteriaController {

    private final SegreteriaView view;
    private final AppController appController;

    public SegreteriaController(AppController appController) {
        this.appController = appController;
        this.view = new SegreteriaView();
    }

    // Inserisci questa versione nel tuo file src/controller/SegreteriaController.java

    public void start() {
        ConnectionFactory.changeRole(Role.SEGRETERIA);
        boolean running = true;
        while (running) {
            try {
                int choice = view.showMenu();
                switch (choice) {
                    case 1 -> createTrip();
                    case 2 -> createItinerario();
                    case 3 -> insertNewPlace();
                    case 4 -> insertNewHotel();
                    case 5 -> insertNewBus();
                    case 6 -> listTrips();
                    case 7 -> listTripDetails();
                    case 8 -> listPlaces();
                    case 9 -> listHotels();
                    case 10 -> generateReports();
                    case 11 -> assignBusToTrip();
                    case 12 -> assignHotelForOvernightStay();
                    case 13 -> bookTrip();
                    case 14 -> cancelBooking();
                    case 15 -> {
                        appController.logout();
                        running = false;
                    }
                    default -> view.showMessage("Opzione non valida. Riprova.");
                }
                // --- BLOCCO CATCH MODIFICATO ---
            } catch (SQLException e) {
                // Se l'eccezione è una SQLException (dal database), mostra solo il messaggio.
                // Questo gestisce elegantemente le regole di business come la chiusura delle prenotazioni.
                view.showMessage("ERRORE: " + e.getMessage());
            } catch (IOException | ParseException | NumberFormatException e) {
                // Gestisce errori di input comuni senza mostrare dettagli tecnici.
                view.showMessage("ERRORE DI INPUT: Controlla i dati inseriti e riprova. (" + e.getMessage() + ")");
            } catch (Exception e) {
                // Gestisce tutti gli altri errori imprevisti e mostra i dettagli per il debug.
                view.showMessage("Si è verificato un errore inaspettato.");
                view.printError(e); // Mostra lo stack trace solo per errori gravi
            }
            // --- FINE MODIFICA ---
        }
    }

    /**
     * Gestisce la creazione di un nuovo viaggio.
     * Utilizza il nuovo metodo della View per selezionare un itinerario e una data di partenza.
     */
    private void createTrip() throws SQLException, IOException, ParseException {
        view.showMessage("\n--- Creazione Nuovo Viaggio da Itinerario ---");
        List<Itinerario> itinerariDisponibili = new ListItinerariDAO().execute();
        if (itinerariDisponibili == null || itinerariDisponibili.isEmpty()) {
            view.showMessage("Nessun itinerario-modello disponibile. Creane prima uno.");
            return;
        }

        // 1. Chiama il nuovo metodo della View che gestisce l'interazione
        Trip tripDetails = view.getNewTripDetails(itinerariDisponibili);

        // L'utente potrebbe aver annullato l'operazione nella view
        if (tripDetails == null) {
            view.showMessage("Creazione viaggio annullata.");
            return;
        }

        // 2. Estrai i dati dall'oggetto Trip e chiama il DAO
        int itinerarioId = tripDetails.getItinerarioId();
        Date sqlStartDate = new Date(tripDetails.getDataPartenza().getTime());

        int nuovoViaggioId = new InsertTripDAO(itinerarioId, sqlStartDate).execute();

        // 3. Mostra il risultato. Tutta la logica di creazione dei pernottamenti è ora nel DB.
        if (nuovoViaggioId != -1) {
            view.showMessage("=> Viaggio creato con successo con ID: " + nuovoViaggioId);
            view.showMessage("I pernottamenti sono stati generati automaticamente.");
        } else {
            view.showMessage("ERRORE: La creazione del viaggio è fallita.");
        }
        view.showMessage("--- Creazione Viaggio Completata ---");
    }

    /**
     * Gestisce la creazione di un nuovo itinerario-modello.
     * Utilizza il nuovo metodo interattivo della View per costruire l'itinerario con le sue tappe.
     */
    private void createItinerario() throws SQLException, IOException {
        // 1. Recupera le località disponibili da mostrare all'utente
        List<Place> luoghiDisponibili = new ListPlacesDAO().execute();
        if (luoghiDisponibili.isEmpty()) {
            view.showMessage("Nessuna località disponibile nel sistema. Inseriscine almeno una prima di creare un itinerario.");
            return;
        }

        // 2. Chiama il nuovo metodo della View che gestisce la raccolta dati
        Itinerario nuovoItinerario = view.getNewItinerarioDetails(luoghiDisponibili);

        // 3. Esegui il DAO per salvare il nuovo itinerario
        try {
            new InsertItinerarioDAO(nuovoItinerario).execute();
            view.showMessage("=> Itinerario-modello '" + nuovoItinerario.getTitolo() + "' creato con successo!");
        } catch (SQLException e) {
            // Gestisce in modo specifico l'errore del DB sul limite di 7 giorni
            if (e.getMessage().contains("La durata totale dell'itinerario non può superare i 7 giorni")) {
                view.printError(new Exception("La durata totale delle tappe non può superare 7 giorni."));
            } else {
                throw e; // Rilancia altri errori SQL
            }
        }
    }


    // =================================================================
    // METODI ESISTENTI (INVARIATI)
    // =================================================================

    private void insertNewPlace() throws SQLException, IOException {
        view.showMessage("\n--- Inserimento Nuova Località ---");
        Place place = view.getPlaceValues();
        new InsertPlaceDAO(place).execute();
        view.showMessage("Nuova località inserita con successo.");
    }

    private void insertNewBus() throws SQLException, IOException {
        Autobus bus = view.getAutobusValues();
        new InsertBusDAO(bus).execute();
        view.showMessage("Nuovo autobus con targa " + bus.getTarga() + " inserito con successo.");
    }

    private void insertNewHotel() throws SQLException, IOException {
        List<Place> places = new ListPlacesDAO().execute();
        Hotel hotel = view.getHotelValues(places);
        new InsertHotelDAO(hotel).execute();
        view.showMessage("Nuovo albergo inserito con successo.");
    }

    private void listTrips() throws SQLException {
        view.showMessage("\n--- Elenco Viaggi Futuri ---");
        List<Trip> trips = new ListTripsDAO(Date.valueOf(LocalDate.now())).execute();
        view.printObjects(trips);
    }

    private void listTripDetails() throws SQLException, IOException {
        int tripId = view.getTripId();
        view.showMessage("\n--- Dettagli per il Viaggio #" + tripId + " ---");

        List<OvernightStay> details = new TripDetailsDAO(tripId).execute();
        if (details.isEmpty()){
            view.showMessage("Tappe del Viaggio: Nessuna tappa trovata.");
        } else {
            view.showMessage("Tappe del Viaggio:");
            view.printObjects(details);
        }
        view.showMessage("-----------------------------------------");

        List<Autobus> assignedBuses = new ListAssignedBusesDAO(tripId).execute();
        if (assignedBuses.isEmpty()) {
            view.showMessage("Autobus Assegnati: Nessuno");
        } else {
            view.showMessage("Autobus Assegnati:");
            view.printObjects(assignedBuses);
        }
        view.showMessage("-----------------------------------------");

        int participantCount = new GetParticipantCountDAO(tripId).execute();
        view.showMessage("=> Numero Totale Partecipanti: " + participantCount);
        view.showMessage("-----------------------------------------");
    }

    private void listPlaces() throws SQLException {
        view.showMessage("\n--- Elenco Località ---");
        view.printObjects(new ListPlacesDAO().execute());
    }

    private void listHotels() throws SQLException {
        view.showMessage("\n--- Elenco Alberghi ---");
        view.printObjects(new ListHotelsDAO().execute());
    }

    private void generateReports() throws SQLException, IOException {
        int tripId = view.getTripId();
        TripReport report = new ReportDAO(tripId).execute();

        if (report != null) {
            view.showMessage(report.toString());
        } else {
            view.showMessage("Impossibile generare il report per il viaggio con ID: " + tripId);
        }
    }

    private void assignBusToTrip() throws SQLException, IOException {
        view.showMessage("\n--- Assegnazione Interattiva Autobus ---");

        int tripId = view.getTripId();
        int participantsCount = new GetParticipantCountDAO(tripId).execute();
        if (participantsCount == 0) {
            view.showMessage("ATTENZIONE: Nessun partecipante per questo viaggio. L'assegnazione non è necessaria.");
            return;
        }
        view.showMessage("=> Viaggio #" + tripId + " | Partecipanti da trasportare: " + participantsCount);
        view.showMessage("--------------------------------------------------");

        List<Autobus> availableBuses = new ListAvailableBusesDAO(tripId).execute();
        if (availableBuses.isEmpty()) {
            view.showMessage("ATTENZIONE: Nessun autobus disponibile per le date di questo viaggio.");
            return;
        }
        view.showMessage("Autobus disponibili per le date del viaggio:");
        view.printObjects(availableBuses);
        view.showMessage("--------------------------------------------------");

        int capacityCovered = 0;

        while (true) {
            view.showMessage("Stato attuale -> Capienza coperta: " + capacityCovered + " / " + participantsCount);
            String targaInput = view.getInput("Inserisci la targa di un bus da aggiungere (o 'fine' per terminare): ");

            if (targaInput.equalsIgnoreCase("fine")) {
                break;
            }

            Optional<Autobus> busToAdd = availableBuses.stream()
                    .filter(b -> b.getTarga().equalsIgnoreCase(targaInput))
                    .findFirst();

            if (busToAdd.isPresent()) {
                Autobus bus = busToAdd.get();
                new AssignBusDAO(tripId, bus.getTarga()).execute();
                view.showMessage("=> Autobus " + bus.getTarga() + " assegnato!");

                availableBuses.remove(bus);
                capacityCovered += bus.getCapienza();
            } else {
                view.showMessage("ERRORE: La targa non è valida o l'autobus è già stato assegnato in questa sessione.");
            }
        }

        view.showMessage("\n--- Assegnazione Completata ---");
        if (capacityCovered < participantsCount) {
            view.showMessage("ATTENZIONE: La capienza totale (" + capacityCovered + ") è inferiore al numero di partecipanti (" + participantsCount + ").");
        } else {
            view.showMessage("Successo! Capienza coperta: " + capacityCovered + " posti per " + participantsCount + " partecipanti.");
        }
    }

    private void assignHotelForOvernightStay() throws SQLException, IOException {
        view.showMessage("\n--- Assegnazione Alberghi (Sessione Interattiva) ---");
        int tripId = view.getTripId();
        int participantsCount = new GetParticipantCountDAO(tripId).execute();
        view.showMessage("=> Gestione Viaggio #" + tripId + " | Partecipanti: " + participantsCount);

        while (true) {
            List<OvernightStay> overnightStays = new TripDetailsDAO(tripId).execute();
            if (overnightStays.isEmpty()) {
                view.showMessage("Nessun pernottamento trovato per questo viaggio.");
                break;
            }
            view.showMessage("\nStato attuale dei pernottamenti per il viaggio #" + tripId + ":");
            view.printObjects(overnightStays);
            view.showMessage("--------------------------------------------------");

            String input = view.getInput("Inserisci l'ID del pernottamento a cui assegnare un albergo (o 'fine' per terminare): ");
            if (input.equalsIgnoreCase("fine")) {
                break;
            }

            int pernottamentoId = Integer.parseInt(input);

            Optional<OvernightStay> targetStay = overnightStays.stream()
                    .filter(os -> os.getId() == pernottamentoId)
                    .findFirst();

            if (targetStay.isEmpty()) {
                view.showMessage("ERRORE: ID pernottamento non valido.");
                continue;
            }
            String location = targetStay.get().getLocalita();

            List<Hotel> availableHotels = new ListAvailableHotelsDAO(tripId, location).execute();
            if (availableHotels.isEmpty()) {
                view.showMessage("ATTENZIONE: Nessun albergo con capienza sufficiente trovato in '" + location + "'.");
                continue;
            }
            view.showMessage("Alberghi disponibili in '" + location + "' con capienza sufficiente:");
            view.printObjects(availableHotels);
            view.showMessage("--------------------------------------------------");

            int hotelCode = view.getHotelCode();

            new AssignHotelDAO(pernottamentoId, hotelCode).execute();
            view.showMessage("=> Albergo con codice " + hotelCode + " assegnato con successo al pernottamento #" + pernottamentoId);
        }
        view.showMessage("\n--- Sessione di Assegnazione Alberghi Terminata ---");
    }

    private void bookTrip() throws SQLException, IOException {
        int tripId = view.getTripId();
        String clientEmail = view.getClientEmail();
        int participants = view.getParticipants();
        int bookingCode = new BookDAO(tripId, clientEmail, participants).execute();
        view.showMessage("Prenotazione effettuata. Il codice è: " + bookingCode);
    }

    private void cancelBooking() throws SQLException, IOException {
        view.showMessage("\n--- Gestione/Cancellazione Prenotazioni Cliente ---");
        String clientEmail = view.getClientEmail();
        List<BookingDetails> clientBookings = new ListClientBookingsDAO(clientEmail).execute();

        if (clientBookings.isEmpty()) {
            view.showMessage("Nessuna prenotazione attiva trovata per il cliente: " + clientEmail);
            return;
        }

        view.showMessage("Prenotazioni attive per " + clientEmail + ":");
        view.printObjects(clientBookings);

        int bookingCodeToCancel = view.getBookingCodeToCancel();
        if (bookingCodeToCancel <= 0) {
            view.showMessage("Operazione annullata.");
            return;
        }

        new CancelBookingDAO(bookingCodeToCancel).execute();
        view.showMessage("Prenotazione con codice " + bookingCodeToCancel + " cancellata con successo.");
    }
}