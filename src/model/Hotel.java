package model;

public class Hotel {
    private final int codice;
    private final String nome;
    private final String referente;
    private final int capienza;
    private final String via;
    private final String civico;
    private final String cp;
    private final String email;
    private final String telefono;
    private final String fax;
    private final Place localita; // Contiene l'intero oggetto Place
    private final double costoNottePersona;

    public Hotel(int codice, String nome, String referente, int capienza, String via, String civico, String cp, String email, String telefono, String fax, Place localita, double costoNottePersona) {
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
        this.localita = localita;
        this.costoNottePersona = costoNottePersona;
    }

    // Getters per il DAO
    public String getNome() { return nome; }
    public String getReferente() { return referente; }
    public int getCapienza() { return capienza; }
    public String getVia() { return via; }
    public String getCivico() { return civico; }
    public String getCp() { return cp; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getFax() { return fax; }
    // Getter speciale che restituisce solo il nome della località come String
    public String getLocalitaNome() { return localita.getNome(); }
    public double getCostoNottePersona() { return costoNottePersona; }

    @Override
    public String toString() {
        return String.format("Hotel #%d: %s, %s - Capienza: %d, Costo/Notte: %.2f €",
                codice, nome, localita.getNome(), capienza, costoNottePersona);
    }
}