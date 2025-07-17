package view;

import exceptions.DatabaseException;
import model.LoginCredentials;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginView {

    private final BufferedReader reader;

    public LoginView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public LoginCredentials authenticate() {
        try {
            System.out.println("--- LOGIN ---");
            System.out.print("Email: ");
            String username = reader.readLine();
            System.out.print("Password: ");
            String password = reader.readLine();
            return new LoginCredentials(username, password);
        } catch (IOException e) {
            throw new RuntimeException("Impossibile leggere l'input dell'utente", e);
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void printError(DatabaseException e) {
        System.err.println("ERRORE DI LOGIN: " + e.getMessage());
    }
}