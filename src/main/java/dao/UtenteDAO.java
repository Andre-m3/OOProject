package dao;

import java.util.ArrayList;

public interface UtenteDAO {

    boolean inserisciUtente(String email, String username, String password, boolean isAdmin);
    ArrayList<String> getUtentePerCredenziali(String emailUsername, String password);
    ArrayList<String> getUtentePerUsername(String username);    // Necessario per implementazioni attuali e future (e miglioramenti)
    ArrayList<String> getUtentePerEmail(String email);          // Necessario per implementazioni attuali e future (e miglioramenti)
    boolean esisteUtente(String username, String email);
    boolean eliminaUtente(String email);         // Abbiamo pianificato un update (non inerente ai termini legati al progetto) per espandere funzionalità aggiuntive!
    ArrayList<ArrayList<String>> getTuttiUtenti();

}