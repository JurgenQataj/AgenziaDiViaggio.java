package View;

import model.LoginCredentials;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginView extends View {

    private final BufferedReader reader;

    public LoginView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }
    public LoginCredentials authenticate() {
        try {
            System.out.println("\n--- LOGIN ---");
            System.out.print("Email: ");
            String email = reader.readLine();
            System.out.print("Password: ");
            String password = reader.readLine();
            return new LoginCredentials(email, password);
        } catch (IOException e) {
            // Usiamo il metodo printError ereditato per mostrare l'errore
            printError(new RuntimeException("Impossibile leggere l'input dell'utente.", e));
            return new LoginCredentials("", "");
        }
    }

}