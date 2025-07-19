package dao;

import java.util.ArrayList;

public interface TicketDAO {

    boolean inserisciTicket(String codicePrenotazione, String nome, String cognome,
                            String numeroDocumento, String dataNascita, String postoAssegnato);
    ArrayList<String> getTicketPerCodicePrenotazioneEPosto(String codicePrenotazione, String postoAssegnato);
    ArrayList<ArrayList<String>> getTicketsPerPrenotazione(String codicePrenotazione);
    ArrayList<ArrayList<String>> getTicketsPerVolo(String numeroVolo);
    ArrayList<ArrayList<String>> getTuttiTickets();
    boolean aggiornaTicket(String codicePrenotazione, String postoAssegnato, String nome,
                           String cognome, String numeroDocumento, String dataNascita);
    boolean eliminaTicket(String codicePrenotazione, String postoAssegnato);
    boolean eliminaTicketsPerPrenotazione(String codicePrenotazione);
}