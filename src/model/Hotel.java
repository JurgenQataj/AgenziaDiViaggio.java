package model;

import java.text.NumberFormat;
import java.util.Locale;

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
    private Place localita;
    private double costoNottePersona;

    /**
     * Costruttore completo, usato per creare o inserire un nuovo albergo con tutti i dettagli.
     */
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

    /**
     * Costruttore semplificato, usato per mostrare una lista di hotel disponibili,
     * senza bisogno di caricare tutti i dettagli dal database.
     */
    public Hotel(int codice, String nome, int capienza, double costoNottePersona) {
        this.codice = codice;
        this.nome = nome;
        this.capienza = capienza;
        this.costoNottePersona = costoNottePersona;
    }

    // --- GETTERS ---
    public int getCodice() { return codice; }
    public String getNome() { return nome; }
    public String getReferente() { return referente; }
    public int getCapienza() { return capienza; }
    public String getVia() { return via; }
    public String getCivico() { return civico; }
    public String getCp() { return cp; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getFax() { return fax; }
    public Place getLocalita() { return localita; }
    public double getCostoNottePersona() { return costoNottePersona; }

    // --- SETTERS ---
    public void setCodice(int codice) { this.codice = codice; }
    public void setNome(String nome) { this.nome = nome; }
    public void setReferente(String referente) { this.referente = referente; }
    public void setCapienza(int capienza) { this.capienza = capienza; }
    public void setVia(String via) { this.via = via; }
    public void setCivico(String civico) { this.civico = civico; }
    public void setCp(String cp) { this.cp = cp; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setFax(String fax) { this.fax = fax; }
    public void setLocalita(Place localita) { this.localita = localita; }
    public void setCostoNottePersona(double costoNottePersona) { this.costoNottePersona = costoNottePersona; }

    @Override
    public String toString() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.ITALY);

        // Se l'hotel è stato creato con il costruttore semplificato, mostra solo i dati rilevanti.
        if (this.referente == null && this.via == null) {
            return "Hotel [Codice: " + codice + ", Nome: '" + nome + "', Capienza: " + capienza + ", Costo/Notte: " + currencyFormatter.format(costoNottePersona) + "]";
        }

        // Altrimenti, mostra la versione completa, gestendo la possibile assenza della località.
        String nomeLocalita = (localita != null) ? localita.getNome() : "N/A";
        return "Hotel [Codice: " + codice + ", Nome: '" + nome + "', Referente: '" + referente + "', Capienza: " + capienza + ", Indirizzo: " + via + " " + civico + ", " + cp + " " + nomeLocalita + "]";
    }
}