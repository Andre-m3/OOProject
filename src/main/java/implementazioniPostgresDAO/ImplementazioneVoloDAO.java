package implementazioniPostgresDAO;

import dao.VoloDAO;
import database.ConnessioneDatabase;
// NESSUN IMPORT DAL PACKAGE "MODEL"!
// Rispettiamo il pattern imposto in lezione "BCE + Dao"

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The type Implementazione volo dao.
 */
public class ImplementazioneVoloDAO implements VoloDAO {

    private Connection connection;

    /**
     * Instantiates a new Implementazione volo dao.
     */
    public ImplementazioneVoloDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                                 String data, String stato, String partenza, String destinazione,
                                 Short gateImbarco, String tipoVolo) {
        String sql = "INSERT INTO voli (numero_volo, compagnia_aerea, orario_previsto, data_volo, stato, partenza, destinazione, gate_imbarco, tipo_volo) VALUES (?, ?, ?, ?, ?::stato_volo, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);
            stmt.setString(2, compagniaAerea);

            // Gestione orario
            String orario = orarioPrevisto;
            if (orario.length() == 5) {
                orario += ":00";
            }
            stmt.setTime(3, Time.valueOf(orario));

            // Gestione data
            LocalDate dataConvertita = convertiDataDaStringa(data);
            stmt.setDate(4, Date.valueOf(dataConvertita));

            stmt.setString(5, stato);
            stmt.setString(6, partenza);
            stmt.setString(7, destinazione);

            // Gate imbarco solo per voli in partenza
            if (gateImbarco != null) {
                stmt.setShort(8, gateImbarco);
            } else {
                stmt.setNull(8, Types.SMALLINT);
            }
            stmt.setString(9, tipoVolo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento volo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<String> getVoloPerNumero(String numeroVolo) {
        String sql = "SELECT * FROM voli WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaArrayListDalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca volo per numero: " + e.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<ArrayList<String>> getTuttiVoli() {
        ArrayList<ArrayList<String>> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                voli.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutti i voli: " + e.getMessage());
        }

        return voli;
    }

    @Override
    public ArrayList<ArrayList<String>> getVoliDisponibili() {
        ArrayList<ArrayList<String>> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli WHERE stato = 'PROGRAMMATO' ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                voli.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero voli disponibili: " + e.getMessage());
        }

        return voli;
    }

    @Override
    public boolean aggiornaVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                                String data, String stato, String partenza, String destinazione,
                                Short gateImbarco, int ritardo) {

        String sql = "UPDATE voli SET compagnia_aerea = ?, orario_previsto = ?, data_volo = ?, stato = ?::stato_volo, partenza = ?, destinazione = ?, gate_imbarco = ?, ritardo = ? WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, compagniaAerea);

            String orario = orarioPrevisto;
            if (orario.length() == 5) {
                orario += ":00";
            }
            stmt.setTime(2, Time.valueOf(orario));

            LocalDate dataConvertita = convertiDataDaStringa(data);
            stmt.setDate(3, Date.valueOf(dataConvertita));

            stmt.setString(4, stato);
            stmt.setString(5, partenza);
            stmt.setString(6, destinazione);

            if (gateImbarco != null) {
                stmt.setShort(7, gateImbarco);
            } else {
                stmt.setNull(7, Types.SMALLINT);
            }

            stmt.setInt(8, ritardo);
            stmt.setString(9, numeroVolo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento volo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiornaStatoVolo(String numeroVolo, String nuovoStato) {
        String sql = "UPDATE voli SET stato = ?::stato_volo WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setString(2, numeroVolo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento stato volo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate) {
        String sql = "UPDATE voli SET gate_imbarco = ? WHERE numero_volo = ? AND tipo_volo = 'PARTENZA'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (nuovoGate != null) {
                stmt.setShort(1, nuovoGate);
            } else {
                stmt.setNull(1, Types.SMALLINT);
            }
            stmt.setString(2, numeroVolo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento gate imbarco: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminaVolo(String numeroVolo) {
        String sql = "DELETE FROM voli WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione volo: " + e.getMessage());
            return false;
        }
    }

    private ArrayList<String> creaArrayListDalResultSet(ResultSet rs) throws SQLException {
        ArrayList<String> datiVolo = new ArrayList<>();

        datiVolo.add(rs.getString("numero_volo"));
        datiVolo.add(rs.getString("compagnia_aerea"));
        datiVolo.add(rs.getTime("orario_previsto").toString());
        datiVolo.add(formatDataPerModello(rs.getDate("data_volo").toLocalDate()));
        datiVolo.add(String.valueOf(rs.getInt("ritardo")));
        datiVolo.add(rs.getString("stato"));
        datiVolo.add(rs.getString("partenza"));
        datiVolo.add(rs.getString("destinazione"));

        // Gate imbarco (può essere null)
        Short gateImbarco = rs.getShort("gate_imbarco");
        if (rs.wasNull()) {
            datiVolo.add(null);
        } else {
            datiVolo.add(String.valueOf(gateImbarco));
        }

        datiVolo.add(rs.getString("tipo_volo"));

        return datiVolo;
    }

    private LocalDate convertiDataDaStringa(String dataString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dataString, inputFormatter);
    }

    private String formatDataPerModello(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formatter);
    }

    @Override
    public ArrayList<ArrayList<String>> getVoliPerCittaPartenza(String cittaPartenza) {
        ArrayList<ArrayList<String>> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli WHERE LOWER(partenza) = LOWER(?) ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cittaPartenza);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                voli.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero voli per città di partenza: " + e.getMessage());
        }

        return voli;
    }

    @Override
    public ArrayList<ArrayList<String>> getVoliPerCittaDestinazione(String cittaDestinazione) {
        ArrayList<ArrayList<String>> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli WHERE LOWER(destinazione) = LOWER(?) ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cittaDestinazione);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                voli.add(creaArrayListDalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero voli per città di destinazione: " + e.getMessage());
        }

        return voli;
    }

}