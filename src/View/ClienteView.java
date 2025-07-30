package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClienteView extends View {
    private final BufferedReader reader;

    public ClienteView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public int showMenu() throws IOException {
        System.out.println("\n***********************************");
        System.out.println("* Agenzia di viaggi - Menù Cliente *");
        System.out.println("***********************************");
        System.out.println("\n1) Visualizza tutti i viaggi disponibili");
        System.out.println("2) Prenota un viaggio");
        System.out.println("3) Cancella una prenotazione");
        System.out.println("4) Esci");
        System.out.print("Inserire il numero dell'opzione: ");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            return -1; // Ritorna un valore non valido se l'input non è un numero
        }
    }

    public int getTripIdToBook() throws IOException {
        System.out.print("Inserisci l'ID del viaggio che vuoi prenotare: ");
        return Integer.parseInt(reader.readLine());
    }

    public int getNumberOfParticipants() throws IOException {
        System.out.print("Per quante persone vuoi prenotare? ");
        return Integer.parseInt(reader.readLine());
    }

    public int getBookingCodeToCancel() throws IOException {
        System.out.print("Inserisci il codice della prenotazione da cancellare (o 0 per annullare): ");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            return -1; // Ritorna un valore non valido
        }
    }

}