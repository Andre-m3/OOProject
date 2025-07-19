package dao;

import java.util.ArrayList;

public interface PrenotazioneDAO {

    boolean inserisciPrenotazione(String codicePrenotazione, String email, String numeroVolo,
                                  String stato, int numeroPasseggeri);
    ArrayList<String> getPrenotazionePerCodice(String codicePrenotazione);
    ArrayList<ArrayList<String>> getPrenotazioniPerUtente(String email);
    ArrayList<ArrayList<String>> getPrenotazioniPerVolo(String numeroVolo);
    ArrayList<ArrayList<String>> getTuttePrenotazioni();
    boolean aggiornaPrenotazione(String codicePrenotazione, String email, String numeroVolo,
                                 String stato, int numeroPasseggeri);
    boolean aggiornaStatoPrenotazione(String codicePrenotazione, String nuovoStato);
    boolean eliminaPrenotazione(String codicePrenotazione);
    boolean esistePrenotazione(String codicePrenotazione);
}