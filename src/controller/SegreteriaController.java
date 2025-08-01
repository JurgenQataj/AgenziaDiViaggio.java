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


public class SegreteriaController implements Controller {

    private final SegreteriaView view;
    private final AppController appController; // Riferimento per il logout

    public SegreteriaController(AppController appController) {
        this.appController = appController;
        this.view = new SegreteriaView();
    }

    @Override
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
                        appController.logout(); // Chiama il logout sull'AppController
                        running = false; // Esce dal suo ciclo
                    }
                    default -> view.showMessage("Opzione non valida. Riprova.");
                }
            } catch (Exception e) {

                view.showMessage("ERRORE: " + e.getMessage());
            }
        }
    }

    private void createTrip() throws SQLException, IOException, ParseException {
        view.showMessage("\n--- Creazione Nuovo Viaggio da Itinerario ---");
        List<Itinerario> itinerariDisponibili = new ListItinerariDAO().execute();
        if (itinerariDisponibili == null || itinerariDisponibili.isEmpty()) {
            view.showMessage("Nessun itinerario-modello disponibile. Creane prima uno.");
            return;
        }


        Trip tripDetails = view.getNewTripDetails(itinerariDisponibili);

        if (tripDetails == null) {
            view.showMessage("Creazione viaggio annullata.");
            return;
        }

        int itinerarioId = tripDetails.getItinerarioId();
        Date sqlStartDate = new Date(tripDetails.dataPartenza().getTime());

        int nuovoViaggioId = new InsertTripDAO(itinerarioId, sqlStartDate).execute();

        if (nuovoViaggioId != -1) {
            view.showMessage("=> Viaggio creato con successo con ID: " + nuovoViaggioId);
            view.showMessage("I pernottamenti sono stati generati automaticamente.");
        } else {
            view.showMessage("ERRORE: La creazione del viaggio è fallita.");
        }
        view.showMessage("--- Creazione Viaggio Completata ---");
    }

    private void createItinerario() throws SQLException, IOException {
        // 1. Recupera le località disponibili da mostrare all'utente
        List<Place> luoghiDisponibili = new ListPlacesDAO().execute();
        if (luoghiDisponibili.isEmpty()) {
            view.showMessage("Nessuna località disponibile nel sistema. Inseriscine almeno una prima di creare un itinerario.");
            return;
        }

        Itinerario nuovoItinerario = view.getNewItinerarioDetails(luoghiDisponibili);

        try {
            new InsertItinerarioDAO(nuovoItinerario).execute();
            view.showMessage("=> Itinerario-modello '" + nuovoItinerario.getTitolo() + "' creato con successo!");
        } catch (SQLException e) {

            if (e.getMessage().contains("La durata totale dell'itinerario non può superare i 7 giorni")) {
                view.printError(new Exception("La durata totale delle tappe non può superare 7 giorni."));
            } else {
                throw e;
            }
        }
    }

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

        while (true) {

            List<Autobus> assignedBuses = new ListAssignedBusesDAO(tripId).execute();
            List<Autobus> availableBuses = new ListAvailableBusesDAO(tripId).execute();

            // Ricalcola la capienza coperta in base ai dati aggiornati
            int capacityCovered = assignedBuses.stream().mapToInt(Autobus::getCapienza).sum();


            view.showMessage("Stato attuale -> Capienza coperta: " + capacityCovered + " / " + participantsCount);
            if (!assignedBuses.isEmpty()) {
                view.showMessage("Autobus già assegnati:");
                view.printObjects(assignedBuses);
            }
            view.showMessage("--------------------------------------------------");

            if (availableBuses.isEmpty()) {
                view.showMessage("ATTENZIONE: Nessun altro autobus disponibile per le date di questo viaggio.");
                break;
            }
            view.showMessage("Autobus ancora disponibili:");
            view.printObjects(availableBuses);
            view.showMessage("--------------------------------------------------");

            String targaInput = view.getInput("Inserisci la targa di un bus da aggiungere (o 'fine' per terminare): ");

            if (targaInput.equalsIgnoreCase("fine")) {
                break;
            }

            // Cerca il bus nella lista di quelli disponibili
            Optional<Autobus> busToAdd = availableBuses.stream()
                    .filter(b -> b.getTarga().equalsIgnoreCase(targaInput))
                    .findFirst();

            if (busToAdd.isPresent()) {
                // Se trovato, esegui l'assegnazione nel database
                new AssignBusDAO(tripId, targaInput).execute();
                view.showMessage("=> Autobus " + targaInput + " assegnato!");
            } else {
                view.showMessage("ERRORE: La targa non è valida o l'autobus non è disponibile.");
            }
        }


        int finalCapacity = new ListAssignedBusesDAO(tripId).execute().stream().mapToInt(Autobus::getCapienza).sum();
        view.showMessage("\n--- Assegnazione Completata ---");
        if (finalCapacity < participantsCount) {
            view.showMessage("ATTENZIONE: La capienza totale (" + finalCapacity + ") è inferiore al numero di partecipanti (" + participantsCount + ").");
        } else {
            view.showMessage("Successo! Capienza coperta: " + finalCapacity + " posti per " + participantsCount + " partecipanti.");
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