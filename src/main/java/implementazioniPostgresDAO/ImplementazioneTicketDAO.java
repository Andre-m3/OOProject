package implementazioniPostgresDAO;

import dao.TicketDAO;
import database.ConnessioneDatabase;
// NESSUN IMPORT DAL MODEL!

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    public boolean inserisciTicket(String codicePrenotazione, String nome, String cognome,
                                   String numeroDocumento, String dataNascita, String postoAssegnato) {
        String sql = "INSERT INTO tickets (codice_prenotazione, nome, cognome, numero_documento, data_nascita, posto_assegnato) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, numeroDocumento);

            LocalDate dataNascitaConvertita = convertiDataNascita(dataNascita);
            stmt.setDate(5, Date.valueOf(dataNascitaConvertita));

            stmt.setString(6, postoAssegnato);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento ticket: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<String> getTicketPerCodicePrenotazioneEPosto(String codicePrenotazione, String postoAssegnato) {
        String sql = "SELECT * FROM tickets WHERE codice_prenotazione = ? AND posto_assegnato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);
            stmt.setString(2, postoAssegnato);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaArrayListDalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca ticket per prenotazione e posto: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<ArrayList<String>> getTicketsPerPrenotazione(String codicePrenotazione) {
        ArrayList<ArrayList<String>> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE codice_prenotazione = ? ORDER BY posto_assegnato";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tickets.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero tickets per prenotazione: " + e.getMessage());
        }

        return tickets;
    }

    @Override
    public ArrayList<ArrayList<String>> getTicketsPerVolo(String numeroVolo) {
        ArrayList<ArrayList<String>> tickets = new ArrayList<>();
        String sql = "SELECT t.* FROM tickets t JOIN prenotazioni p ON t.codice_prenotazione = p.codice_prenotazione WHERE p.numero_volo = ? ORDER BY t.posto_assegnato";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tickets.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero tickets per volo: " + e.getMessage());
        }

        return tickets;
    }

    @Override
    public ArrayList<ArrayList<String>> getTuttiTickets() {
        ArrayList<ArrayList<String>> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY codice_prenotazione, posto_assegnato";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tickets.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutti i tickets: " + e.getMessage());
        }

        return tickets;
    }

    @Override
    public boolean aggiornaTicket(String codicePrenotazione, String postoAssegnato, String nome,
                                  String cognome, String numeroDocumento, String dataNascita) {
        String sql = "UPDATE tickets SET nome = ?, cognome = ?, numero_documento = ?, data_nascita = ? WHERE codice_prenotazione = ? AND posto_assegnato = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, numeroDocumento);

            LocalDate dataNascitaConvertita = convertiDataNascita(dataNascita);
            stmt.setDate(4, Date.valueOf(dataNascitaConvertita));

            stmt.setString(5, codicePrenotazione);
            stmt.setString(6, postoAssegnato);

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

    // Metodi di utilità
    private ArrayList<String> creaArrayListDalResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> datiTicket = new ArrayList<>();

        datiTicket.add(rs.getString("codice_prenotazione"));    // 0
        datiTicket.add(rs.getString("nome"));                   // 1
        datiTicket.add(rs.getString("cognome"));                // 2
        datiTicket.add(rs.getString("numero_documento"));       // 3
        datiTicket.add(formatDataPerModello(rs.getDate("data_nascita").toLocalDate())); // 4
        datiTicket.add(rs.getString("posto_assegnato"));        // 5

        return datiTicket;
    }

    private LocalDate convertiDataNascita(String dataString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(dataString, formatter);
        } catch (Exception e) {
            System.out.println("Errore nella conversione della data: " + dataString);
            return LocalDate.now();
        }
    }

    private String formatDataPerModello(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formatter);
    }
}