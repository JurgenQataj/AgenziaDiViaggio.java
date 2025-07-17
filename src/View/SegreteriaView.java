package view;

import exceptions.DatabaseException;
import model.Hotel;
import model.Itinerario;
import model.OvernightStay;
import model.Place;
import model.Trip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SegreteriaView extends View {

    private final BufferedReader reader;

    public SegreteriaView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    // --- METODI HELPER PRIVATI ---

    private String readLineFromUser() throws IOException {
        String line;
        while ((line = reader.readLine()) != null && line.trim().isEmpty()) {}
        return line;
    }

    private LocalDate parseDateFromUser() throws IOException {
        while (true) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                format.setLenient(false);
                java.util.Date parsedDate = format.parse(readLineFromUser());
                return parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ParseException e) {
                System.err.print("Formato data non valido (gg/mm/aaaa): ");
            }
        }
    }

    private int parseIntFromUser() throws IOException {
        while (true) {
            try {
                return Integer.parseInt(readLineFromUser());
            } catch (NumberFormatException e) {
                System.err.print("Input non valido. Inserire un numero intero: ");
            }
        }
    }

    private double parseDoubleFromUser() throws IOException {
        while (true) {
            try {
                return Double.parseDouble(readLineFromUser());
            } catch (NumberFormatException e) {
                System.err.print("Input non valido. Inserire un numero: ");
            }
        }
    }

    // --- METODI PUBBLICI PER L'INTERAZIONE ---

    public int showMenu() throws IOException {
        System.out.println("\n--- MENU SEGRETERIA ---");
        System.out.println("1)  Crea un nuovo viaggio da un itinerario");
        System.out.println("2)  Crea un nuovo itinerario-modello"); // NUOVA OPZIONE
        System.out.println("3)  Inserisci una nuova località");
        System.out.println("4)  Inserisci un nuovo albergo");
        System.out.println("5)  Elenca tutti i viaggi futuri");
        System.out.println("6)  Elenca i dettagli di un viaggio");
        System.out.println("7)  Elenca tutte le località");
        System.out.println("8)  Elenca tutti gli alberghi");
        System.out.println("9)  Genera report guadagni/perdite di un viaggio");
        System.out.println("10) Assegna autobus ad un viaggio");
        System.out.println("11) Assegna albergo a un pernottamento");
        System.out.println("12) Esegui una prenotazione per un cliente");
        System.out.println("13) Cancella una prenotazione");
        System.out.println("14) Esci"); // AGGIORNATO
        System.out.print("Scegli un'opzione: ");
        return parseIntFromUser();
    }

    public void printError(DatabaseException e) {
        System.err.println("ERRORE DATABASE: " + e.getMessage());
    }

    public int askForItinerario(List<Itinerario> itinerari) throws IOException {
        System.out.println("\n--- Selezione Itinerario ---");
        if (itinerari == null || itinerari.isEmpty()) {
            System.out.println("Nessun itinerario-template disponibile. Creane uno prima con l'opzione 2.");
            return -1;
        }
        itinerari.forEach(System.out::println);
        System.out.print("\nSeleziona l'ID dell'itinerario: ");
        return parseIntFromUser();
    }

    public Trip getTripDates() throws IOException {
        System.out.print("Data di partenza (gg/mm/aaaa): ");
        LocalDate startDate = parseDateFromUser();
        System.out.print("Data del rientro (gg/mm/aaaa): ");
        LocalDate returnDate = parseDateFromUser();

        if (returnDate.isBefore(startDate)) {
            System.err.println("Errore: La data di rientro non può precedere la partenza.");
            return getTripDates();
        }
        return new Trip(0, Date.valueOf(startDate), Date.valueOf(returnDate), null);
    }

    public int getOvernightsNumber() throws IOException {
        System.out.print("Quante tappe (pernottamenti) vuoi aggiungere a questo viaggio? ");
        return parseIntFromUser();
    }

    public OvernightStay getOvernightData(List<Place> availablePlaces) throws IOException {
        System.out.println("\n--- Aggiunta Tappa (Pernottamento) ---");
        if (availablePlaces == null || availablePlaces.isEmpty()) {
            System.out.println("Nessuna località disponibile.");
            return null;
        }
        System.out.println("Località disponibili:");
        availablePlaces.forEach(p -> System.out.println("- " + p.getName()));

        System.out.print("Scegli la località (digita il nome esatto): ");
        String placeName = readLineFromUser();

        Place selectedPlace = availablePlaces.stream()
                .filter(p -> p.getName().equalsIgnoreCase(placeName))
                .findFirst().orElse(null);

        if (selectedPlace == null) {
            System.err.println("Nome località non trovato o non valido. Riprova.");
            return getOvernightData(availablePlaces);
        }
        System.out.print("Data di inizio pernottamento (gg/mm/aaaa): ");
        LocalDate startDate = parseDateFromUser();
        System.out.print("Data di fine pernottamento (gg/mm/aaaa): ");
        LocalDate endDate = parseDateFromUser();
        if (endDate.isBefore(startDate)) {
            System.err.println("Errore: La data di fine non può precedere l'inizio.");
            return getOvernightData(availablePlaces);
        }
        return new OvernightStay(0, Date.valueOf(startDate), Date.valueOf(endDate), selectedPlace, null);
    }

    public Place getPlaceValues() throws IOException {
        System.out.print("Nome della località: ");
        String name = readLineFromUser();
        System.out.print("Paese della località: ");
        String country = readLineFromUser();
        return new Place(name, country);
    }

    public Hotel getHotelValues(List<Place> availablePlaces) throws IOException {
        System.out.println("\n--- Inserimento Nuovo Albergo ---");
        System.out.print("Nome albergo: "); String name = readLineFromUser();
        System.out.print("Nome referente: "); String referee = readLineFromUser();
        System.out.print("Capienza: "); int capacity = parseIntFromUser();
        System.out.print("Via: "); String street = readLineFromUser();
        System.out.print("Civico: "); String civic = readLineFromUser();
        System.out.print("CAP: "); String cp = readLineFromUser();
        System.out.print("Email: "); String email = readLineFromUser();
        System.out.print("Telefono: "); String telephone = readLineFromUser();
        System.out.print("Fax (opzionale): "); String fax = readLineFromUser();
        System.out.print("Costo per notte a persona (€): "); double cost = parseDoubleFromUser();
        if (availablePlaces == null || availablePlaces.isEmpty()) {
            System.out.println("Nessuna località disponibile a cui associare l'albergo.");
            return null;
        }
        System.out.println("Località disponibili:");
        availablePlaces.forEach(p -> System.out.println("- " + p.getName()));

        System.out.print("Scegli la località per il nuovo albergo (digita il nome esatto): ");
        String placeName = readLineFromUser();

        Place selectedPlace = availablePlaces.stream()
                .filter(p -> p.getName().equalsIgnoreCase(placeName))
                .findFirst().orElse(null);

        if (selectedPlace == null) {
            System.err.println("Nome località non trovato o non valido. Riprova.");
            return null;
        }
        return new Hotel(0, name, referee, capacity, street, civic, cp, email, telephone, fax, selectedPlace, (float)cost);
    }

    public int getTripId() throws IOException {
        System.out.print("Inserire l'ID del viaggio: ");
        return parseIntFromUser();
    }

    public int getOvernightStayId() throws IOException {
        System.out.print("Inserire l'ID del pernottamento: ");
        return parseIntFromUser();
    }

    public int getHotelCode() throws IOException {
        System.out.print("Inserire il codice dell'albergo: ");
        return parseIntFromUser();
    }

    public List<String> getBusPlates() throws IOException {
        List<String> plates = new ArrayList<>();
        String input;
        System.out.println("Inserire le targhe degli autobus (inserire 'q' per terminare):");
        while (!(input = readLineFromUser()).equalsIgnoreCase("q")) {
            if (!input.trim().isEmpty()) plates.add(input);
        }
        return plates;
    }

    public int getParticipants() throws IOException {
        System.out.print("Inserire il numero di persone per la prenotazione: ");
        return parseIntFromUser();
    }

    public int getBookingCode() throws IOException {
        System.out.print("Inserire il codice della prenotazione: ");
        return parseIntFromUser();
    }

    public String getClientEmail() throws IOException {
        System.out.print("Inserire l'email del cliente: ");
        return readLineFromUser();
    }

    /**
     * NUOVO METODO: Chiede i dati per un nuovo itinerario-modello.
     * @return Un oggetto Itinerario con i dati inseriti.
     * @throws IOException
     */
    public Itinerario getItinerarioValues() throws IOException {
        System.out.println("\n--- Creazione Nuovo Itinerario-Modello ---");
        System.out.print("Inserisci il titolo dell'itinerario (es. Tour della Toscana): ");
        String titolo = readLineFromUser();
        System.out.print("Inserisci il costo base per persona (€): ");
        double costo = parseDoubleFromUser();
        return new Itinerario(0, titolo, (float) costo);
    }
}