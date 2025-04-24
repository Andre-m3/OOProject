import model.Amministratore;
import model.UtenteGenerico;

public class Main {
    public static void main(String[] args) {

        // Genero le istanze di due utenti a scopo di test. Quella di un'utente generico e quella di un amministratore
        // Cosi posso verificare senza difficoltà che non ci siano errori anche richiamando i vari metodi

        UtenteGenerico utente1 = new UtenteGenerico(
                "giancarloMinecraft@gmail.com",
                "gianmine10",
                "Giancarlo10!"
        );  // Utente -> Utente Generico

        Amministratore admin1 = new Amministratore(
                "andreamontella@capodichino.eu",
                "admin-AndreaMontella",
                "!MyAmazingPassword_3!"
        );  // Utente -> Amministratore

        // Richiamiamo il metodo toString per simulare il ritorno dei dati quali: email, username, password
        System.out.println(utente1.toString());
        System.out.println(admin1.toString());

        admin1.inserimentoVolo();           // Metodo accessibile dal solo Amministratore
        utente1.prenotaVolo();                       // Metodo accessibile dal solo Utente
    }
}
