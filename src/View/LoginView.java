package view;

import model.LoginCredentials;

import java.util.Scanner;

public class LoginView {

    private final Scanner scanner;

    public LoginView() {
        this.scanner = new Scanner(System.in);
    }

    public LoginCredentials authenticate() {
        System.out.println("Effettuare l'accesso");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        return new LoginCredentials(username, password);
    }
}

