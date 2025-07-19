package model;

public class User {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    private Role ruolo; // Il ruolo sarà gestito internamente, non chiesto all'utente

    /**
     * Costruttore per la registrazione di un nuovo utente.
     * Il ruolo viene impostato di default a CLIENTE.
     */
    public User(String nome, String cognome, String email, String password, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.ruolo = Role.CLIENTE; // I nuovi utenti sono sempre clienti
    }

    // --- GETTERS ---
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTelefono() {
        return telefono;
    }

    public Role getRuolo() {
        return ruolo;
    }

    // --- SETTERS ---
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setRuolo(Role ruolo) {
        this.ruolo = ruolo;
    }
}