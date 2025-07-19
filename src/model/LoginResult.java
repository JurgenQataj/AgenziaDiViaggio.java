package controller;
import model.Role;

public class LoginResult {
    private final String email;
    private final Role role;

    public LoginResult(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() { return email; }
    public Role getRole() { return role; }
}