package model;

public class Place {

    private int id;
    private String name;
    private String country;

    // ---> AGGIUNGI QUESTO COSTRUTTORE <---
    public Place() {
        // Costruttore vuoto
    }

    // Questo è il costruttore che hai già
    public Place(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // Qui ci sono i tuoi metodi getter e setter...
    // Assicurati che ci sia anche setId(), come detto prima.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
