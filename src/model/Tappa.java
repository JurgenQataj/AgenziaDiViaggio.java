package model;

/**
 * Rappresenta una singola tappa di un itinerario, caratterizzata da una località
 * e dalla durata del soggiorno in giorni (notti).
 */
public class Tappa {
    private int id;
    private int itinerarioId;
    private String localita;
    private int giorni;
    private int ordineTappa;

    /**
     * Costruttore per creare una nuova istanza di Tappa.
     * Usato principalmente quando si costruisce un nuovo itinerario dalla View.
     * @param localita Il nome della località della tappa.
     * @param giorni Il numero di notti da trascorrere nella località.
     */
    public Tappa(String localita, int giorni) {
        this.localita = localita;
        this.giorni = giorni;
    }

    // --- GETTERS E SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItinerarioId() {
        return itinerarioId;
    }

    public void setItinerarioId(int itinerarioId) {
        this.itinerarioId = itinerarioId;
    }

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public int getGiorni() {
        return giorni;
    }

    public void setGiorni(int giorni) {
        this.giorni = giorni;
    }


    public int getOrdineTappa() {
        return ordineTappa;
    }

    public void setOrdineTappa(int ordineTappa) {
        this.ordineTappa = ordineTappa;
    }
}