package model;

/*
 * The type Utente.
 */

public class Utente {
    private String email;                   //C'ERA SCRITTO "PRIVATE FINAL". DA' ERRORE.
    private String username;
    private String password;

    /*
     * Instantiates a new Utente.
     *
     * @param login    the login
     * @param password the password
     */

    public Utente(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /*
     * Gets login.
     *
     * @return the login
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String visualizzaVoli() {
        return "ciao";
    }
}
