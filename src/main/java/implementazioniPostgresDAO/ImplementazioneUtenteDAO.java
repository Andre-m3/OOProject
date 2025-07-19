package implementazioniPostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
// NESSUN IMPORT DAL MODEL!

import java.sql.*;
import java.util.ArrayList;

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
    public boolean inserisciUtente(String email, String username, String password, boolean isAdmin) {
        String sql = "INSERT INTO utenti (username, email, password, is_admin) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setBoolean(4, isAdmin);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento utente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<String> getUtentePerCredenziali(String emailUsername, String password) {
        String sql = "SELECT * FROM utenti WHERE (username = ? OR email = ?) AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emailUsername);
            stmt.setString(2, emailUsername);
            stmt.setString(3, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaArrayListDalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca utente per credenziali: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<String> getUtentePerUsername(String username) {
        String sql = "SELECT * FROM utenti WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaArrayListDalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca utente per username: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<String> getUtentePerEmail(String email) {
        String sql = "SELECT * FROM utenti WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaArrayListDalResultSet(rs);
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
    public boolean aggiornaUtente(String username, String email, String password, boolean isAdmin) {
        String sql = "UPDATE utenti SET email = ?, password = ?, is_admin = ? WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setBoolean(3, isAdmin);
            stmt.setString(4, username);

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
    public ArrayList<ArrayList<String>> getTuttiUtenti() {
        ArrayList<ArrayList<String>> utenti = new ArrayList<>();
        String sql = "SELECT * FROM utenti ORDER BY username";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                utenti.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutti gli utenti: " + e.getMessage());
        }

        return utenti;
    }

    // Metodo di utilità per creare un ArrayList dal ResultSet
    private ArrayList<String> creaArrayListDalResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> datiUtente = new ArrayList<>();

        datiUtente.add(rs.getString("username"));       // 0
        datiUtente.add(rs.getString("email"));          // 1
        datiUtente.add(rs.getString("password"));       // 2
        datiUtente.add(String.valueOf(rs.getBoolean("is_admin"))); // 3

        return datiUtente;
    }
}