
package implementazioniPostgresDAO;

import dao.TicketDAO;
import database.ConnessioneDatabase;
import model.Ticket;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImplementazioneTicketDAO implements TicketDAO {

    private Connection connection;

    public ImplementazioneTicketDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean inserisciTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (codice_prenotazione, nome, cognome, numero_documento, data_nascita, posto_assegnato) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ticket.getCodicePrenotazione());
            stmt.setString(2, ticket.getNome());
            stmt.setString(3, ticket.getCognome());
            stmt.setString(4, ticket.getNumeroDocumento());
            stmt.setDate(5, Date.valueOf(ticket.getDataNascita()));
            stmt.setString(6, ticket.getPostoAssegnato());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento ticket: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Ticket getTicketPerCodicePrenotazioneEPosto(String codicePrenotazione, String postoAssegnato) {
        String sql = "SELECT * FROM tickets WHERE codice_prenotazione = ? AND posto_assegnato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);
            stmt.setString(2, postoAssegnato);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaTicketDalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca ticket per prenotazione e posto: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Ticket> getTicketsPerPrenotazione(String codicePrenotazione) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE codice_prenotazione = ? ORDER BY posto_assegnato";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tickets.add(creaTicketDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero tickets per prenotazione: " + e.getMessage());
        }

        return tickets;
    }

    @Override
    public List<Ticket> getTicketsPerVolo(String numeroVolo) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.* FROM tickets t JOIN prenotazioni p ON t.codice_prenotazione = p.codice_prenotazione WHERE p.numero_volo = ? ORDER BY t.posto_assegnato";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tickets.add(creaTicketDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero tickets per volo: " + e.getMessage());
        }

        return tickets;
    }

    @Override
    public List<Ticket> getTuttiTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY codice_prenotazione, posto_assegnato";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tickets.add(creaTicketDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutti i tickets: " + e.getMessage());
        }

        return tickets;
    }

    @Override
    public boolean aggiornaTicket(Ticket ticket) {
        String sql = "UPDATE tickets SET nome = ?, cognome = ?, numero_documento = ?, data_nascita = ? WHERE codice_prenotazione = ? AND posto_assegnato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ticket.getNome());
            stmt.setString(2, ticket.getCognome());
            stmt.setString(3, ticket.getNumeroDocumento());
            stmt.setDate(4, Date.valueOf(ticket.getDataNascita()));
            stmt.setString(5, ticket.getCodicePrenotazione());
            stmt.setString(6, ticket.getPostoAssegnato());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento ticket: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminaTicket(String codicePrenotazione, String postoAssegnato) {
        String sql = "DELETE FROM tickets WHERE codice_prenotazione = ? AND posto_assegnato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);
            stmt.setString(2, postoAssegnato);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione ticket: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminaTicketsPerPrenotazione(String codicePrenotazione) {
        String sql = "DELETE FROM tickets WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione tickets per prenotazione: " + e.getMessage());
            return false;
        }
    }


    // Metodo di utilità per creare un oggetto Ticket dal ResultSet
    private Ticket creaTicketDalResultSet(ResultSet rs) throws SQLException {
        String nome = rs.getString("nome");
        String cognome = rs.getString("cognome");
        String numeroDocumento = rs.getString("numero_documento");
        LocalDate dataNascita = rs.getDate("data_nascita").toLocalDate();
        String postoAssegnato = rs.getString("posto_assegnato");
        String codicePrenotazione = rs.getString("codice_prenotazione");

        return new Ticket(nome, cognome, numeroDocumento, dataNascita.toString(), postoAssegnato, codicePrenotazione);
    }
}