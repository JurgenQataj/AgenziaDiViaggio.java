package model;

public class LoginCredentials {
    private final String email;
    private final String password;

    public LoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Restituisce l'email per queste credenziali.
     * @return una Stringa contenente l'email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce la password per queste credenziali.
     * @return una Stringa contenente la password.
     */
    public String getPassword() {
        return password;
    }
}