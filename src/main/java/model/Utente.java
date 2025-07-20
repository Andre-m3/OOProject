package model;

/**
 * The type Utente.
 */
public class Utente {

    // NB: Non possono esistere due utenti con la stessa username o email! Nella nostra Base di Dati abbiamo come PK l'attributo "email"
    private String email;
    private String username;
    private String password;

    /**
     * Instantiates a new Utente.
     *
     * @param email    the email
     * @param username the username
     * @param password the password
     */
// Costruttore di Utente
    public Utente(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Ottiene la password dell'utente (solo per controlli interni)
     *
     * @return La password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
