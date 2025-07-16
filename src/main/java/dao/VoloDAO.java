package dao;

import model.Volo;
import model.StatoVolo;
import java.util.List;

public interface VoloDAO {

    boolean inserisciVolo(Volo volo);
    Volo getVoloPerNumero(String numeroVolo);
    List<Volo> getTuttiVoli();
    List<Volo> getVoliDisponibili();
    List<Volo> getVoliPerStato(StatoVolo stato);
    boolean aggiornaVolo(Volo volo);
    boolean aggiornaStatoVolo(String numeroVolo, StatoVolo nuovoStato);
    boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate);
    boolean eliminaVolo(String numeroVolo);
    boolean esisteVolo(String numeroVolo);
}