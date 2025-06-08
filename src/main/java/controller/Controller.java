package controller;

import model.*;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller che fa da intermediario tra il pacchetto GUI e il pacchetto model.
 * Viene utilizzato il pattern 'Singleton' per garantire una singola istanza.
 */
public class Controller {

    private static Controller instance = null;      // Singleton instance
    private Utente utenteLoggato;                   // Utente corrente
    private ArrayList<Utente> utentiRegistrati;     // Lista degli utenti registrati

    /**
     * Costruttore privato per il pattern
     */
    public Controller() {
        // Inizializzazione di una lista provvisoria per creare due utenti registrati di default
        utentiRegistrati = new ArrayList<>();

        // Creazione utenti di default per testing
        UtenteGenerico utente = new UtenteGenerico(
                "user@example.com",
                "user",
                "password123"
        );

        Amministratore admin = new Amministratore(
                "admin@example.com",
                "admin",
                "admin123"
        );

        utentiRegistrati.add(utente);
        utentiRegistrati.add(admin);

        // Aggiuntai di alcuni voli per testing
        Volo.aggiungiVolo(new Volo("AZ123", "Alitalia", "14:30", "10/06/2025", 0, "IN ORARIO", "PARTENZA"));
        Volo.aggiungiVolo(new Volo("FR456", "Ryanair", "16:45", "11/06/2025", 15, "RITARDO", "PARTENZA"));
        Volo.aggiungiVolo(new Volo("LH789", "Lufthansa", "09:15", "12/06/2025", 0, "IN ORARIO", "ARRIVO"));
        Volo.aggiungiVolo(new Volo("BA101", "British Airways", "18:20", "13/06/2025", 0, "IN ORARIO", "PARTENZA"));
    }

    /**
     * Restituisce l'istanza singleton del controller
     * @return L'istanza del controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Effettua il login di un utente
     * @param emailUsername Username dell'utente
     * @param password Password dell'utente
     * @return true se il login è avvenuto con successo, false altrimenti
     */
    public boolean login(String emailUsername, String password) {
        for (Utente utente : utentiRegistrati) {
            if (utente.getUsername().equals(emailUsername) || utente.getEmail().equals(emailUsername)) {
                // Verifica la password
                if (utente.verificaPassword(password)) {
                    utenteLoggato = utente;
                    return true;
                } else {
                    // Password errata
                    return false;
                }
            }
        }

        // Utente non trovato
        return false;
    }

    /**
     * Effettua il logout dell'utente corrente
     */
    public void logout() {
        utenteLoggato = null;
    }

    /**
     * Registra un nuovo utente
     * @param email Email del nuovo utente
     * @param username Username del nuovo utente
     * @param password Password del nuovo utente
     * @param isAdmin true se l'utente è un amministratore, false altrimenti
     * @return true se la registrazione è avvenuta con successo, false altrimenti
     */
    public boolean registraUtente(String email, String username, String password, boolean isAdmin) {
        // Controlla se l'utente esiste già
        for (Utente utente : utentiRegistrati) {
            if (utente.getUsername().equals(username) || utente.getEmail().equals(email)) {
                return false;       // Username o Email gia utilizzati!
            }
        }

        // Crea il nuovo utente
        Utente nuovoUtente;
        if (isAdmin) {      // Vediamo se si tratta di un nuovo admin o un nuovo user
            nuovoUtente = new Amministratore(email, username, password);
        } else {
            nuovoUtente = new UtenteGenerico(email, username, password);
        }

        // Aggiungi l'utente alla lista
        utentiRegistrati.add(nuovoUtente);
        return true;
    }

    /**
     * Restituisce l'utente attualmente loggato
     * @return L'utente loggato o null se nessun utente è loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Verifica se l'utente loggato è un amministratore
     * @return true se l'utente è un amministratore, false altrimenti
     */
    public boolean isUtenteAdmin() {
        return utenteLoggato instanceof Amministratore;
    }

    /**
     * Restituisce la lista di tutti i voli disponibili
     * @return Lista di voli
     */
    public ArrayList<Volo> getListaVoli() {
        return Volo.getListaVoli();
    }

