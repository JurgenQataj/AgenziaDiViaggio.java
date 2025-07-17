package controller;

import exceptions.DatabaseException;
import model.*;
import model.dao.*;
import view.SegreteriaView;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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
        ConnectionFactory.changeRole(Role.SEGRETERIA);

        int choice = 0;
        do {
            try {
                choice = segreteriaView.showMenu();
                // Lo switch ora è aggiornato con la nuova numerazione
                switch (choice) {
                    case 1 -> createTrip();
                    case 2 -> createItinerario(); // NUOVA OPZIONE
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
                    case 14 -> segreteriaView.showMessage("Sessione terminata. Arrivederci!"); // AGGIORNATO
                    default -> segreteriaView.showMessage("Opzione non valida. Riprova.");
                }
            } catch (SQLException e) {
                segreteriaView.printError(new DatabaseException("Errore del database: " + e.getMessage()));
            } catch (IOException e) {
                segreteriaView.printError(new DatabaseException("Errore di input/output: " + e.getMessage()));
            } catch (Exception e) {
                segreteriaView.showMessage("Si è verificato un errore inaspettato: " + e.getMessage());
            }
        } while (choice != 14); // AGGIORNATO
    }

    private void createTrip() throws SQLException, IOException {
        segreteriaView.showMessage("\n--- Inizio Creazione Nuovo Viaggio ---");
        List<Itinerario> itinerari = new ListItinerariDAO().execute();
        int itinerarioId = segreteriaView.askForItinerario(itinerari);
        if (itinerarioId == -1) return;

        Trip tripWithUtilDates = segreteriaView.getTripDates();
        Date sqlStartDate = new Date(tripWithUtilDates.getStartDate().getTime());
        Date sqlEndDate = new Date(tripWithUtilDates.getEndDate().getTime());

        int nuovoViaggioId = new InsertTripDAO(itinerarioId, sqlStartDate, sqlEndDate).execute();
        segreteriaView.showMessage("=> Viaggio creato con successo con ID: " + nuovoViaggioId);

        int numPernottamenti = segreteriaView.getOvernightsNumber();
        if (numPernottamenti > 0) {
            List<Place> availablePlaces = new ListPlacesDAO().execute();
            for (int i = 0; i < numPernottamenti; i++) {
                OvernightStay pernottamento = segreteriaView.getOvernightData(availablePlaces);
                if (pernottamento != null) {
                    new InsertOvernightStayDAO(nuovoViaggioId, pernottamento).execute();
                    segreteriaView.showMessage("--> Tappa " + (i + 1) + " aggiunta con successo!");
                }
            }
        }
        segreteriaView.showMessage("--- Creazione Viaggio Completata ---");
    }

    /**
     * NUOVO METODO: Gestisce la creazione di un itinerario-modello.
     */
    private void createItinerario() throws IOException, SQLException {
        Itinerario nuovoItinerario = segreteriaView.getItinerarioValues();
        new InsertItinerarioDAO(nuovoItinerario).execute();
        segreteriaView.showMessage("Itinerario-modello '" + nuovoItinerario.getTitolo() + "' creato con successo!");
    }

    private void insertNewPlace() throws IOException, SQLException {
        Place place = segreteriaView.getPlaceValues();
        new InsertPlaceDAO(place).execute();
        segreteriaView.showMessage("Nuova località inserita con successo.");
    }

    private void insertNewHotel() throws IOException, SQLException {
        List<Place> places = new ListPlacesDAO().execute();
        Hotel hotel = segreteriaView.getHotelValues(places);

        if (hotel != null) {
            new InsertHotelDAO(hotel).execute();
            segreteriaView.showMessage("Nuovo albergo inserito con successo.");
        }
    }

    private void listTrips() throws SQLException {
        List<Trip> trips = new ListTripsDAO(Date.valueOf(LocalDate.now())).execute();
        segreteriaView.printObjects(trips);
    }

    private void listTripDetails() throws IOException, SQLException {
        int tripId = segreteriaView.getTripId();
        List<OvernightStay> details = new TripDetailsDAO(tripId).execute();
        if (details.isEmpty()){
            segreteriaView.showMessage("Nessun dettaglio trovato per il viaggio con ID: " + tripId);
        } else {
            segreteriaView.showMessage("Dettagli per il viaggio #" + tripId + ":");
            segreteriaView.printObjects(details);
        }
    }

    private void listPlaces() throws SQLException {
        segreteriaView.printObjects(new ListPlacesDAO().execute());
    }

    private void listHotels() throws SQLException {
        segreteriaView.printObjects(new ListHotelsDAO().execute());
    }

    private void generateReports() throws IOException, SQLException {
        int tripId = segreteriaView.getTripId();
        TripReport report = new ReportDAO(tripId).execute();
        if (report != null) {
            segreteriaView.showMessage(report.toString());
        } else {
            segreteriaView.showMessage("Impossibile generare il report per il viaggio con ID: " + tripId);
        }
    }

    private void assignBusToTrip() throws IOException, SQLException {
        int tripId = segreteriaView.getTripId();
        List<String> busPlates = segreteriaView.getBusPlates();
        for(String targa : busPlates) {
            new AssignBusDAO(tripId, targa).execute();
            segreteriaView.showMessage("Autobus " + targa + " assegnato al viaggio " + tripId);
        }
        segreteriaView.showMessage("Assegnazione autobus completata.");
    }

    private void assignHotelForOvernightStay() throws IOException, SQLException {
        int pernottamentoId = segreteriaView.getOvernightStayId();
        int hotelCode = segreteriaView.getHotelCode();
        new AssignHotelDAO(pernottamentoId, hotelCode).execute();
        segreteriaView.showMessage("Albergo con codice " + hotelCode + " assegnato al pernottamento " + pernottamentoId);
    }

    private void bookTrip() throws IOException, SQLException {
        int tripId = segreteriaView.getTripId();
        String clientEmail = segreteriaView.getClientEmail();
        int participants = segreteriaView.getParticipants();
        int bookingCode = new BookDAO(tripId, clientEmail, participants).execute();
        segreteriaView.showMessage(String.format("Prenotazione effettuata. Il codice è: %d", bookingCode));
    }

    private void cancelBooking() throws IOException, SQLException {
        int code = segreteriaView.getBookingCode();
        new CancelBookingDAO(code).execute();
        segreteriaView.showMessage("Prenotazione con codice " + code + " cancellata.");
    }
}