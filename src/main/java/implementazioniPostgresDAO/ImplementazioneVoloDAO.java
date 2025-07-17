package implementazioniPostgresDAO;

import dao.VoloDAO;
import database.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ImplementazioneVoloDAO implements VoloDAO{

    private Connection connection;

    public ImplementazioneVoloDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean inserisciVolo(Volo volo) {
        String sql = "INSERT INTO voli (numero_volo, compagnia_aerea, orario_previsto, data_volo, stato, partenza, destinazione, gate_imbarco, tipo_volo) VALUES (?, ?, ?, ?, ?::stato_volo, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, volo.getNumeroVolo());
            stmt.setString(2, volo.getCompagniaAerea());

            // Dobbiamo gestire l'orario. Ci viene chiesto di passare anche i secondi (informazione pero omessa dall'amministratore in fase di inserimento!)
            String orario = volo.getOrarioPrevisto();
            if (orario.length() == 5) {     // formato HH:MM
                orario += ":00";            // diventa HH:MM:SS
            }
            stmt.setTime(3, Time.valueOf(orario));      // Passiamo quindi l'orario comprensivo di "secondi" (--> formato hh.mm.ss nel database)

            // Anche la data va osservata. Dobbiamo passare un formato del tipo "YYYY-MM-DD".
            // Si potrebbe gestire diversamente, modificando il "datestyle" di postgres. Però per la nostra applicazione si è preferito un approccio base.
            // Di conseguenza, siccome l'utente ha inserito una data "DD-MM-YYYY" (per facilitare l'input), noi andremo a riconvertirla nel formato "YYYY-MM-DD"!
            String dataString = volo.getData();
            LocalDate dataConvertita = convertiDataDaStringa(dataString);
            stmt.setDate(4, Date.valueOf(dataConvertita));

            stmt.setString(5, volo.getStato().toString());
            stmt.setString(6, volo.getPartenza());
            stmt.setString(7, volo.getDestinazione());

            // Gate imbarco solo per voli in partenza (gestito anche in caso non venga inserito alcun gate inizialmente)
            if (volo instanceof VoloInPartenza) {
                Short gateImbarco = ((VoloInPartenza) volo).getGateImbarco();
                if (gateImbarco != null) {
                    stmt.setShort(8, gateImbarco);
                } else {
                    stmt.setNull(8, Types.SMALLINT);
                }
                stmt.setString(9, "PARTENZA");
            } else {
                stmt.setNull(8, Types.SMALLINT);
                stmt.setString(9, "ARRIVO");
            }


            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento volo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Volo getVoloPerNumero(String numeroVolo) {
        String sql = "SELECT * FROM voli WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return creaVolodalResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la ricerca volo per numero: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Volo> getTuttiVoli() {
        List<Volo> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                voli.add(creaVolodalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero di tutti i voli: " + e.getMessage());
        }

        return voli;
    }

    @Override
    public List<Volo> getVoliDisponibili() {
        List<Volo> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli WHERE stato = 'PROGRAMMATO' ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                voli.add(creaVolodalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero voli disponibili: " + e.getMessage());
        }

        return voli;
    }

    @Override
    public List<Volo> getVoliPerStato(StatoVolo stato) {
        List<Volo> voli = new ArrayList<>();
        String sql = "SELECT * FROM voli WHERE stato = ? ORDER BY data_volo, orario_previsto";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, stato.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                voli.add(creaVolodalResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Errore durante il recupero voli per stato: " + e.getMessage());
        }

        return voli;
    }

    @Override
    public boolean aggiornaVolo(Volo volo) {
        String sql = "UPDATE voli SET compagnia_aerea = ?, orario_previsto = ?, data_volo = ?, stato = ?::stato_volo, partenza = ?, destinazione = ?, gate_imbarco = ? WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, volo.getCompagniaAerea());
            // Notiamo le stesse problematiche viste prima nel metodo "inserisci volo" (linee di codice copiate, commenti compresi)
            // Dobbiamo gestire l'orario. Ci viene chiesto di passare anche i secondi (informazione pero omessa dall'amministratore in fase di inserimento!)
            String orario = volo.getOrarioPrevisto();
            if (orario.length() == 5) {         // formato HH:MM
                orario += ":00";                // diventa HH:MM:SS
            }
            stmt.setTime(2, Time.valueOf(orario));          // Passiamo quindi l'orario comprensivo di "secondi" (--> formato hh.mm.ss)

            String dataString = volo.getData();
            LocalDate dataConvertita = convertiDataDaStringa(dataString);
            stmt.setDate(3, Date.valueOf(dataConvertita));

            stmt.setString(4, volo.getStato().toString());
            stmt.setString(5, volo.getPartenza());
            stmt.setString(6, volo.getDestinazione());

            if (volo instanceof VoloInPartenza) {
                Short gateImbarco = ((VoloInPartenza) volo).getGateImbarco();
                if (gateImbarco != null) {
                    stmt.setShort(7, gateImbarco);
                } else {
                    stmt.setNull(7, Types.SMALLINT);
                }
            } else {
                stmt.setNull(7, Types.SMALLINT);
            }

            stmt.setString(8, volo.getNumeroVolo());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento volo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiornaStatoVolo(String numeroVolo, StatoVolo nuovoStato) {
        String sql = "UPDATE voli SET stato = ?::stato_volo WHERE numero_volo = ?";     // abbiamo effettuato cast esplicito per passare un valore di tipo stato_volo (enum presente anche nel database)

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato.toString());
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
            stmt.setShort(1, nuovoGate);
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

    @Override
    public boolean esisteVolo(String numeroVolo) {
        String sql = "SELECT COUNT(*) FROM voli WHERE numero_volo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeroVolo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Errore durante la verifica esistenza volo: " + e.getMessage());
        }

        return false;
    }

    // Metodo di utilità per creare un oggetto Volo dal ResultSet
    private Volo creaVolodalResultSet(ResultSet rs) throws SQLException {
        String numeroVolo = rs.getString("numero_volo");
        String compagniaAerea = rs.getString("compagnia_aerea");

        // Gestiamo l'orario (formato TIME)
        Time orarioTime = rs.getTime("orario_previsto");
        String orarioPrevisto = orarioTime.toLocalTime().toString().substring(0, 5);        //HH:MM (quindi 5) substring essere sicuri di non star prendendo valori "errati"

        // Gestiamo la data (formato DATE)
        Date dataVolo = rs.getDate("data_volo");
        String dataFormattata = formatDataPerModello(dataVolo.toLocalDate());

        int ritardo = rs.getInt("ritardo");
        StatoVolo stato = StatoVolo.valueOf(rs.getString("stato"));
        String partenza = rs.getString("partenza");
        String destinazione = rs.getString("destinazione");
        String tipoVolo = rs.getString("tipo_volo");

        if ("PARTENZA".equals(tipoVolo)) {
            Short gateImbarco = rs.getShort("gate_imbarco");
            if (rs.wasNull()) {
                gateImbarco = null;
            }

            return new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
                    dataFormattata, ritardo, stato, destinazione, gateImbarco);
        } else {

            return new VoloInArrivo(numeroVolo, compagniaAerea, orarioPrevisto,
                    dataFormattata, ritardo, stato, partenza);
        }

    }



    /**
     * Formatta una LocalDate nel formato DD-MM-YYYY per il modello
     * @param data LocalDate da formattare
     * @return Stringa nel formato DD-MM-YYYY
     */
    private String formatDataPerModello(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formatter);
    }

    /**
     * Converte una stringa di data dal formato DD-MM-YYYY a LocalDate
     * @param dataString Data in formato DD-MM-YYYY
     * @return LocalDate convertita
     */
    private LocalDate convertiDataDaStringa(String dataString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(dataString, formatter);
        } catch (Exception e) {
            System.out.println("Errore nel parsing della data: " + dataString);
            return LocalDate.now(); // Fallback
        }
    }



}