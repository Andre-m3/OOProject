package model;

public class Amministratore extends Utente {
    public Amministratore(String email, String username, String password) {
        super(email, username, password);
    }

    public void inserimentoVolo() {
        System.out.println("Inserimento volo: ");
    }

    public void aggiornaVolo() {
        System.out.println("Aggiorna volo: ");
    }

    public void modificaGateImbarco() {
        System.out.println("Modifica gate imbarco: ");
    }
}
