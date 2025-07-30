package controller;

import model.LoginResult;
import model.Role;
import java.util.Scanner;

public class AppController {

    private final Scanner scanner;
    private LoginResult currentUser = null; // Memorizza chi è l'utente loggato

    public AppController() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            if (currentUser == null) {

                running = showMainMenu();
            } else {

                Controller userController;
                if (currentUser.getRole() == Role.CLIENTE) {
                    userController = new ClienteController(this, currentUser.getEmail());
                } else {
                    userController = new SegreteriaController(this);
                }
                userController.start();
            }
        }

        System.out.println("Grazie per aver usato i nostri servizi. Arrivederci!");
    }

    private boolean showMainMenu() {
        System.out.println("\n--- BENVENUTO NELL'AGENZIA VIAGGI ---");
        System.out.println("1) Login");
        System.out.println("2) Registrati");
        System.out.println("3) Esci");
        System.out.print("Scegli un'opzione: ");

        String choice = scanner.nextLine();
        Controller selectedController = null;

        switch (choice) {
            case "1":
                LoginController loginController = new LoginController();
                this.currentUser = loginController.executeLogin();
                if (currentUser == null) {
                    System.out.println("ERRORE: Credenziali non valide. Riprova.");
                }
                break;
            case "2":
                selectedController = new RegistrationController();
                break;
            case "3":
                // Se l'utente sceglie di uscire, restituisce false
                return false;
            default:
                System.out.println("Opzione non valida. Riprova.");
                break;
        }

        if (selectedController != null) {
            selectedController.start();
        }

        return true;
    }

    public void logout() {
        this.currentUser = null;
        System.out.println("\nSei stato disconnesso. Stai tornando al menu principale...");
    }
}