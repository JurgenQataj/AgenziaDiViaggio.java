package controller;

import model.dao.RegistrationDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class RegistrationController {
    private final Scanner scanner;

    public RegistrationController(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Gestisce il flusso di interazione per la registrazione di un nuovo utente.
     */
    public void handleRegistration() {
        System.out.println("\n--- REGISTRAZIONE NUOVO UTENTE ---");
        System.out.print("Inserisci la tua email: ");
        String email = scanner.nextLine();
        System.out.print("Inserisci il tuo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Inserisci il tuo cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Inserisci un numero di telefono: ");
        String telefono = scanner.nextLine();
        System.out.print("Scegli una password: ");
        String password = scanner.nextLine();

        if (email.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            System.out.println("ERRORE: Tutti i campi sono obbligatori.");
            return;
        }

        try {
            // 1. Crea il DAO passando tutti i dati nel costruttore
            RegistrationDAO dao = new RegistrationDAO(email, nome, cognome, password, telefono);
            // 2. Chiama il metodo execute() senza argomenti
            dao.execute();

            System.out.println("\n-> Registrazione completata con successo! Ora puoi effettuare il login.");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                if (ex.getMessage().contains("for key 'utente.email'")) {
                    System.out.println("ERRORE: L'email inserita è già in uso.");
                } else if (ex.getMessage().contains("for key 'utente.telefono'")) {
                    System.out.println("ERRORE: Il numero di telefono inserito è già in uso.");
                } else {
                    System.out.println("ERRORE DI DUPLICAZIONE: " + ex.getMessage());
                }
            } else {
                System.out.println("ERRORE DATABASE DURANTE LA REGISTRAZIONE: " + ex.getMessage());
            }
        }
    }
}