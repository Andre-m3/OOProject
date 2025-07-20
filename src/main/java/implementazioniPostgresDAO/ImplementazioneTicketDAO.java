package implementazioniPostgresDAO;

import dao.TicketDAO;
import database.ConnessioneDatabase;
// NESSUN IMPORT DAL PACKAGE "MODEL"!
// Rispettiamo il pattern imposto in lezione "BCE + Dao"

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The type Implementazione ticket dao.
 */
public class ImplementazioneTicketDAO implements TicketDAO {

    private Connection connection;

    /**
     * Instantiates a new Implementazione ticket dao.
     */
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

    private ArrayList<String> creaArrayListDalResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> datiTicket = new ArrayList<>();

        datiTicket.add(rs.getString("nome"));
        datiTicket.add(rs.getString("cognome"));
        datiTicket.add(rs.getString("numero_documento"));

        Date dataNascitaDB = rs.getDate("data_nascita");
        if (dataNascitaDB != null) {
            LocalDate dataLocalDate = dataNascitaDB.toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            datiTicket.add(dataLocalDate.format(formatter));
        } else {
            datiTicket.add("");
        }

        datiTicket.add(rs.getString("posto_assegnato"));
        datiTicket.add(rs.getString("codice_prenotazione"));

        return datiTicket;
    }


    // Metodo di utilità
    private LocalDate convertiDataNascita(String dataString) {
        try {
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            try {
                return LocalDate.parse(dataString, formatter1);
            } catch (Exception e) {
                return LocalDate.parse(dataString, formatter2);
            }
        } catch (Exception e) {
            System.out.println("Errore nella conversione della data: " + dataString);
            return LocalDate.now();
        }
    }

}