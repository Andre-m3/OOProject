package dao;

import model.Utente;
import java.util.List;

public interface UtenteDAO {

    boolean inserisciUtente(Utente utente);
    Utente getUtentePerCredenziali(String emailUsername, String password);
    Utente getUtentePerUsername(String username);
    Utente getUtentePerEmail(String email);
    boolean esisteUtente(String username, String email);
    boolean aggiornaUtente(Utente utente);
    boolean eliminaUtente(String username);
    List<Utente> getTuttiUtenti();
}