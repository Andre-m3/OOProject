package model;

public class Utente {
    private String email;
    private String username;
    private String password;

    /*
     * Instantiates a new Utente.
     *
     * @param email the email
     * @param username the username
     * @param password the password
     */

    public Utente(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

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

    public void setPassword(String password) {
        this.password = password;
    }
    // Seppur i livelli di sicurezza non siano stati affrontati, non è opportuno aggiungere il metodo getPassword
    // Questa volta solo ed esclusivamente per mostrare in output i vari valori, utilizzeremo il metodo "toString".
    // Verrà abbandonato e di conseguenza rimosso alla prossima implementazione!
    public String toString() {
        return "Username: " + username + "\nEmail: " + email + "\nPassword: " + password;
    }

    public String visualizzaVoli() {
        return "verifica richiamo metodo - temporanea...";
    }
}
