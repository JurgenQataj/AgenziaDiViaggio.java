package model;

public class Place {
    // Rimuoviamo il campo 'id'
    private String name;
    private String country;

    /**
     * Il nuovo costruttore non ha più bisogno dell'ID.
     */
    public Place(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // --- Getters e Setters (invariati) ---

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

    /**
     * Il metodo toString() ora stampa solo le informazioni reali,
     * senza un ID fittizio.
     */
    @Override
    public String toString() {
        // Formato di output pulito, senza ID.
        return name + " (" + country + ")";
    }
}