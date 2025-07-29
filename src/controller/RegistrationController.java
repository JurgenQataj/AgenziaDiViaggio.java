package controller;

import View.RegistrationView;
import model.User;
import model.dao.RegistrationDAO;
import java.sql.SQLException;

public class RegistrationController {

    private final RegistrationView view;

    public RegistrationController() {
        this.view = new RegistrationView();
    }

    public void start() {
        view.showIntroMessage();
        try {
            // La View cii chiedere i dati e ci restituisce un oggetto User completo
            User newUser = view.getNewUserDetails();

            // Il DAO ora riceve l'oggetto User
            new RegistrationDAO(newUser).execute();

            view.showSuccessMessage();

        } catch (SQLException e) {
            // Gestisco errori comuni
            if (e.getErrorCode() == 1062) {
                view.showErrorMessage("Un utente con questa email o numero di telefono è già registrato.");
            } else {
                view.showErrorMessage("Errore del database durante la registrazione: " + e.getMessage());
            }
        } catch (Exception e) {
            view.showErrorMessage("Si è verificato un errore inaspettato: " + e.getMessage());
        }
    }
}