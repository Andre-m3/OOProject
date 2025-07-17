package implementazioniPostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.Utente;
import model.UtenteGenerico;
import model.Amministratore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImplementazioneUtenteDAO implements UtenteDAO {

    private Connection connection;

    public ImplementazioneUtenteDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean inserisciUtente(Utente utente) {
        String sql = "INSERT INTO utenti (username, email, password, is_admin) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getEmail());
            stmt.setString(3, utente.getPassword());
            stmt.setBoolean(4, utente instanceof Amministratore);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento utente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Utente getUtentePerCredenziali(String emailUsername, String password) {
        String sql = "SELECT * FROM utenti WHERE (username = ? OR email = ?) AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emailUsername);
            stmt.setString(2, emailUsername);
            stmt.setString(3, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaUtentedalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca utente per credenziali: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Utente getUtentePerUsername(String username) {
        String sql = "SELECT * FROM utenti WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaUtentedalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca utente per username: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Utente getUtentePerEmail(String email) {
        String sql = "SELECT * FROM utenti WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaUtentedalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca utente per email: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean esisteUtente(String username, String email) {
        String sql = "SELECT COUNT(*) FROM utenti WHERE username = ? OR email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la verifica esistenza utente: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean aggiornaUtente(Utente utente) {
        String sql = "UPDATE utenti SET email = ?, password = ?, is_admin = ? WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utente.getEmail());
            stmt.setString(2, utente.getPassword());
            stmt.setBoolean(3, utente instanceof Amministratore);
            stmt.setString(4, utente.getUsername());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento utente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminaUtente(String username) {
        String sql = "DELETE FROM utenti WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione utente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Utente> getTuttiUtenti() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM utenti ORDER BY username";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                utenti.add(creaUtentedalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutti gli utenti: " + e.getMessage());
        }

        return utenti;
    }

    // Metodo di utilità per creare un oggetto Utente dal ResultSet
    private Utente creaUtentedalResultSet(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        boolean isAdmin = rs.getBoolean("is_admin");

        if (isAdmin)
            return new Amministratore(email, username, password);
        else
            return new UtenteGenerico(email, username, password);
    }
}