package model;

public class Hotel {
    private int codice;
    private String nome;
    private String referente;
    private int capienza;
    private String via;
    private String civico;
    private String cp;
    private String email;
    private String telefono;
    private String fax;
    private Place place; // Manteniamo l'oggetto Place per la coerenza
    private float costoNottePersona;

    // Costruttore completo per allinearsi al nuovo DB
    public Hotel(int codice, String nome, String referente, int capienza, String via, String civico, String cp, String email, String telefono, String fax, Place place, float costoNottePersona) {
        this.codice = codice;
        this.nome = nome;
        this.referente = referente;
        this.capienza = capienza;
        this.via = via;
        this.civico = civico;
        this.cp = cp;
        this.email = email;
        this.telefono = telefono;
        this.fax = fax;
        this.place = place;
        this.costoNottePersona = costoNottePersona;
    }

    // --- GETTERS ---
    // Aggiungiamo tutti i metodi "get" necessari per accedere ai campi privati.

    public int getCodice() {
        return codice;
    }

    /**
     * Questo è il metodo che mancava e che causava l'errore di compilazione.
     * @return Il nome dell'hotel.
     */
    public String getNome() {
        return nome;
    }

    public String getReferente() {
        return referente;
    }

    public int getCapienza() {
        return capienza;
    }

    public String getVia() {
        return via;
    }

    public String getCivico() {
        return civico;
    }

    public String getCp() {
        return cp;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFax() {
        return fax;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public float getCostoNottePersona() {
        return costoNottePersona;
    }

    @Override
    public String toString() {
        return String.format("Hotel #%d: %s, Località: %s, Capienza: %d, Costo/Notte: %.2f€",
                codice,
                nome,
                (place != null ? place.getName() : "N/D"),
                capienza,
                costoNottePersona
        );
    }
}