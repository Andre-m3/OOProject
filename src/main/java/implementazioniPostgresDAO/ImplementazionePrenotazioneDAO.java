package implementazioniPostgresDAO;

import dao.PrenotazioneDAO;
import database.ConnessioneDatabase;
// NESSUN IMPORT DAL PACKAGE "MODEL"!
// Rispettiamo il pattern imposto in lezione "BCE + Dao"

// Utilizziamo un logger per gestire gli output a schermo secondo quanto consigliato da Sonarqube
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;

/**
 * The type Implementazione prenotazione dao.
 */
public class ImplementazionePrenotazioneDAO implements PrenotazioneDAO {

    private Connection connection;
    Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Instantiates a new Implementazione prenotazione dao.
     */
    public ImplementazionePrenotazioneDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            logger.warning("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean inserisciPrenotazione(String codicePrenotazione, String email, String numeroVolo,
                                         String stato, int numeroPasseggeri) {
        String sql = "INSERT INTO prenotazioni (codice_prenotazione, email, numero_volo, stato_prenotazione, numero_passeggeri) VALUES (?, ?, ?, ?::stato_prenotazione, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);
            stmt.setString(2, email);
            stmt.setString(3, numeroVolo);
            stmt.setString(4, stato);
            stmt.setInt(5, numeroPasseggeri);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.warning("Errore durante l'inserimento prenotazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<String> getPrenotazionePerCodice(String codicePrenotazione) {
        String sql = "SELECT * FROM prenotazioni WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaArrayListDalResultSet(rs);
            }

        } catch (SQLException e) {
            logger.warning("Errore durante la ricerca prenotazione per codice: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<ArrayList<String>> getPrenotazioniPerUtente(String email) {
        ArrayList<ArrayList<String>> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni WHERE email = ? ORDER BY codice_prenotazione DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                prenotazioni.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            logger.warning("Errore durante il recupero prenotazioni per utente: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public ArrayList<ArrayList<String>> getPrenotazioniPerVolo(String numeroVolo) {
        ArrayList<ArrayList<String>> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni WHERE numero_volo = ? ORDER BY codice_prenotazione";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                prenotazioni.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            logger.warning("Errore durante il recupero prenotazioni per volo: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public ArrayList<ArrayList<String>> getTuttePrenotazioni() {
        ArrayList<ArrayList<String>> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni ORDER BY codice_prenotazione DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                prenotazioni.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            logger.warning("Errore durante il recupero di tutte le prenotazioni: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public boolean eliminaPrenotazione(String codicePrenotazione) {
        String sqlTickets = "DELETE FROM tickets WHERE codice_prenotazione = ?";
        String sqlPrenotazione = "DELETE FROM prenotazioni WHERE codice_prenotazione = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtTickets = connection.prepareStatement(sqlTickets)) {
                stmtTickets.setString(1, codicePrenotazione);
                stmtTickets.executeUpdate();
            }

            try (PreparedStatement stmtPrenotazione = connection.prepareStatement(sqlPrenotazione)) {
                stmtPrenotazione.setString(1, codicePrenotazione);
                int rowsAffected = stmtPrenotazione.executeUpdate();

                connection.commit();
                connection.setAutoCommit(true);

                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                logger.warning("Errore durante il rollback: " + rollbackEx.getMessage());
            }
            logger.warning("Errore durante l'eliminazione prenotazione: " + e.getMessage());
            return false;
        }
    }

    // Metodo di utilità per creare un ArrayList dal ResultSet
    private ArrayList<String> creaArrayListDalResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> datiPrenotazione = new ArrayList<>();

        datiPrenotazione.add(rs.getString("codice_prenotazione"));
        datiPrenotazione.add(rs.getString("email"));
        datiPrenotazione.add(rs.getString("numero_volo"));
        datiPrenotazione.add(rs.getString("stato_prenotazione"));
        datiPrenotazione.add(String.valueOf(rs.getInt("numero_passeggeri")));

        return datiPrenotazione;
    }
}