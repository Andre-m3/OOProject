package dao;

import java.util.ArrayList;

/**
 * The interface Prenotazione dao.
 */
public interface PrenotazioneDAO {

    /**
     * Inserisci prenotazione boolean.
     *
     * @param codicePrenotazione the codice prenotazione
     * @param email              the email
     * @param numeroVolo         the numero volo
     * @param stato              the stato
     * @param numeroPasseggeri   the numero passeggeri
     * @return the boolean
     */
    boolean inserisciPrenotazione(String codicePrenotazione, String email, String numeroVolo,
                                  String stato, int numeroPasseggeri);

    /**
     * Gets prenotazione per codice.
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the prenotazione per codice
     */
    ArrayList<String> getPrenotazionePerCodice(String codicePrenotazione);

    /**
     * Gets prenotazioni per utente.
     *
     * @param email the email
     * @return the prenotazioni per utente
     */
    ArrayList<ArrayList<String>> getPrenotazioniPerUtente(String email);

    /**
     * Gets prenotazioni per volo.
     *
     * @param numeroVolo the numero volo
     * @return the prenotazioni per volo
     */
    ArrayList<ArrayList<String>> getPrenotazioniPerVolo(String numeroVolo);

    /**
     * Gets tutte prenotazioni.
     *
     * @return the tutte prenotazioni
     */
    ArrayList<ArrayList<String>> getTuttePrenotazioni();

    /**
     * Elimina prenotazione boolean.
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the boolean
     */
    boolean eliminaPrenotazione(String codicePrenotazione);
}