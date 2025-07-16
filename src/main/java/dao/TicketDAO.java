package dao;

import model.Ticket;
import java.util.List;

public interface TicketDAO {

    boolean inserisciTicket(Ticket ticket);
    Ticket getTicketPerCodicePrenotazioneEPosto(String codicePrenotazione, String postoAssegnato);
    List<Ticket> getTicketsPerPrenotazione(String codicePrenotazione);
    List<Ticket> getTicketsPerVolo(String numeroVolo);
    List<Ticket> getTuttiTickets();
    boolean aggiornaTicket(Ticket ticket);
    boolean eliminaTicket(String codicePrenotazione, String postoAssegnato);
    boolean eliminaTicketsPerPrenotazione(String codicePrenotazione);
}