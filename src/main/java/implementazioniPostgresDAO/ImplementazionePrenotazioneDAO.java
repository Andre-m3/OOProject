package implementazioniPostgresDAO;

import dao.PrenotazioneDAO;
import database.ConnessioneDatabase;
// NESSUN IMPORT DAL MODEL!

import java.sql.*;
import java.util.ArrayList;

public class ImplementazionePrenotazioneDAO implements PrenotazioneDAO {

    private Connection connection;

    public ImplementazionePrenotazioneDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
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
            System.out.println("Errore durante l'inserimento prenotazione: " + e.getMessage());
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
            System.out.println("Errore durante la ricerca prenotazione per codice: " + e.getMessage());
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
            System.out.println("Errore durante il recupero prenotazioni per utente: " + e.getMessage());
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
            System.out.println("Errore durante il recupero prenotazioni per volo: " + e.getMessage());
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
            System.out.println("Errore durante il recupero di tutte le prenotazioni: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public boolean aggiornaPrenotazione(String codicePrenotazione, String email, String numeroVolo,
                                        String stato, int numeroPasseggeri) {
        String sql = "UPDATE prenotazioni SET email = ?, numero_volo = ?, stato_prenotazione = ?::stato_prenotazione, numero_passeggeri = ? WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, numeroVolo);
            stmt.setString(3, stato);
            stmt.setInt(4, numeroPasseggeri);
            stmt.setString(5, codicePrenotazione);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento prenotazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiornaStatoPrenotazione(String codicePrenotazione, String nuovoStato) {
        String sql = "UPDATE prenotazioni SET stato_prenotazione = ?::stato_prenotazione WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setString(2, codicePrenotazione);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento stato prenotazione: " + e.getMessage());
            return false;
        }
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
                System.out.println("Errore durante il rollback: " + rollbackEx.getMessage());
            }
            System.out.println("Errore durante l'eliminazione prenotazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean esistePrenotazione(String codicePrenotazione) {
        String sql = "SELECT COUNT(*) FROM prenotazioni WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la verifica esistenza prenotazione: " + e.getMessage());
        }

        return false;
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