package dao;

import java.util.ArrayList;

/**
 * The interface Utente dao.
 */
public interface UtenteDAO {

    /**
     * Inserisci utente boolean.
     *
     * @param email    the email
     * @param username the username
     * @param password the password
     * @param isAdmin  the is admin
     * @return the boolean
     */
    boolean inserisciUtente(String email, String username, String password, boolean isAdmin);

    /**
     * Gets utente per credenziali.
     *
     * @param emailUsername the email username
     * @param password      the password
     * @return the utente per credenziali
     */
    ArrayList<String> getUtentePerCredenziali(String emailUsername, String password);

    /**
     * Gets utente per username.
     *
     * @param username the username
     * @return the utente per username
     */
    ArrayList<String> getUtentePerUsername(String username);    // Necessario per implementazioni attuali e future (e miglioramenti)

    /**
     * Gets utente per email.
     *
     * @param email the email
     * @return the utente per email
     */
    ArrayList<String> getUtentePerEmail(String email);          // Necessario per implementazioni attuali e future (e miglioramenti)

    /**
     * Esiste utente boolean.
     *
     * @param username the username
     * @param email    the email
     * @return the boolean
     */
    boolean esisteUtente(String username, String email);

    /**
     * Elimina utente boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean eliminaUtente(String email);         // Abbiamo pianificato un update (non inerente ai termini legati al progetto) per espandere funzionalità aggiuntive!

    /**
     * Gets tutti utenti.
     *
     * @return the tutti utenti
     */
    ArrayList<ArrayList<String>> getTuttiUtenti();

}