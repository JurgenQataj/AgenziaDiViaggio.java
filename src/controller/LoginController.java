package controller;

import model.LoginCredentials;
import model.Role;
import model.dao.LoginDAO;

import java.sql.SQLException;
import java.util.Scanner;

// La dichiarazione della classe è fondamentale per risolvere l'errore
public class LoginController {

    private final Scanner scanner;
    private final AppController appController;

    public LoginController(Scanner scanner, AppController appController) {
        this.scanner = scanner;
        this.appController = appController;
    }

    /**
     * Gestisce il processo di login.
     * Chiede le credenziali all'utente e restituisce un oggetto LoginResult
     * contenente l'email e il ruolo se il login ha successo.
     * @return Un oggetto LoginResult o null in caso di fallimento.
     * @throws SQLException
     */
    public LoginResult login() throws SQLException {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        LoginCredentials credentials = new LoginCredentials(email, password);
        LoginDAO loginDAO = new LoginDAO(credentials);

        Role role = loginDAO.execute(); // Il DAO chiama la procedura nel DB

        if (role != null) {
            System.out.println("Login effettuato con successo. Ruolo: " + role);
            // Restituisce un oggetto che contiene sia l'email che il ruolo
            return new LoginResult(email, role);
        } else {
            System.out.println("ERRORE: Credenziali non valide. Riprova.");
            return null;
        }
    }
} // <-- Assicurati che questa parentesi graffa di chiusura non manchi