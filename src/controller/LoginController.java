package controller;

import model.LoginCredentials;
import model.Role;
import model.dao.LoginDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginController {
    private final Scanner scanner;
    private final AppController appController;

    public LoginController(Scanner scanner, AppController appController) {
        this.scanner = scanner;
        this.appController = appController;
    }

    /**
     * Gestisce il flusso di interazione per il login.
     */
    public void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Inserisci email: ");
        String email = scanner.nextLine(); // Abbiamo l'email qui
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        LoginCredentials credentials = new LoginCredentials(email, password);

        try {
            LoginDAO dao = new LoginDAO(credentials);
            Role role = dao.execute();

            if (role != null) {
                // --- CORREZIONE: Usiamo la variabile 'email' che già abbiamo ---
                appController.onLoginSuccess(role, email);
            } else {
                System.out.println("ERRORE: Email o password non validi.");
            }
        } catch (SQLException e) {
            System.out.println("ERRORE DATABASE: " + e.getMessage());
        }
    }
}