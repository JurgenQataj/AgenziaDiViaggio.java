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

public class SegreteriaView extends View {
    private final BufferedReader reader;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

    public Itinerario getNewItinerarioDetails(List<Place> luoghiDisponibili) throws IOException {
        System.out.println("\n--- Creazione Nuovo Itinerario-Modello ---");
        System.out.print("Inserisci il titolo del nuovo itinerario: ");
        String titolo = reader.readLine();
        System.out.print("Inserisci il costo per persona: ");
        double costo = Double.parseDouble(reader.readLine());

        List<Tappa> tappe = new ArrayList<>();
        int giorniTotali = 0;

        System.out.println("\n--- Aggiunta Tappe ---");
        System.out.println("Località disponibili:");
        luoghiDisponibili.forEach(p -> System.out.println("- " + p.getNome()));

        while (true) {
            System.out.print("\nInserisci la località per la prossima tappa (o 'fine' per terminare): ");
            String nomeLocalita = reader.readLine();
            if (nomeLocalita.equalsIgnoreCase("fine")) {
                if(tappe.isEmpty()){
                    showMessage("ERRORE: Un itinerario deve avere almeno una tappa.");
                    continue;
                }
                break;
            }

            boolean localitaValida = luoghiDisponibili.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(nomeLocalita));
            if (!localitaValida) {
                System.out.println("ERRORE: Località non valida. Riprova.");
                continue;
            }

            System.out.print("Inserisci il numero di notti per la tappa a " + nomeLocalita + ": ");
            try {
                int notti = Integer.parseInt(reader.readLine());
                if (notti <= 0) {
                    System.out.println("ERRORE: Il numero di notti deve essere positivo.");
                    continue;
                }
                if (giorniTotali + notti > 7) {
                    System.out.println("ERRORE: La durata totale dell'itinerario non può superare i 7 giorni.");
                    continue;
                }

                tappe.add(new Tappa(nomeLocalita, notti));
                giorniTotali += notti;
                System.out.println("Tappa aggiunta! Durata totale attuale: " + giorniTotali + " giorni.");

            } catch (NumberFormatException e) {
                System.out.println("ERRORE: Inserisci un numero valido.");
            }
        }

        return new Itinerario(titolo, costo, tappe);
    }
    public Trip getNewTripDetails(List<Itinerario> itinerariDisponibili) throws IOException, ParseException {
        System.out.println("\n--- Creazione Nuovo Viaggio da Itinerario ---");
        if (itinerariDisponibili == null || itinerariDisponibili.isEmpty()) {
            showMessage("Nessun itinerario-modello disponibile per creare un viaggio.");
            return null;
        }

        System.out.println("Itinerari-modello disponibili:");
        printObjects(itinerariDisponibili);

        System.out.print("Scegli l'ID dell'itinerario da usare: ");
        int itinerarioId = Integer.parseInt(reader.readLine());

        Itinerario itinerarioScelto = itinerariDisponibili.stream()
                .filter(i -> i.getId() == itinerarioId)
                .findFirst()
                .orElse(null);

        if (itinerarioScelto == null) {
            showMessage("ERRORE: ID Itinerario non valido.");
            return null;
        }

        System.out.print("Inserisci la data di partenza (YYYY-MM-DD): ");
        Date dataPartenza = sdf.parse(reader.readLine());

        return new Trip(0, dataPartenza, null, itinerarioScelto);
    }

    public String getInput(String message) throws IOException {
        System.out.print(message);
        return reader.readLine();
    }

    public int getTripId() throws IOException {
        System.out.print("Inserisci l'ID del viaggio: ");
        return Integer.parseInt(reader.readLine());
    }

    public int getBookingCodeToCancel() throws IOException {
        System.out.print("Inserisci il codice della prenotazione da cancellare (o 0 per annullare): ");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getClientEmail() throws IOException {
        System.out.print("Inserisci l'email del cliente: ");
        return reader.readLine();
    }

    public int getParticipants() throws IOException {
        System.out.print("Inserisci il numero di partecipanti: ");
        return Integer.parseInt(reader.readLine());
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

        String nomeLocalita;
        Optional<Place> selectedPlace;

        while (true) {
            System.out.print("In quale località si trova l'albergo? ");
            nomeLocalita = reader.readLine();
            String finalNomeLocalita = nomeLocalita;
            selectedPlace = places.stream()
                    .filter(p -> p.getNome().equalsIgnoreCase(finalNomeLocalita))
                    .findFirst();
            if (selectedPlace.isPresent()) {
                break;
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

        return new Hotel(0, nome, referente, capienza, via, civico, cp, email, telefono, fax, selectedPlace.get(), costoNotte);
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