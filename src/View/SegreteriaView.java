package view;

import model.Hotel;
import model.OvernightStay;
import model.Place;
import model.Trip;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SegreteriaView extends View {

    private final Scanner scanner;

    public SegreteriaView() {
        this.scanner = new Scanner(System.in).useDelimiter("\n");
    }

    public int showMenu() {
        System.out.println("***********************************");
        System.out.println("*     Agenzia di viaggi - Menù    *");
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
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 13);
        return choice;
    }

    public Trip getTripValues() {
        scanner.nextLine(); // Consuma il newline rimasto
        System.out.print("Inserire il titolo del viaggio: ");
        String title = scanner.nextLine();
        System.out.print("Inserire la data di partenza (gg/mm/aaaa): ");
        String startDateString = scanner.next();
        LocalDate startDate = parseStringDate(startDateString);
        System.out.print("Inserire la data del rientro (gg/mm/aaaa): ");
        String returnDateString = scanner.next();
        LocalDate returnDate = parseStringDate(returnDateString);
        System.out.print("Inserire il costo (€): ");
        Double price = scanner.nextDouble();

        return new Trip(title, startDate, returnDate, price);
    }

    public int getOvernightsNumber() {
        int n;
        do {
            System.out.print("Inserire il numero di pernottamenti: ");
            n = scanner.nextInt();
            scanner.nextLine(); // Consuma il resto della riga (il carattere '\n')
        } while (n < 0);
        return n;
    }

    public OvernightStay getOvernightData() {
        System.out.print("Inserire la data di inizio pernottamento (gg/mm/aaaa): ");
        LocalDate startDate = parseStringDate(scanner.nextLine());
        System.out.print("Inserire la data di fine pernottamento (gg/mm/aaaa): ");
        LocalDate endDate = parseStringDate(scanner.nextLine());
        return new OvernightStay(startDate, endDate);
    }

    public Hotel getHotelValues() {
        System.out.print("Inserire il nome dell'albergo: ");
        String name = scanner.nextLine();
        System.out.print("Inserire il nome del referente: ");
        String referee = scanner.nextLine();
        System.out.print("Inserire la capienza dell'albergo: ");
        int capacity = scanner.nextInt();
        System.out.print("Inserire la via dell'albergo: ");
        String street = scanner.nextLine();
        System.out.print("Inserire il civico dell'albergo: ");
        String civic = scanner.nextLine();
        System.out.print("Inserire il codice postale dell'albergo: ");
        String cp = scanner.nextLine();
        System.out.print("Inserire l'email dell'albergo: ");
        String email = scanner.nextLine();
        System.out.print("Inserire il numero di telefono dell'albergo: ");
        String telephone = scanner.nextLine();
        System.out.print("Inserire il fax dell'albergo (opzionale): ");
        String fax = scanner.nextLine();

        return new Hotel(name, referee, capacity, street, civic, cp, email, telephone, fax);
    }

    public String getPlaceName() {
        System.out.print("Inserire il nome della località: ");
        return scanner.nextLine();
    }

    public Place getPlaceValues() {
        System.out.print("Inserire il nome della località: ");
        String name = scanner.nextLine();
        System.out.print("Inserire il paese della località: ");
        String country = scanner.nextLine();
        return new Place(name, country);
    }

    public Date getDate() {
        /*System.out.print("Inserire una data (gg/mm/aaaa): ");
        String stringDate = scanner.next("^[0-9]{2}/[0-9]{2}/[0-9]{4}$");*/
        // return Date.valueOf(parseStringDate(stringDate));
        // Modifica:
        System.out.print("Inserire una data (aaaa/mm/gg): ");
        String stringDate = scanner.next();
        return Date.valueOf(stringDate);
    }

    public String getTripTitle() {
        System.out.print("Inserire il titolo del viaggio: ");
        return scanner.next();
    }

    public List<String> getBusPlates() throws IOException {
        List<String> plates = new ArrayList<>();
        String input;
        do {
            System.out.print("Inserire la targa di un autobus (q per uscire): ");
            input = scanner.next();
            if (!input.equals("q"))
                plates.add(input);
        } while (!input.equals("q"));
        return plates;
    }

    public int getHotelCode() throws IOException {
        int input;
        do {
            System.out.print("Inserire il codice del'albergo scelto': ");
            input = scanner.nextInt();
        } while (input < 0);
        return input;
    }

    public int getParticipants() {
        int n;
        do {
            System.out.print("Inserire il numero persone per la prenotazione: ");
            n = scanner.nextInt();
        } while (n < 1);
        return n;
    }

    public int getBookingCode() {
        System.out.print("Inserire il codice della prenotazione ");
        return scanner.nextInt();
    }
}
