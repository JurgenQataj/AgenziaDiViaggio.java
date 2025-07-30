package controller;

import View.LoginView;
import exceptions.DatabaseException;
import model.LoginCredentials;
import model.LoginResult;
import model.Role;
import model.dao.LoginDAO;

public class LoginController {

    private final LoginView view;

    public LoginController() {
        this.view = new LoginView();
    }

    public LoginResult executeLogin() {
        LoginCredentials credentials = view.authenticate();

        if (credentials.getEmail() == null || credentials.getEmail().isEmpty()) {
            return null;
        }

        LoginDAO loginDAO = new LoginDAO(credentials);

        try {
            Role role = loginDAO.execute();
            if (role != null) {
                view.showMessage("Login effettuato con successo. Ruolo: " + role);
                return new LoginResult(credentials.getEmail(), role);
            }
        } catch (DatabaseException e) {

            view.printError(e);
        }

        return null;
    }
}