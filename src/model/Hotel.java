package model;

import java.text.NumberFormat;
import java.util.Locale;

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
    private final Place localita;
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

    public Hotel(int codice, String nome, int capienza, double costoNottePersona) {
        this(codice, nome, null, capienza, null, null, null, null, null, null, null, costoNottePersona);
    }

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

    @Override
    public String toString() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.ITALY);

        if (this.referente == null && this.via == null) {
            return "Hotel [Codice: " + codice + ", Nome: '" + nome + "', Capienza: " + capienza + ", Costo/Notte: " + currencyFormatter.format(costoNottePersona) + "]";
        }

        String nomeLocalita = (localita != null) ? localita.getNome() : "N/A";
        return "Hotel [Codice: " + codice + ", Nome: '" + nome + "', Referente: '" + referente + "', Capienza: " + capienza + ", Indirizzo: " + via + " " + civico + ", " + cp + " " + nomeLocalita + "]";
    }
}