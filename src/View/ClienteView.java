package view;

import java.sql.Date;
import java.util.Scanner;

public class ClienteView extends View {

    private final Scanner scanner;

    public ClienteView() {
        this.scanner = new Scanner(System.in).useDelimiter("\n");
    }

    public int showMenu() {

        System.out.println("***********************************");
        System.out.println("*     Agenzia di viaggi - Menù    *");
        System.out.println("***********************************\n");
        System.out.println("*** Selezionare un'operazione ***\n");
        System.out.println("1) Elenca tutti i viaggi");
        System.out.println("2) Elenca i dettagli di un viaggio");
        System.out.println("3) Prenota un viaggio");
        System.out.println("4) Cancella una prenotazione");
        System.out.println("5) Esci");

        int choice;
        do {
            System.out.print("Inserire il numero dell'opzione: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 5);
        return choice;
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

    public Date getDate() {
        System.out.print("Inserire una data (aaaa/mm/gg): ");
        String dateString = scanner.next();
        return Date.valueOf(dateString);
    }

    public String getTripTitle() {
        System.out.print("Inserire il titolo del viaggio: ");
        return scanner.next();
    }
}