package dao;

import model.Prenotazione;
import model.StatoPrenotazione;
import java.util.List;

public interface PrenotazioneDAO {

    boolean inserisciPrenotazione(Prenotazione prenotazione);
    Prenotazione getPrenotazionePerCodice(String codicePrenotazione);
    List<Prenotazione> getPrenotazioniPerUtente(String username);
    List<Prenotazione> getPrenotazioniPerVolo(String numeroVolo);
    List<Prenotazione> getTuttePrenotazioni();
    boolean aggiornaPrenotazione(Prenotazione prenotazione);
    boolean aggiornaStatoPrenotazione(String codicePrenotazione, StatoPrenotazione nuovoStato);
    boolean eliminaPrenotazione(String codicePrenotazione);
    boolean esistePrenotazione(String codicePrenotazione);
}