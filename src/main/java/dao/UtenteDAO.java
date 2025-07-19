package dao;

import java.util.ArrayList;

public interface UtenteDAO {

    boolean inserisciUtente(String email, String username, String password, boolean isAdmin);
    ArrayList<String> getUtentePerCredenziali(String emailUsername, String password);
    ArrayList<String> getUtentePerUsername(String username);
    ArrayList<String> getUtentePerEmail(String email);
    boolean esisteUtente(String username, String email);
    boolean aggiornaUtente(String username, String email, String password, boolean isAdmin);
    boolean eliminaUtente(String username);
    ArrayList<ArrayList<String>> getTuttiUtenti();

}