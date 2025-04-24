package model;

public class UtenteGenerico extends Utente {

    public UtenteGenerico(String email, String username, String password) {
        super(email, username, password);
    }

    public void prenotaVolo() {
        System.out.println("Prenota volo: ");
    }

    public void cercaPrenotazioneVolo() {
        System.out.println("Cerca prenotazione volo: ");
    }

    public void cercaPrenotazioneNome(String username) {
        System.out.println("Cerca prenotazione nome: ");
    }

    public void modificaPrenotazione() {
        System.out.println("Modifica prenotazione: ");
    }
}