    /**
     * Inserisce un nuovo volo (disponibile solo per amministratori)
     * @param numeroVolo Numero del volo
     * @param compagniaAerea Compagnia aerea
     * @param orarioPrevisto Orario previsto
     * @param data Data del volo
     * @param stato Stato del volo
     * @param tipoVolo Tipo di volo (partenza/arrivo)
     * @param datiSpecifici Dati specifici in base al tipo di volo
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    public boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                                 String data, String stato, String tipoVolo, Object... datiSpecifici) {
        if (!isUtenteAdmin()) {
            return false;
        }

        try {
            // Verifica che lo stato sia valido
            StatoVolo statoVolo = StatoVolo.valueOf(stato.toUpperCase());

            // Il ritardo iniziale è sempre 0
            int ritardo = 0;

            // Crea il volo in base al tipo
            if (tipoVolo.equalsIgnoreCase("Partenza")) {
                // Per i voli in partenza, i datiSpecifici sono:
                // - aeroportoDestinazione (String) (obbligatorio)
                // - gateImbarco (Short) (opzionale)
                String aeroportoDestinazione = (String) datiSpecifici[0];

                if (datiSpecifici.length > 1 && datiSpecifici[1] != null) {
                    // Con gate specificato
                    Short gateImbarco = (Short) datiSpecifici[1];
                    VoloInPartenza volo = new VoloInPartenza(
                            numeroVolo, compagniaAerea, orarioPrevisto, data,
                            ritardo, statoVolo.toString(), tipoVolo,
                            gateImbarco, aeroportoDestinazione
                    );
                    Volo.aggiungiVolo(volo);
                } else {
                    // Senza gate specificato
                    VoloInPartenza volo = new VoloInPartenza(
                            numeroVolo, compagniaAerea, orarioPrevisto, data,
                            ritardo, statoVolo.toString(), tipoVolo,
                            aeroportoDestinazione
                    );
                    Volo.aggiungiVolo(volo);
                }
            } else if (tipoVolo.equalsIgnoreCase("Arrivo")) {
                // Per i voli in arrivo, i datiSpecifici sono:
                // - aeroportoOrigine (String) (obbligatorio)
                String aeroportoOrigine = (String) datiSpecifici[0];

                VoloInArrivo volo = new VoloInArrivo(
                        numeroVolo, compagniaAerea, orarioPrevisto, data,
                        ritardo, statoVolo.toString(), tipoVolo,
                        aeroportoOrigine
                );
                Volo.aggiungiVolo(volo);
            } else {
                return false;  // Tipo di volo non valido
            }

            return true;
        } catch (Exception e) {
            return false;  // Gestisce qualsiasi errore durante l'inserimento
        }
    }

    /**
     * Aggiorna lo stato di un volo esistente (disponibile solo per amministratori)
     * @param numeroVolo Numero del volo da aggiornare
     * @param nuovoStato Nuovo stato del volo
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaStatoVolo(String numeroVolo, String nuovoStato) {
        if (!isUtenteAdmin()) {
            return false;
        }

        try {
            // Verifica che lo stato sia valido
            StatoVolo statoVolo = StatoVolo.valueOf(nuovoStato.toUpperCase());

            // Cerca il volo
            for (Volo volo : Volo.getListaVoli()) {
                if (volo.getNumeroVolo().equals(numeroVolo)) {
                    volo.setStato(statoVolo.toString());
                    return true;
                }
            }

            return false;  // Volo non trovato
        } catch (IllegalArgumentException e) {
            return false;  // Stato non valido
        }
    }

    /**
     * Aggiorna il ritardo di un volo (disponibile solo per amministratori)
     * @param numeroVolo Numero del volo
     * @param nuovoRitardo Nuovo valore del ritardo in minuti
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaRitardoVolo(String numeroVolo, int nuovoRitardo) {
        if (!isUtenteAdmin()) {
            return false;
        }

        // Cerca il volo
        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(numeroVolo)) {
                volo.setRitardo(nuovoRitardo);
                return true;
            }
        }

        return false;  // Volo non trovato
    }

    /**
     * Aggiorna il gate di imbarco di un volo in partenza (disponibile solo per amministratori)
     * @param numeroVolo Numero del volo
     * @param nuovoGate Nuovo gate di imbarco
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate) {
        if (!isUtenteAdmin()) {
            return false;
        }

        // Cerca il volo
        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(numeroVolo) && volo instanceof VoloInPartenza) {
                VoloInPartenza voloPartenza = (VoloInPartenza) volo;
                voloPartenza.setGateImbarco(nuovoGate);
                return true;
            }
        }

        return false;  // Volo non trovato o non è un volo in partenza
    }

    /**
     * Crea una nuova prenotazione per un volo (disponibile solo per utenti generici)
     * @param codiceVolo Codice del volo
     * @param numeroPasseggeri Numero di passeggeri
     * @return La prenotazione creata o null in caso di errore
     */
    public Prenotazione creaPrenotazione(String codiceVolo, int numeroPasseggeri) {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return null;
        }

