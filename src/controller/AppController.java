package controller;

import model.LoginResult; // <-- IMPORT AGGIUNTO (LA SOLUZIONE)
import model.Role;
import model.dao.ConnectionFactory;
import java.util.Scanner;

public class AppController {

    private final Scanner scanner;
    private boolean userLoggedIn = false;

    public AppController() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            // This loop ensures the menu is always shown after a controller (like Segreteria or Cliente) finishes.
            if (!userLoggedIn) {
                showMainMenu();
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- BENVENUTO NELL'AGENZIA VIAGGI ---");
        System.out.println("1) Login");
        System.out.println("2) Registrati");
        System.out.println("3) Esci");
        System.out.print("Scegli un'opzione: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    LoginController loginController = new LoginController(scanner, this);
                    LoginResult result = loginController.login();

                    if (result != null && result.getRole() != null) {
                        userLoggedIn = true;
                        if (result.getRole() == Role.CLIENTE) {
                            new ClienteController(this, result.getEmail()).start();
                        } else if (result.getRole() == Role.SEGRETERIA) {
                            new SegreteriaController(this).start();
                        }
                    }
                    break;
                case 2:
                    new RegistrationController().start();
                    break;
                case 3:
                    System.out.println("Arrivederci!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opzione non valida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERRORE: Inserisci un'opzione numerica.");
        } catch (Exception e) {
            System.out.println("ERRORE INASPETTATO: " + e.getMessage());
            e.printStackTrace(); // Useful for debugging unexpected errors
        }
    }

    public void logout() {
        this.userLoggedIn = false;
        System.out.println("\nSei stato disconnesso. Stai tornando al menu principale...");
    }
}