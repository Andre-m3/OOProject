package implementazioniPostgresDAO;

import dao.PrenotazioneDAO;
import database.ConnessioneDatabase;
import model.Prenotazione;
import model.StatoPrenotazione;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public boolean inserisciPrenotazione(Prenotazione prenotazione) {
        String sql = "INSERT INTO prenotazioni (codice_prenotazione, email, numero_volo, data_volo, partenza, destinazione, stato, numero_passeggeri) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, prenotazione.getCodicePrenotazione());
            stmt.setString(2, prenotazione.getUsername());
            stmt.setString(3, prenotazione.getCodiceVolo());
            stmt.setDate(4, Date.valueOf(prenotazione.getDataVolo()));
            stmt.setString(5, prenotazione.getPartenzaDestinazione());
            stmt.setString(5, prenotazione.getPartenzaDestinazione());
            stmt.setString(6, prenotazione.getStato().toString());
            stmt.setInt(7, prenotazione.getNumeroPasseggeri());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento prenotazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Prenotazione getPrenotazionePerCodice(String codicePrenotazione) {
        String sql = "SELECT * FROM prenotazioni WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codicePrenotazione);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaPrenotazioneDalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca prenotazione per codice: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Prenotazione> getPrenotazioniPerUtente(String username) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni WHERE username = ? ORDER BY data_volo DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                prenotazioni.add(creaPrenotazioneDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero prenotazioni per utente: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public List<Prenotazione> getPrenotazioniPerVolo(String numeroVolo) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni WHERE numero_volo = ? ORDER BY created_at";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                prenotazioni.add(creaPrenotazioneDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero prenotazioni per volo: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public List<Prenotazione> getTuttePrenotazioni() {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni ORDER BY data_volo DESC, created_at DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                prenotazioni.add(creaPrenotazioneDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutte le prenotazioni: " + e.getMessage());
        }

        return prenotazioni;
    }

    @Override
    public boolean aggiornaPrenotazione(Prenotazione prenotazione) {
        String sql = "UPDATE prenotazioni SET numero_volo = ?, data_volo = ?, tratta = ?, stato = ?, numero_passeggeri = ? WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, prenotazione.getCodiceVolo());
            stmt.setDate(2, Date.valueOf(prenotazione.getDataVolo()));
            stmt.setString(3, prenotazione.getTratta());
            stmt.setString(4, prenotazione.getStato().toString());
            stmt.setInt(5, prenotazione.getNumeroPasseggeri());
            stmt.setString(6, prenotazione.getCodicePrenotazione());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento prenotazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiornaStatoPrenotazione(String codicePrenotazione, StatoPrenotazione nuovoStato) {
        String sql = "UPDATE prenotazioni SET stato = ? WHERE codice_prenotazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato.toString());
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
        // Prima eliminiamo i ticket associati alla prenotazione
        String sqlTickets = "DELETE FROM tickets WHERE codice_prenotazione = ?";
        String sqlPrenotazione = "DELETE FROM prenotazioni WHERE codice_prenotazione = ?";

        try {
            // Iniziamo una transazione
            connection.setAutoCommit(false);

            // Eliminiamo prima i tickets
            try (PreparedStatement stmtTickets = connection.prepareStatement(sqlTickets)) {
                stmtTickets.setString(1, codicePrenotazione);
                stmtTickets.executeUpdate();
            }

            // Poi eliminiamo la prenotazione
            try (PreparedStatement stmtPrenotazione = connection.prepareStatement(sqlPrenotazione)) {
                stmtPrenotazione.setString(1, codicePrenotazione);
                int rowsAffected = stmtPrenotazione.executeUpdate();

                // Confermiamo la transazione
                connection.commit();
                connection.setAutoCommit(true);

                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            try {
                // Rollback in caso di errore
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

    // Metodo di utilità per creare un oggetto Prenotazione dal ResultSet
    private Prenotazione creaPrenotazioneDalResultSet(ResultSet rs) throws SQLException {
        String codicePrenotazione = rs.getString("codice_prenotazione");
        String numeroVolo = rs.getString("numero_volo");
        LocalDate dataVolo = rs.getDate("data_volo").toLocalDate();
        String tratta = rs.getString("tratta");
        StatoPrenotazione stato = StatoPrenotazione.valueOf(rs.getString("stato"));
        int numeroPasseggeri = rs.getInt("numero_passeggeri");
        String username = rs.getString("username");

        return new Prenotazione(codicePrenotazione, numeroVolo, stato, numeroPasseggeri, username);
    }

}