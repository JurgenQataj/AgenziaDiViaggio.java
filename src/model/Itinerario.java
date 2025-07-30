package model;

import java.util.List;

public class Itinerario {

    private final int id;
    private final String titolo;
    private final double costoPersona;
    private final List<Tappa> tappe;

    public Itinerario(String titolo, double costoPersona, List<Tappa> tappe) {
        this.id = 0; // Un nuovo itinerario non ha ancora un ID dal database
        this.titolo = titolo;
        this.costoPersona = costoPersona;
        this.tappe = tappe;
    }

    public Itinerario(int id, String titolo, double costoPersona) {
        this.id = id;
        this.titolo = titolo;
        this.costoPersona = costoPersona;
        this.tappe = null; // Le tappe non in questo costruttore
    }

    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public double getCostoPersona() {
        return costoPersona;
    }

    public List<Tappa> getTappe() {
        return tappe;
    }

    public int getDurataTotale() {
        if (tappe == null || tappe.isEmpty()) {
            return 0;
        }
        return tappe.stream().mapToInt(Tappa::getGiorni).sum();
    }
    @Override
    public String toString() {
        // Ora il toString usa anche getDurataTotale(), risolvendo l'avviso
        // e fornendo un output più completo.
        int durata = getDurataTotale();
        String durataStr = durata > 0 ? ", Durata: " + durata + " giorni" : "";
        return String.format("Itinerario #%d: %s, Costo: %.2f €%s", id, titolo, costoPersona, durataStr);
    }
}