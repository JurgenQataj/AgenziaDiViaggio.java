package controller;

import model.Role;
import java.util.Scanner;

public class AppController {
    private final Scanner scanner;

    public AppController() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Metodo principale che avvia e gestisce il ciclo di vita dell'applicazione.
     */
    public void start() {
        while (true) {
            showInitialMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    // Delega al LoginController
                    new LoginController(scanner, this).handleLogin();
                    break;
                case 2:
                    // Delega al RegistrationController
                    new RegistrationController(scanner).handleRegistration();
                    break;
                case 3:
                    System.out.println("Arrivederci!");
                    return; // Esce dal ciclo e termina l'applicazione
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }

    private void showInitialMenu() {
        System.out.println("\n--- BENVENUTO NELL'AGENZIA DI VIAGGI ---");
        System.out.println("1) Esegui il Login");
        System.out.println("2) Registrati");
        System.out.println("3) Esci");
        System.out.print("Scegli un'opzione: ");
    }

    /**
     * Questo metodo viene chiamato dal LoginController in caso di successo.
     * Avvia il controller specifico per il ruolo (Cliente o Segreteria).
     * @param role Il ruolo dell'utente loggato.
     * @param userEmail L'email dell'utente, da passare al controller del cliente.
     */
    public void onLoginSuccess(Role role, String userEmail) {
        if (role == Role.CLIENTE) {
            ClienteController clienteController = new ClienteController(scanner, userEmail, this);
            clienteController.start(); // Il cliente controller avrà il suo ciclo di menu
        } else if (role == Role.SEGRETERIA) {
            SegreteriaController segreteriaController = new SegreteriaController(scanner, this);
            segreteriaController.start(); // Il segreteria controller avrà il suo ciclo di menu
        }
    }

    /**
     * Metodo chiamato dai controller Cliente/Segreteria per effettuare il logout.
     * L'azione di logout consiste semplicemente nel terminare il menu specifico del ruolo.
     * Il controllo tornerà al ciclo `while` del metodo `start()`, mostrando di nuovo il menu iniziale.
     */
    public void logout() {
        System.out.println("Logout effettuato con successo.");
    }

    private int getUserChoice() {
        try {
            // Usiamo nextLine() per consumare l'intera riga ed evitare problemi con input successivi
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Ritorna un valore che causerà l'errore di default nello switch
        }
    }
}