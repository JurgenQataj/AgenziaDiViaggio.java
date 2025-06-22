package controller;

import exceptions.RoleException;
import model.LoginCredentials;

public class AppController implements Controller {
    @Override
    public void start() {

        LoginController loginController = new LoginController();
        LoginCredentials credentials = null;
        int retries = 0;
        do {
            loginController.start();
            credentials = loginController.getLoginCredentials();
            retries++;
        } while (retries < 3 && credentials == null);

        if (credentials.getRole() == null)
            throw new RoleException("Credenziali invalide");

        switch (credentials.getRole()) {
            case CLIENTE -> new ClienteController(credentials).start();
            case SEGRETERIA -> new SegreteriaController(credentials).start();
            default -> throw new RuntimeException("Invalid credentials");
        }

    }
}