package controller;

import View.RegistrationView;
import model.User;
import model.dao.RegistrationDAO;
import java.sql.SQLException;

public class RegistrationController implements Controller {

    private final RegistrationView view;

    public RegistrationController() {
        this.view = new RegistrationView();
    }

    @Override
    public void start() {
        view.showIntroMessage();
        try {
            User newUser = view.getNewUserDetails();
            new RegistrationDAO(newUser).execute();
            view.showSuccessMessage();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Codice errore per duplicato
                view.showErrorMessage("Un utente con questa email o numero di telefono è già registrato.");
            } else {
                view.showErrorMessage("Errore del database durante la registrazione: " + e.getMessage());
            }
        } catch (Exception e) {
            view.showErrorMessage("Si è verificato un errore inaspettato: " + e.getMessage());
        }
    }
}