package dao;

import java.util.ArrayList;

public interface TicketDAO {

    boolean inserisciTicket(String codicePrenotazione, String nome, String cognome,
                            String numeroDocumento, String dataNascita, String postoAssegnato);
    ArrayList<ArrayList<String>> getTicketsPerPrenotazione(String codicePrenotazione);
    boolean aggiornaTicket(String codicePrenotazione, String postoAssegnato, String nome,
                           String cognome, String numeroDocumento, String dataNascita);
    boolean eliminaTicketsPerPrenotazione(String codicePrenotazione);
}