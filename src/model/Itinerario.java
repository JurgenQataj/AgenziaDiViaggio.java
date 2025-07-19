package model;

public class Itinerario {
    private final int id;
    private final String titolo;
    private final double costoPersona; // Il tipo è double per la precisione

    public Itinerario(int id, String titolo, double costoPersona) {
        this.id = id;
        this.titolo = titolo;
        this.costoPersona = costoPersona;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce il costo per persona.
     * @return un valore double.
     */
    public double getCostoPersona() {
        return costoPersona;
    }

    @Override
    public String toString() {
        return String.format("Itinerario #%d: %s, Costo: %.2f €", id, titolo, costoPersona);
    }
}