package model;

import java.util.ArrayList;

public class Utente {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void visualizzaVoli() {
        System.out.println("\n=== Elenco Voli Registrati ===");
        ArrayList<Volo> listaVoli = Volo.getListaVoli();

        if (listaVoli.isEmpty()) {
            System.out.println("Non ci sono voli registrati nel sistema.");
            return;         // Così terminiamo l'esecuzione del metodo. Non usiamo System.exit(0) perché terminerebbe l'intera JVM
        }

        for (Volo volo : listaVoli) {
            Volo.contatoreVoli++;
            System.out.println(volo.toString().replace("COUNT", "" + Volo.contatoreVoli));
        }
        Volo.contatoreVoli = 0;
    }

}
