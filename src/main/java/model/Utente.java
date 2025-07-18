package model;

import java.util.ArrayList;

public class Utente {

    // NB: Non possono esistere due utenti con la stessa username o email!
    private String email;
    private String username;
    private String password;

    // Costruttore di Utente
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

    /**
     * Ottiene la password dell'utente (solo per controlli interni)
     * @return La password dell'utente
     */
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verifica se la password inserita corrisponde alla password dell'utente
     * @param passwordToCheck La password da verificare
     * @return true se la password è corretta, false altrimenti
     */
    public boolean verificaPassword(String passwordToCheck) {
        return this.password.equals(passwordToCheck);
    }


    public void visualizzaVoli() {
        System.out.println("\n=== Elenco Voli Registrati ===");
        ArrayList<Volo> listaVoli = Volo.getListaVoli();

        if (listaVoli.isEmpty()) {
            System.out.println("Non ci sono voli registrati nel sistema.");
            return;         // Così terminiamo l'esecuzione del metodo. Non usiamo System.exit(0) perché terminerebbe l'intera JVM
        }

        for (Volo volo : listaVoli) {
            System.out.println(volo.toString());
        }
    }

}
