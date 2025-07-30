package model;

/**
 * Rappresenta una singola tappa di un itinerario in modo immutabile.
 * Una volta creata, i suoi dati non possono essere cambiati.
 */
public class Tappa {

    private final String localita;
    private final int giorni;

    public Tappa(String localita, int giorni) {
        this.localita = localita;
        this.giorni = giorni;
    }

    public String getLocalita() {
        return localita;
    }

    public int getGiorni() {
        return giorni;
    }

    @Override
    public String toString() {

        return "Tappa a " + getLocalita() + " per " + getGiorni() + " giorni";
    }
}