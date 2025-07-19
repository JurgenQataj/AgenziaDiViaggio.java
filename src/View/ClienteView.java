package View;

import exceptions.DatabaseException; // <-- AGGIUNGI QUESTA RIGA
import model.Trip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ClienteView extends View {

    private final BufferedReader reader;

    public ClienteView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private int parseIntFromUser() throws IOException {
        while (true) {
            String input = reader.readLine();
            if (input == null || input.trim().isEmpty()) continue;
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.err.print("Input non valido. Inserire un numero intero: ");
            }
        }
    }

    public int showMenu() throws IOException {
        System.out.println("\n***********************************");
        System.out.println("* Agenzia di viaggi - Menù Cliente *");
        System.out.println("***********************************\n");
        System.out.println("1) Visualizza tutti i viaggi disponibili");
        System.out.println("2) Prenota un viaggio");
        System.out.println("3) Cancella una prenotazione");
        System.out.println("4) Esci");

        int choice;
        do {
            System.out.print("Inserire il numero dell'opzione: ");
            choice = parseIntFromUser();
        } while (choice < 1 || choice > 4);
        return choice;
    }

    /**
     * Questo è il metodo che mancava e che causava l'errore.
     * @param e L'eccezione del database da stampare.
     */
    // Versione corretta e più flessibile
    public void printError(Exception e) { // <-- Adesso accetta qualsiasi tipo di eccezione
        System.out.println("ERRORE: " + e.getMessage());
    }

    public int getTripId() throws IOException {
        System.out.print("Inserire l'ID del viaggio che vuoi prenotare: ");
        return parseIntFromUser();
    }

    public int getParticipants() throws IOException {
        System.out.print("Inserire il numero di persone per la prenotazione: ");
        return parseIntFromUser();
    }

    public int getBookingCode() throws IOException {
        System.out.print("Inserire il codice della prenotazione da cancellare: ");
        return parseIntFromUser();
    }
}