package dao;

import java.util.ArrayList;

/**
 * The interface Volo dao.
 */
public interface VoloDAO {

    /**
     * Inserisci volo boolean.
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param stato          the stato
     * @param partenza       the partenza
     * @param destinazione   the destinazione
     * @param gateImbarco    the gate imbarco
     * @param tipoVolo       the tipo volo
     * @return the boolean
     */
    boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, String stato, String partenza, String destinazione,
                          Short gateImbarco, String tipoVolo);

    /**
     * Gets volo per numero.
     *
     * @param numeroVolo the numero volo
     * @return the volo per numero
     */
    ArrayList<String> getVoloPerNumero(String numeroVolo);

    /**
     * Gets tutti voli.
     *
     * @return the tutti voli
     */
    ArrayList<ArrayList<String>> getTuttiVoli();

    /**
     * Gets voli disponibili.
     *
     * @return the voli disponibili
     */
    ArrayList<ArrayList<String>> getVoliDisponibili();

    /**
     * Gets voli per citta partenza.
     *
     * @param cittaPartenza the citta partenza
     * @return the voli per citta partenza
     */
    ArrayList<ArrayList<String>> getVoliPerCittaPartenza(String cittaPartenza);

    /**
     * Gets voli per citta destinazione.
     *
     * @param cittaDestinazione the citta destinazione
     * @return the voli per citta destinazione
     */
    ArrayList<ArrayList<String>> getVoliPerCittaDestinazione(String cittaDestinazione);

    /**
     * Aggiorna volo boolean.
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param stato          the stato
     * @param partenza       the partenza
     * @param destinazione   the destinazione
     * @param gateImbarco    the gate imbarco
     * @param ritardo        the ritardo
     * @return the boolean
     */
    boolean aggiornaVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                         String data, String stato, String partenza, String destinazione,
                         Short gateImbarco, int ritardo);

    /**
     * Aggiorna stato volo boolean.
     *
     * @param numeroVolo the numero volo
     * @param nuovoStato the nuovo stato
     * @return the boolean
     */
    boolean aggiornaStatoVolo(String numeroVolo, String nuovoStato);        // Necessario a implementazioni attuali e future (e miglioramenti)

    /**
     * Aggiorna gate imbarco boolean.
     *
     * @param numeroVolo the numero volo
     * @param nuovoGate  the nuovo gate
     * @return the boolean
     */
    boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate);

    /**
     * Elimina volo boolean.
     *
     * @param numeroVolo the numero volo
     * @return the boolean
     */
    boolean eliminaVolo(String numeroVolo);

}