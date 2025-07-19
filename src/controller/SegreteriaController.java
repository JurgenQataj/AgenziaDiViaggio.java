package controller;

import View.SegreteriaView;
import model.*;
import model.dao.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

public class SegreteriaController {

    private final SegreteriaView view;
    private final AppController appController;

    public SegreteriaController(AppController appController) {
        this.appController = appController;
        this.view = new SegreteriaView();
    }

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
            } catch (Exception e) {
                view.printError(e);
            }
        }
    }

    private void createTrip() throws SQLException, IOException, ParseException {
        view.showMessage("\n--- Inizio Creazione Nuovo Viaggio ---");
        List<Itinerario> itinerari = new ListItinerariDAO().execute();

        int itinerarioId = view.askForItinerario(itinerari);
        if (itinerarioId == 0) {
            view.showMessage("Creazione viaggio annullata.");
            return;
        }

        java.util.Date[] dates = view.getTripDates();
        Date sqlStartDate = new Date(dates[0].getTime());
        Date sqlEndDate = new Date(dates[1].getTime());

        int nuovoViaggioId = new InsertTripDAO(itinerarioId, sqlStartDate, sqlEndDate).execute();
        view.showMessage("=> Viaggio creato con successo con ID: " + nuovoViaggioId);

        int numPernottamenti = view.getOvernightsNumber();

        if (numPernottamenti > 0) {
            List<Place> availablePlaces = new ListPlacesDAO().execute();
            for (int i = 0; i < numPernottamenti; i++) {
                OvernightStay pernottamento = view.getOvernightData(availablePlaces);
                new InsertOvernightStayDAO(nuovoViaggioId, pernottamento).execute();
                view.showMessage("--> Tappa " + (i + 1) + " aggiunta con successo!");
            }
        }
        view.showMessage("--- Creazione Viaggio Completata ---");
    }

    private void createItinerario() throws SQLException, IOException {
        view.showMessage("\n--- Creazione Nuovo Itinerario-Modello ---");
        Itinerario nuovoItinerario = view.getItinerarioValues();
        new InsertItinerarioDAO(nuovoItinerario).execute();
        view.showMessage("Itinerario-modello '" + nuovoItinerario.getTitolo() + "' creato con successo!");
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
            view.showMessage("\n--- Report per il Viaggio #" + tripId + " ---");
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

        // 1. CHIEDI L'ID DEL VIAGGIO (UNA SOLA VOLTA ALL'INIZIO)
        int tripId = view.getTripId();
        int participantsCount = new GetParticipantCountDAO(tripId).execute();
        view.showMessage("=> Gestione Viaggio #" + tripId + " | Partecipanti: " + participantsCount);

        while (true) {
            // 2. AD OGNI GIRO, MOSTRA LO STATO AGGIORNATO DEI PERNOTTAMENTI
            List<OvernightStay> overnightStays = new TripDetailsDAO(tripId).execute();
            if (overnightStays.isEmpty()) {
                view.showMessage("Nessun pernottamento trovato per questo viaggio.");
                break;
            }
            view.showMessage("\nStato attuale dei pernottamenti per il viaggio #" + tripId + ":");
            view.printObjects(overnightStays);
            view.showMessage("--------------------------------------------------");

            // 3. CHIEDI QUALE PERNOTTAMENTO GESTIRE O SE USCIRE
            String input = view.getInput("Inserisci l'ID del pernottamento a cui assegnare un albergo (o 'fine' per terminare): ");
            if (input.equalsIgnoreCase("fine")) {
                break; // L'utente ha finito, esci dal ciclo
            }

            int pernottamentoId = Integer.parseInt(input);

            // Trova la località del pernottamento scelto
            Optional<OvernightStay> targetStay = overnightStays.stream()
                    .filter(os -> os.getId() == pernottamentoId)
                    .findFirst();

            if (targetStay.isEmpty()) {
                view.showMessage("ERRORE: ID pernottamento non valido.");
                continue; // Torna all'inizio del ciclo
            }
            String location = targetStay.get().getLocalita();

            // 4. MOSTRA GLI ALBERGHI ADATTI IN QUELLA LOCALITÀ
            List<Hotel> availableHotels = new ListAvailableHotelsDAO(tripId, location).execute();
            if (availableHotels.isEmpty()) {
                view.showMessage("ATTENZIONE: Nessun albergo con capienza sufficiente trovato in '" + location + "'.");
                continue; // Torna all'inizio del ciclo
            }
            view.showMessage("Alberghi disponibili in '" + location + "' con capienza sufficiente:");
            view.printObjects(availableHotels);
            view.showMessage("--------------------------------------------------");

            // 5. CHIEDI QUALE ALBERGO ASSEGNARE
            int hotelCode = view.getHotelCode();

            // Esegui l'assegnazione
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
        int code = view.getBookingCode();
        new CancelBookingDAO(code).execute();
        view.showMessage("Prenotazione con codice " + code + " cancellata.");
    }
}