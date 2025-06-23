package view;

import model.Hotel;
import model.OvernightStay;
import model.Place;
import model.Trip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    // --- METODI HELPER PRIVATI E ROBUSTI ---

    private String readLineFromUser() throws IOException {
        String line;
        while ((line = reader.readLine()) != null && line.trim().isEmpty()) {
            // Ignora le righe vuote e continua a leggere
        }
        return line;
    }

    private LocalDate parseDateFromUser() throws IOException {
        while (true) {
            String dateString = readLineFromUser();
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                format.setLenient(false);
                java.util.Date parsedDate = format.parse(dateString);
                return parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ParseException e) {
                System.err.print("Formato data non valido. Riprova (gg/mm/aaaa): ");
            }
        }
    }

    private int parseIntFromUser() throws IOException {
        while (true) {
            String input = readLineFromUser();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.print("Input non valido. Inserire un numero intero: ");
            }
        }
    }

    private double parseDoubleFromUser() throws IOException {
        while (true) {
            String input = readLineFromUser();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.print("Input non valido. Inserire un numero: ");
            }
        }
    }

    public int showMenu() throws IOException {
        System.out.println("***********************************");
        System.out.println("* Agenzia di viaggi - Menù    *");
        System.out.println("***********************************\n");
        System.out.println("*** Selezionare un'operazione ***\n");
        System.out.println("1)  Crea un nuovo viaggio");
        System.out.println("2)  Inserisci una nuova località");
        System.out.println("3)  Inserisci un nuovo albergo");
        System.out.println("4)  Elenca tutti i viaggi");
        System.out.println("5)  Elenca i dettagli di un viaggio");
        System.out.println("6)  Elenca tutte le località");
        System.out.println("7)  Elenca tutti gli alberghi");
        System.out.println("8)  Genera i report disponibili");
        System.out.println("9)  Assegna degli autobus ad un viaggio");
        System.out.println("10) Scegli un albergo per un pernottamento");
        System.out.println("11) Esegui una prenotazione");
        System.out.println("12) Cancella una prenotazione");
        System.out.println("13) Esci");

        int choice;
        do {
            System.out.print("Inserire il numero dell'opzione: ");
            choice = parseIntFromUser();
        } while (choice < 1 || choice > 13);
        return choice;
    }

    public Trip getTripValues() throws IOException {
        System.out.print("Inserire il titolo del viaggio: ");
        String title = readLineFromUser();
        System.out.print("Inserire la data di partenza (gg/mm/aaaa): ");
        LocalDate startDate = parseDateFromUser();
        System.out.print("Inserire la data del rientro (gg/mm/aaaa): ");
        LocalDate returnDate = parseDateFromUser();
        System.out.print("Inserire il costo (€): ");
        double price = parseDoubleFromUser();
        return new Trip(title, startDate, returnDate, price);
    }

    public int getOvernightsNumber() throws IOException {
        int n;
        do {
            System.out.print("Inserire il numero di pernottamenti: ");
            n = parseIntFromUser();
        } while (n < 0);
        return n;
    }

    public OvernightStay getOvernightData() throws IOException {

        System.out.print("Inserire la data di inizio pernottamento (gg/mm/aaaa): ");
        LocalDate startDate = parseDateFromUser();
        System.out.print("Inserire la data di fine pernottamento (gg/mm/aaaa): ");
        LocalDate endDate = parseDateFromUser();
        if (endDate.isBefore(startDate)) {
            System.err.println("Errore: La data di fine non può precedere la data di inizio. Reinserire i dati del pernottamento.");
            return getOvernightData();
        }
        return new OvernightStay(startDate, endDate);
    }

    public Place getPlaceValues() throws IOException {
        System.out.print("Inserire il nome della località: ");
        String name = readLineFromUser();
        System.out.print("Inserire il paese della località: ");
        String country = readLineFromUser();
        return new Place(name, country);
    }

    public Hotel getHotelValues() throws IOException {
        System.out.print("Inserire il nome dell'albergo: ");
        String name = readLineFromUser();
        System.out.print("Inserire il nome del referente: ");
        String referee = readLineFromUser();
        System.out.print("Inserire la capienza dell'albergo: ");
        int capacity = parseIntFromUser();
        System.out.print("Inserire la via dell'albergo: ");
        String street = readLineFromUser();
        System.out.print("Inserire il civico dell'albergo: ");
        String civic = readLineFromUser();
        System.out.print("Inserire il codice postale dell'albergo: ");
        String cp = readLineFromUser();
        System.out.print("Inserire l'email dell'albergo: ");
        String email = readLineFromUser();
        System.out.print("Inserire il numero di telefono dell'albergo: ");
        String telephone = readLineFromUser();
        System.out.print("Inserire il fax dell'albergo (opzionale): ");
        String fax = readLineFromUser();
        return new Hotel(name, referee, capacity, street, civic, cp, email, telephone, fax);
    }

    public LocalDate askForDate(String message) throws IOException {
        System.out.print(message);
        return parseDateFromUser();
    }

    public String getPlaceName() throws IOException {
        System.out.print("Inserire il nome della località: ");
        return readLineFromUser();
    }

    public String getTripTitle() throws IOException {
        System.out.print("Inserire il titolo del viaggio: ");
        return readLineFromUser();
    }

    public List<String> getBusPlates() throws IOException {
        List<String> plates = new ArrayList<>();
        String input;
        System.out.println("Inserire le targhe degli autobus (inserire 'q' per terminare):");
        while (!(input = readLineFromUser()).equals("q")) {
            if (!input.trim().isEmpty()) plates.add(input);
        }
        return plates;
    }

    public int getHotelCode() throws IOException {
        System.out.print("Inserire il codice dell'albergo scelto: ");
        return parseIntFromUser();
    }

    public int getParticipants() throws IOException {
        System.out.print("Inserire il numero di persone per la prenotazione: ");
        return parseIntFromUser();
    }

    public int getBookingCode() throws IOException {
        System.out.print("Inserire il codice della prenotazione: ");
        return parseIntFromUser();
    }
}