package View;

import model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegistrationView extends View {
    private final BufferedReader reader;

    public RegistrationView() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void showIntroMessage() {
        System.out.println("\n--- REGISTRAZIONE NUOVO UTENTE ---");
        System.out.println("Inserisci i tuoi dati.");
    }

    public User getNewUserDetails() throws IOException {
        System.out.print("Nome: ");
        String nome = reader.readLine();
        System.out.print("Cognome: ");
        String cognome = reader.readLine();
        System.out.print("Email: ");
        String email = reader.readLine();
        System.out.print("Password: ");
        String password = reader.readLine();
        System.out.print("Telefono: ");
        String telefono = reader.readLine();

        return new User(nome, cognome, email, password, telefono);
    }

    public void showSuccessMessage() {
        showMessage("Registrazione effettuata con successo! Ora puoi effettuare il login.");
    }

    public void showErrorMessage(String message) {
        printError(new Exception(message));
    }
}