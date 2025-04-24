import model.Amministratore;
import model.UtenteGenerico;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // Genero le istanze di due utenti a scopo di test. Cosi posso verificare senza difficoltà i vari metodi qui nel main
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

        amministratore.inserimentoVolo();           //Metodo accessibile dal solo Amministratore
        utente.prenotaVolo();                       //Metodo accessibile dal solo Utente
    }
}