        // Verifica se il numero di passeggeri è valido
        if (numeroPasseggeri <= 0 || numeroPasseggeri > Prenotazione.MAX_PASSEGGERI) {
            return null;
        }

        // Cerca il volo
        Volo voloSelezionato = null;
        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(codiceVolo)) {
                voloSelezionato = volo;
                break;
            }
        }

        if (voloSelezionato == null) {
            return null;  // Volo non trovato
        }

        // Ottieni informazioni sulla partenza/destinazione in base al tipo di volo
        String partenzaDestinazione;
        if (voloSelezionato instanceof VoloInPartenza) {
            partenzaDestinazione = ((VoloInPartenza) voloSelezionato).getAeroportoDestinazione();
        } else if (voloSelezionato instanceof VoloInArrivo) {
            partenzaDestinazione = ((VoloInArrivo) voloSelezionato).getAeroportoOrigine();
        } else {
            return null;  // Tipo di volo non gestito
        }

        // Genera un nuovo codice di prenotazione
        String codicePrenotazione = Prenotazione.generaNuovoCodice();

        // Crea la prenotazione
        Prenotazione prenotazione = new Prenotazione(
                codicePrenotazione,
                codiceVolo,
                voloSelezionato.getData(),
                partenzaDestinazione,
                StatoPrenotazione.CONFERMATA.toString(),
                numeroPasseggeri
        );

        // Aggiungi la prenotazione all'utente
        ((UtenteGenerico) utenteLoggato).setPrenotazione(prenotazione);

        return prenotazione;
    }

    /**
     * Aggiunge un ticket a una prenotazione esistente
     * @param prenotazione Prenotazione a cui aggiungere il ticket
     * @param nome Nome del passeggero
     * @param cognome Cognome del passeggero
     * @param numeroDocumento Numero documento del passeggero
     * @param dataNascita Data di nascita del passeggero
     * @param postoAssegnato Posto assegnato al passeggero
     * @return true se l'aggiunta è avvenuta con successo, false altrimenti
     */
    public boolean aggiungiTicket(Prenotazione prenotazione, String nome, String cognome,
                                  String numeroDocumento, String dataNascita, String postoAssegnato) {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return false;
        }

        // Verifica se la prenotazione appartiene all'utente loggato
        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        boolean prenotazioneTrovata = false;

        for (Prenotazione p : utente.getPrenotazioni()) {
            if (p.getCodicePrenotazione().equals(prenotazione.getCodicePrenotazione())) {
                prenotazioneTrovata = true;
                break;
            }
        }

        if (!prenotazioneTrovata) {
            return false;  // Prenotazione non trovata o non appartiene all'utente
        }

        // Verifica se c'è spazio per altri ticket
        if (prenotazione.getTickets().size() >= prenotazione.getNumeroPasseggeri()) {
            return false;  // Numero massimo di ticket raggiunto
        }

        // Crea e aggiungi il ticket
        Ticket ticket = new Ticket(
                nome,
                cognome,
                numeroDocumento,
                dataNascita,
                postoAssegnato,
                prenotazione.getCodicePrenotazione()
        );

        prenotazione.aggiungiTicket(ticket);
        return true;
    }


    /**
     * Ottiene tutte le prenotazioni dell'utente loggato
     * @return Lista di prenotazioni dell'utente o lista vuota se l'utente non è un UtenteGenerico
     */
    public ArrayList<Prenotazione> getPrenotazioniUtente() {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return new ArrayList<>();
        }

        return ((UtenteGenerico) utenteLoggato).getPrenotazioni();
    }

    /**
     * Ottiene un volo per numero
     * @param numeroVolo Numero del volo da cercare
     * @return Il volo trovato o null se non esiste
     */
    public Volo getVoloPerNumero(String numeroVolo) {
        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(numeroVolo)) {
                return volo;
            }
        }

        return null;  // Volo non trovato
    }

    /**
     * Ottiene la lista di voli in partenza
     * @return Lista di voli in partenza
     */
    public ArrayList<VoloInPartenza> getVoliInPartenza() {
        ArrayList<VoloInPartenza> voliInPartenza = new ArrayList<>();

        for (Volo volo : Volo.getListaVoli()) {
            if (volo instanceof VoloInPartenza) {
                voliInPartenza.add((VoloInPartenza) volo);
            }
        }

        return voliInPartenza;
    }

    /**
     * Ottiene la lista di voli in arrivo
     * @return Lista di voli in arrivo
     */
    public ArrayList<VoloInArrivo> getVoliInArrivo() {
        ArrayList<VoloInArrivo> voliInArrivo = new ArrayList<>();

        for (Volo volo : Volo.getListaVoli()) {
            if (volo instanceof VoloInArrivo) {
                voliInArrivo.add((VoloInArrivo) volo);
            }
        }

        return voliInArrivo;
    }

    public ArrayList<Volo> getTuttiIVoli() {
        return Volo.getListaVoli();
    }

    /**
     * Ottiene tutti i voli disponibili per la prenotazione
     * Filtra solo i voli che non sono cancellati o ritardati
     * @return Lista dei voli disponibili per prenotazione
     */
    public ArrayList<Volo> getVoliDisponibili() {
        ArrayList<Volo> voliDisponibili = new ArrayList<>();

        for (Volo volo : Volo.getListaVoli()) {
            // Filtra solo i voli che sono disponibili per la prenotazione
            // (puoi aggiungere più filtri se necessario)
            if (!"CANCELLATO".equalsIgnoreCase(volo.getStato()) &&
                    !"TERMINATO".equalsIgnoreCase(volo.getStato())) {
                voliDisponibili.add(volo);
            }
        }

        return voliDisponibili;
    }


    /**
     * Cambia lo stato di una prenotazione
     * @param codicePrenotazione Codice della prenotazione
     * @param nuovoStato Nuovo stato della prenotazione
     * @return true se la modifica è avvenuta con successo, false altrimenti
     */
    /* public boolean cambiaStatoPrenotazione(String codicePrenotazione, String nuovoStato) {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return false;
        }

        try {
            // Verifica che lo stato sia valido
            StatoPrenotazione statoPrenotazione = StatoPrenotazione.valueOf(nuovoStato.toUpperCase());

            // Cerca la prenotazione
            Prenotazione prenotazione = cercaPrenotazione(codicePrenotazione);
            if (prenotazione != null) {
                prenotazione.setStato(statoPrenotazione.toString());
                return true;
            }

            return false;  // Prenotazione non trovata
        } catch (IllegalArgumentException e) {
            return false;  // Stato non valido
        }
    }
    */

    /**
     * Elimina una prenotazione
     * @param prenotazione La prenotazione da eliminare
     * @return true se l'eliminazione è avvenuta con successo
     */
    public boolean eliminaPrenotazione(Prenotazione prenotazione) {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return false;
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        return utente.getPrenotazioni().remove(prenotazione);
    }

    /**
     * Modifica un ticket di una prenotazione
     * @param ticket Il ticket da modificare
     * @param nuovoNome Nuovo nome (opzionale)
     * @param nuovoCognome Nuovo cognome (opzionale)
     * @param nuovoNumeroDocumento Nuovo numero documento (opzionale)
     * @param nuovaDataNascita Nuova data nascita (opzionale)
     * @return true se la modifica è avvenuta con successo
     */
    public boolean modificaTicket(Ticket ticket, String nuovoNome, String nuovoCognome,
                                  String nuovoNumeroDocumento, String nuovaDataNascita) {
        try {
            if (nuovoNome != null && !nuovoNome.trim().isEmpty()) {
                ticket.setNome(nuovoNome);
            }
            if (nuovoCognome != null && !nuovoCognome.trim().isEmpty()) {
                ticket.setCognome(nuovoCognome);
            }
            if (nuovoNumeroDocumento != null && !nuovoNumeroDocumento.trim().isEmpty()) {
                ticket.setNumeroDocumento(nuovoNumeroDocumento);
            }
            if (nuovaDataNascita != null && !nuovaDataNascita.trim().isEmpty()) {
                ticket.setDataNascita(nuovaDataNascita);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}