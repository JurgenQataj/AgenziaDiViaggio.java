package model;

public class User {
    private final String nome;
    private final String cognome;
    private final String email;
    private final String password;
    private final String telefono;
    private final Role ruolo;

    public User(String nome, String cognome, String email, String password, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.ruolo = Role.CLIENTE; // I nuovi utenti sono sempre clienti
    }

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

    public String toString() {
        return "Utente [Email: " + email +
                ", Nome: " + nome +
                " " + cognome +
                ", Telefono: " + telefono +
                ", Ruolo: " + getRuolo() + "]";
    }

}