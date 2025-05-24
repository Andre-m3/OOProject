import model.Amministratore;
import model.UtenteGenerico;

public class Main {
    public static void main(String[] args) {

        // Genero le istanze di due utenti a scopo di test. Quella di un'utente generico e quella di un'utente amministratore
        // Cosi potremo verificare senza in modo diretto e immediato l'eventuale presenza di errori di base

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

        admin1.inserimentoVolo();                   // Metodo accessibile dal solo Amministratore
        admin1.inserimentoVolo();                   // Metodo accessibile dal solo Amministratore
        admin1.aggiornaVolo();                      // Metodo accessibile dal solo Amministratore
        admin1.modificaGateImbarco();               // Metodo accessibile dal solo Amministratore
        admin1.visualizzaVoli();                    // Metodo accessibile da chiunque

    }
}
