package model;

public class Itinerario {
    private int id;
    private String titolo;
    private float costoPersona;

    public Itinerario(int id, String titolo, float costoPersona) {
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

    public float getCostoPersona() {
        return costoPersona;
    }

    @Override
    public String toString() {
        return "Itinerario #" + id +
                " - Titolo: '" + titolo + '\'' +
                ", Costo per Persona: " + String.format("%.2f", costoPersona) + "€";
    }
}
