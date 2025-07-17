package controller;

import model.LoginCredentials;
import model.Role;

public class AppController {
    public void start() {
        System.out.println("--- Benvenuto nell'Agenzia di Viaggio ---");

        // Il loop principale gestisce il processo di login e il re-login dopo il logout.
        while (true) {
            LoginController loginController = new LoginController();
            LoginCredentials credentials = loginController.authenticateUser();

            // Se il login ha successo (credentials non è null), avvia il controller appropriato.
            if (credentials != null) {
                Controller userController = null;
                if (credentials.getRole() == Role.SEGRETERIA) {
                    userController = new SegreteriaController(credentials);
                } else if (credentials.getRole() == Role.CLIENTE) {
                    userController = new ClienteController(credentials);
                }

                // Avvia il controller specifico per l'utente loggato.
                if (userController != null) {
                    userController.start();
                }
                // Una volta che l'utente fa logout (il metodo start() del suo controller termina),
                // il loop ricomincia, mostrando di nuovo la schermata di login.

            } else {
                // Se l'utente non riesce a loggarsi o interrompe, potremmo decidere di
                // terminare l'applicazione o semplicemente riprovare.
                // Per ora, riproviamo il login.
                System.out.println("Processo di login terminato. Riprovare...");
            }
        }
    }
}