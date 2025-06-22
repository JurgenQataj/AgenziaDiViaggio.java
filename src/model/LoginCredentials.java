package model;

public class LoginCredentials {

    private final String username;
    private final String password;
    private Role role = null;

    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginCredentials(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
