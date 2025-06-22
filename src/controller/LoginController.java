package controller;

import exceptions.DatabaseException;
import model.LoginCredentials;
import model.dao.LoginDAO;
import view.LoginView;

public class LoginController implements Controller {

    private final LoginView loginView;

    private LoginCredentials loginCredentials;

    public LoginController() {
        loginView = new LoginView();
    }

    @Override
    public void start() {
        LoginCredentials credentials = this.loginView.authenticate();

        try {
            this.loginCredentials = new LoginDAO().execute(credentials.getUsername(), credentials.getPassword());
        } catch (DatabaseException e) {
            System.err.printf("Login error: %s%n", e.getMessage());
        }
    }

    public LoginCredentials
    getLoginCredentials() {
        return loginCredentials;
    }
}
