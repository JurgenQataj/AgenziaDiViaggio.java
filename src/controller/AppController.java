package controller;

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
                    // Modifichiamo il login per restituire anche l'email
                    LoginResult result = loginController.login(); // Assumendo che login() restituisca un oggetto LoginResult

                    if (result != null && result.getRole() != null) {
                        userLoggedIn = true;
                        if (result.getRole() == Role.CLIENTE) {
                            // ################### ECCO LA CORREZIONE ###################
                            // Passiamo l'email ottenuta dal login al costruttore
                            new ClienteController(this, result.getEmail()).start();
                            // #########################################################
                        } else if (result.getRole() == Role.SEGRETERIA) {
                            new SegreteriaController(this).start();
                        }
                    }
                    break;
                case 2:
                    // Nuova versione corretta
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
        }
    }

    public void logout() {
        this.userLoggedIn = false;
        System.out.println("Sei stato disconnesso.");
    }
}