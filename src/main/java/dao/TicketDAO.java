package dao;

import java.util.ArrayList;

/**
 * The interface Ticket dao.
 */
public interface TicketDAO {

    /**
     * Inserisci ticket boolean.
     *
     * @param codicePrenotazione the codice prenotazione
     * @param nome               the nome
     * @param cognome            the cognome
     * @param numeroDocumento    the numero documento
     * @param dataNascita        the data nascita
     * @param postoAssegnato     the posto assegnato
     * @return the boolean
     */
    boolean inserisciTicket(String codicePrenotazione, String nome, String cognome,
                            String numeroDocumento, String dataNascita, String postoAssegnato);

    /**
     * Gets tickets per prenotazione.
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the tickets per prenotazione
     */
    ArrayList<ArrayList<String>> getTicketsPerPrenotazione(String codicePrenotazione);

    /**
     * Aggiorna ticket boolean.
     *
     * @param codicePrenotazione the codice prenotazione
     * @param postoAssegnato     the posto assegnato
     * @param nome               the nome
     * @param cognome            the cognome
     * @param numeroDocumento    the numero documento
     * @param dataNascita        the data nascita
     * @return the boolean
     */
    boolean aggiornaTicket(String codicePrenotazione, String postoAssegnato, String nome,
                           String cognome, String numeroDocumento, String dataNascita);

    /**
     * Elimina tickets per prenotazione boolean.
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the boolean
     */
    boolean eliminaTicketsPerPrenotazione(String codicePrenotazione);
}