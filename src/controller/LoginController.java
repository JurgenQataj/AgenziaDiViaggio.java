package controller;

import exceptions.DatabaseException;
import model.LoginCredentials;
import model.Role;
import model.dao.LoginDAO;
import view.LoginView;

import java.sql.SQLException;

public class LoginController implements Controller {

    private final LoginView loginView;

    public LoginController() {
        loginView = new LoginView();
    }

    @Override
    public void start() {
        // Questo metodo è intenzionalmente vuoto perché la logica
        // è ora gestita da AppController che chiama authenticateUser.
    }

    public LoginCredentials authenticateUser() {
        LoginCredentials credentials = loginView.authenticate();
        if (credentials == null) return null; // L'utente potrebbe aver interrotto

        try {
            LoginDAO loginDAO = new LoginDAO(credentials);
            Role role = loginDAO.execute();

            if (role != null) {
                credentials.setRole(role);
                loginView.showMessage("Login effettuato con successo come: " + role);
                return credentials;
            } else {
                loginView.showMessage("Credenziali non valide. Riprova.");
                return null;
            }
        } catch (SQLException e) {
            loginView.printError(new DatabaseException(e.getMessage()));
            return null;
        }
    }
}