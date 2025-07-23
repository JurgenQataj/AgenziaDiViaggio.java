package model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rappresenta un itinerario-modello, che funge da template per i viaggi.
 * È definito da un titolo, un costo per persona e una sequenza ordinata di tappe.
 * La sua durata totale è data dalla somma dei giorni di tutte le sue tappe.
 */
public class Itinerario {

    private int id;
    private String titolo;
    private double costoPersona;
    private List<Tappa> tappe; // Campo aggiunto per contenere le tappe

    /**
     * Costruttore usato per creare una nuova istanza di Itinerario dalla View,
     * prima che venga salvato nel database (e quindi prima che abbia un ID).
     *
     * @param titolo Il titolo descrittivo dell'itinerario.
     * @param costoPersona Il costo base del viaggio per una singola persona.
     * @param tappe La lista di oggetti Tappa che compongono l'itinerario.
     */
    public Itinerario(String titolo, double costoPersona, List<Tappa> tappe) {
        this.titolo = titolo;
        this.costoPersona = costoPersona;
        this.tappe = tappe;
    }

    /**
     * Costruttore usato per istanziare un oggetto Itinerario con i dati
     * recuperati dal database, incluso il suo ID.
     *
     * @param id L'ID univoco dell'itinerario.
     * @param titolo Il titolo dell'itinerario.
     * @param costoPersona Il costo per persona.
     */
    public Itinerario(int id, String titolo, double costoPersona) {
        this.id = id;
        this.titolo = titolo;
        this.costoPersona = costoPersona;
    }


    // --- GETTERS E SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public double getCostoPersona() {
        return costoPersona;
    }

    public void setCostoPersona(double costoPersona) {
        this.costoPersona = costoPersona;
    }

    public List<Tappa> getTappe() {
        return tappe;
    }

    public void setTappe(List<Tappa> tappe) {
        this.tappe = tappe;
    }

    /**
     * Calcola e restituisce la durata totale dell'itinerario in giorni.
     * @return La somma dei giorni di tutte le tappe.
     */
    public int getDurataTotale() {
        if (tappe == null || tappe.isEmpty()) {
            return 0;
        }
        return tappe.stream().mapToInt(Tappa::getGiorni).sum();
    }

    @Override
    public String toString() {
        return String.format("Itinerario #%d: %s, Costo: %.2f €", id, titolo, costoPersona);
    }
}