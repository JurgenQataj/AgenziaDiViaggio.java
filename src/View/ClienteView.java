package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// NOTA: Non usiamo più Scanner, quindi l'import è stato rimosso.

public class ClienteView extends View {

    // Sostituiamo Scanner con BufferedReader per una lettura più stabile.
    private final BufferedReader reader;

    public ClienteView() {
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

    // --- METODI PUBBLICI CORRETTI ---

    public int showMenu() throws IOException {
        System.out.println("***********************************");
        System.out.println("* Agenzia di viaggi - Menù    *");
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
            choice = parseIntFromUser(); // Usa il metodo helper robusto
        } while (choice < 1 || choice > 5);
        return choice;
    }

    public Date getDate() throws IOException {
        Date sqlDate = null;
        while (sqlDate == null) {
            System.out.print("Inserire una data (aaaa/mm/gg): ");
            String dateString = readLineFromUser();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                format.setLenient(false);
                java.util.Date parsedDate = format.parse(dateString);
                sqlDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                System.err.println("Formato della data non valido. Riprovare.");
            }
        }
        return sqlDate;
    }

    public String getTripTitle() throws IOException {
        System.out.print("Inserire il titolo del viaggio: ");
        return readLineFromUser();
    }

    public int getParticipants() throws IOException {
        int n;
        do {
            System.out.print("Inserire il numero persone per la prenotazione: ");
            n = parseIntFromUser(); // Usa il metodo helper robusto
        } while (n < 1);
        return n;
    }

    public int getBookingCode() throws IOException {
        System.out.print("Inserire il codice della prenotazione: ");
        return parseIntFromUser(); // Usa il metodo helper robusto
    }
}