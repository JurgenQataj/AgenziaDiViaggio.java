package controller;

import model.LoginCredentials;
import model.LoginResult;
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

            return new LoginResult(email, role);
        } else {
            System.out.println("ERRORE: Credenziali non valide. Riprova.");
            return null;
        }
    }
}