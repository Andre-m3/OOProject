package dao;

import java.util.ArrayList;

public interface VoloDAO {

    boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, String stato, String partenza, String destinazione,
                          Short gateImbarco, String tipoVolo);
    ArrayList<String> getVoloPerNumero(String numeroVolo);
    ArrayList<ArrayList<String>> getTuttiVoli();
    ArrayList<ArrayList<String>> getVoliDisponibili();
    ArrayList<ArrayList<String>> getVoliPerCittaPartenza(String cittaPartenza);
    ArrayList<ArrayList<String>> getVoliPerCittaDestinazione(String cittaDestinazione);
    boolean aggiornaVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                         String data, String stato, String partenza, String destinazione,
                         Short gateImbarco, int ritardo);
    boolean aggiornaStatoVolo(String numeroVolo, String nuovoStato);        // Necessario a implementazioni attuali e future (e miglioramenti)
    boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate);
    boolean eliminaVolo(String numeroVolo);

}