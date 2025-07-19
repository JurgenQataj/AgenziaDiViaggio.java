package model;

// Questa classe rappresenta una singola prenotazione
public class Booking {
    private final int codice;
    private final int numeroPersone;
    private final String clienteEmail;
    private final int viaggioId;

    /**
     * COSTRUTTORE CORRETTO:
     * Accetta i parametri che corrispondono alle colonne della tabella 'prenotazione'.
     * @param codice L'ID univoco della prenotazione.
     * @param numeroPersone Il numero di partecipanti per questa prenotazione.
     * @param clienteEmail L'email del cliente che ha prenotato.
     * @param viaggioId L'ID del viaggio a cui si riferisce la prenotazione.
     */
    public Booking(int codice, int numeroPersone, String clienteEmail, int viaggioId) {
        this.codice = codice;
        this.numeroPersone = numeroPersone;
        this.clienteEmail = clienteEmail;
        this.viaggioId = viaggioId;
    }

    // Metodi Getter per accedere ai dati
    public int getCodice() {
        return codice;
    }

    public int getNumeroPersone() {
        return numeroPersone;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public int getViaggioId() {
        return viaggioId;
    }

    @Override
    public String toString() {
        // Un formato di stampa chiaro e utile per l'utente
        return String.format("Prenotazione #%d | Viaggio ID: %d | Persone: %d",
                codice, viaggioId, numeroPersone);
    }
}