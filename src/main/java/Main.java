import model.Amministratore;
import model.UtenteGenerico;

public class Main {
    public static void main(String[] args) {

        // Genero le istanze di due utenti a scopo di test. Quella di un'utente generico e quella di un amministratore
        // Cosi posso verificare senza difficoltà che non ci siano errori anche richiamando i vari metodi

        UtenteGenerico utente = new UtenteGenerico(
                "giancarloMinecraft@gmail.com",
                "gianmine10",
                "Giancarlo10!"
        );  // Utente -> Utente Generico

        Amministratore amministratore = new Amministratore(
                "andreamontella@capodichino.eu",
                "admin-AndreaMontella",
                "!MyAmazingPassword_3!"
        );  // Utente -> Amministratore

        amministratore.inserimentoVolo();           // Metodo accessibile dal solo Amministratore
        utente.prenotaVolo();                       // Metodo accessibile dal solo Utente
    }
}
