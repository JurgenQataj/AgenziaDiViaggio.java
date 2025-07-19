package View;

import model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SegreteriaView {
    private final BufferedReader reader;

    public SegreteriaView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public int showMenu() throws IOException {
        System.out.println("\n--- MENU SEGRETERIA ---");
        System.out.println("1) Crea un nuovo viaggio da un itinerario");
        System.out.println("2) Crea un nuovo itinerario-modello");
        System.out.println("3) Inserisci una nuova località");
        System.out.println("4) Inserisci un nuovo albergo");
        System.out.println("5) Inserisci un nuovo autobus");
        System.out.println("6) Elenca tutti i viaggi futuri");
        System.out.println("7) Elenca i dettagli di un viaggio");
        System.out.println("8) Elenca tutte le località");
        System.out.println("9) Elenca tutti gli alberghi");
        System.out.println("10) Genera report guadagni/perdite di un viaggio");
        System.out.println("11) Assegna autobus ad un viaggio");
        System.out.println("12) Assegna albergo a un pernottamento");
        System.out.println("13) Esegui una prenotazione per un cliente");
        System.out.println("14) Cancella una prenotazione");
        System.out.println("15) Esci");
        System.out.print("Scegli un'opzione: ");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Mostra un messaggio e legge una linea di input dall'utente.
     * @param message Il messaggio da mostrare.
     * @return La stringa inserita.
     * @throws IOException
     */
    public String getInput(String message) throws IOException {
        System.out.print(message);
        return reader.readLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void printError(Exception e) {
        System.out.println("ERRORE: " + e.getMessage());
    }

    public <T> void printObjects(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            System.out.println("Nessun risultato da mostrare.");
            return;
        }
        for (T obj : objects) {
            System.out.println(obj.toString());
        }
    }

    public int getTripId() throws IOException {
        System.out.print("Inserisci l'ID del viaggio: ");
        return Integer.parseInt(reader.readLine());
    }

    public int getBookingCode() throws IOException {
        System.out.print("Inserisci il codice della prenotazione: ");
        return Integer.parseInt(reader.readLine());
    }

    public String getClientEmail() throws IOException {
        System.out.print("Inserisci l'email del cliente: ");
        return reader.readLine();
    }

    public int getParticipants() throws IOException {
        System.out.print("Inserisci il numero di partecipanti: ");
        return Integer.parseInt(reader.readLine());
    }

    public int askForItinerario(List<Itinerario> itinerari) throws IOException {
        System.out.println("Itinerari-modello disponibili:");
        printObjects(itinerari);
        System.out.print("Scegli l'ID dell'itinerario da usare (o 0 per annullare): ");
        return Integer.parseInt(reader.readLine());
    }

    public Date[] getTripDates() throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.print("Inserisci la data di partenza (YYYY-MM-DD): ");
        Date startDate = sdf.parse(reader.readLine());
        System.out.print("Inserisci la data di rientro (YYYY-MM-DD): ");
        Date endDate = sdf.parse(reader.readLine());
        return new Date[]{startDate, endDate};
    }

    public int getOvernightsNumber() throws IOException {
        System.out.print("Quante tappe (pernottamenti) vuoi aggiungere? ");
        return Integer.parseInt(reader.readLine());
    }

    public OvernightStay getOvernightData(List<Place> availablePlaces) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("\n-- Aggiunta Nuova Tappa --");
        System.out.println("Località disponibili:");
        availablePlaces.forEach(p -> System.out.println("- " + p.getNome()));
        System.out.print("Inserisci il nome della località per questa tappa: ");
        String nomeLocalita = reader.readLine();
        System.out.print("Data inizio pernottamento (YYYY-MM-DD): ");
        Date dataInizioP = sdf.parse(reader.readLine());
        System.out.print("Data fine pernottamento (YYYY-MM-DD): ");
        Date dataFineP = sdf.parse(reader.readLine());
        return new OvernightStay(dataInizioP, dataFineP, nomeLocalita);
    }

    public Itinerario getItinerarioValues() throws IOException {
        System.out.print("Inserisci il titolo del nuovo itinerario: ");
        String titolo = reader.readLine();
        System.out.print("Inserisci il costo per persona: ");
        double costo = Double.parseDouble(reader.readLine());
        return new Itinerario(0, titolo, costo);
    }

    public Place getPlaceValues() throws IOException {
        System.out.print("Nome della località: ");
        String nome = reader.readLine();
        System.out.print("Paese di appartenenza: ");
        String paese = reader.readLine();
        return new Place(nome, paese);
    }

    public Autobus getAutobusValues() throws IOException {
        System.out.println("\n--- Inserimento Nuovo Autobus ---");
        System.out.print("Inserisci la targa (es. AA123BB): ");
        String targa = reader.readLine();
        System.out.print("Inserisci la capienza: ");
        int capienza = Integer.parseInt(reader.readLine());
        System.out.print("Inserisci il costo giornaliero: ");
        double costoGiornaliero = Double.parseDouble(reader.readLine());
        return new Autobus(targa, capienza, costoGiornaliero);
    }

    public Hotel getHotelValues(List<Place> places) throws IOException {
        System.out.println("\n--- Inserimento Nuovo Albergo ---");
        System.out.println("Località esistenti:");
        places.forEach(p -> System.out.println("- " + p.getNome()));

        Place selectedPlace = null;
        while (selectedPlace == null) {
            System.out.print("In quale località si trova l'albergo? ");
            String nomeLocalita = reader.readLine();

            Optional<Place> foundPlace = places.stream()
                    .filter(p -> p.getNome().equalsIgnoreCase(nomeLocalita))
                    .findFirst();
            if (foundPlace.isPresent()) {
                selectedPlace = foundPlace.get();
            } else {
                System.out.println("ERRORE: Località non trovata. Riprova.");
            }
        }

        System.out.print("Nome albergo: "); String nome = reader.readLine();
        System.out.print("Referente: "); String referente = reader.readLine();
        System.out.print("Capienza: "); int capienza = Integer.parseInt(reader.readLine());
        System.out.print("Via: "); String via = reader.readLine();
        System.out.print("Civico: "); String civico = reader.readLine();
        System.out.print("CAP: "); String cp = reader.readLine();
        System.out.print("Email: "); String email = reader.readLine();
        System.out.print("Telefono: "); String telefono = reader.readLine();
        System.out.print("Fax (opzionale): "); String fax = reader.readLine();
        System.out.print("Costo per notte a persona: "); double costoNotte = Double.parseDouble(reader.readLine());

        return new Hotel(0, nome, referente, capienza, via, civico, cp, email, telefono, fax, selectedPlace, costoNotte);
    }

    public List<String> getBusPlates() throws IOException {
        System.out.print("Inserisci le targhe degli autobus da assegnare (separate da una virgola): ");
        String[] plates = reader.readLine().split(",");
        List<String> plateList = new ArrayList<>();
        for (String p : plates) { plateList.add(p.trim()); }
        return plateList;
    }

    public int getOvernightStayId() throws IOException {
        System.out.print("Inserisci l'ID del pernottamento a cui assegnare l'albergo: ");
        return Integer.parseInt(reader.readLine());
    }

    public int getHotelCode() throws IOException {
        System.out.print("Inserisci il codice dell'albergo da assegnare: ");
        return Integer.parseInt(reader.readLine());
    }
}