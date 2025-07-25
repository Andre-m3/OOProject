package controller;

import implementazioniPostgresDAO.*;
import model.*;
import dao.*;

// SonarQube suggerisce l'utilizzo dei Logger per sostituire eventuali System.out!
import java.util.logging.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Controller che fa da intermediario tra il pacchetto GUI e il pacchetto model.
 * Viene utilizzato il pattern 'Singleton' per garantire una singola istanza.
 */
public class Controller {

    private Random random = new Random();                   // Oggetto riutilizzabile imposto da SonarQube
    Logger logger = Logger.getLogger(getClass().getName()); // Logger per sostituire tutti i System.out (come suggerito da SonarQube)

    // Altre modifiche apportate al fine di soddisfare i consigli di SonarQube (duplicazione di "dd-MM-yyyy")
    private static final DateTimeFormatter FORMATTER_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_DASH = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String NAPOLI = "Napoli";
    private static final String TIPO_VOLO_PARTENZA = "Partenza";

    // NB.: SonarQube suggerisce inoltre la rimozione del nome generico di un eccezione (nei nostri casi 'e', 'e1'), e l'utilizzo di un unnamed operator '_'
    //      Questo però provoca errori di compilazione dovuti a versioni utilizzate nel progetto, quindi non implementabile.

    private static Controller instance = null;              // istanza Singleton
    private Utente utenteLoggato;                           // Utente corrente

    // DAO per l'accesso ai dati
    private VoloDAO voloDAO = new ImplementazioneVoloDAO();
    private UtenteDAO utenteDAO = new ImplementazioneUtenteDAO();
    private PrenotazioneDAO prenotazioneDAO = new ImplementazionePrenotazioneDAO();
    private TicketDAO ticketDAO = new ImplementazioneTicketDAO();

    /**
     * Costruttore privato per il pattern Singleton
     */
    private Controller() {}

    /**
     * Restituisce l'istanza singleton del controller
     *
     * @return L 'istanza del controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Effettua il login di un utente
     *
     * @param emailUsername Username dell'utente
     * @param password      Password dell'utente
     * @return true se il login è avvenuto con successo, false altrimenti
     */
    public boolean login(String emailUsername, String password) {
        ArrayList<String> datiUtente = utenteDAO.getUtentePerCredenziali(emailUsername, password);

        if (datiUtente != null) {
            String username = datiUtente.get(0);
            String email = datiUtente.get(1);
            String pass = datiUtente.get(2);
            boolean isAdmin = Boolean.parseBoolean(datiUtente.get(3));

            if (isAdmin) {
                utenteLoggato = new Amministratore(email, username, pass);
            } else {
                utenteLoggato = new UtenteGenerico(email, username, pass);
            }

            return true;
        }

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
     *
     * @param email    Email del nuovo utente
     * @param username Username del nuovo utente
     * @param password Password del nuovo utente
     * @param isAdmin  true se l'utente è un amministratore, false altrimenti
     * @return true se la registrazione è avvenuta con successo, false altrimenti
     */
    public boolean registraUtente(String email, String username, String password, boolean isAdmin) {
        // Verifica se l'utente esiste già
        if (utenteDAO.esisteUtente(username, email)) {
            return false;
        }

        // Inserisce l'utente usando solo dati primitivi!
        return utenteDAO.inserisciUtente(email, username, password, isAdmin);

    }

    /**
     * Restituisce l'utente attualmente loggato
     *
     * @return L 'utente loggato o null se nessun utente è loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Verifica se l'utente loggato è un amministratore
     *
     * @return true se l'utente è un amministratore, false altrimenti
     */
    public boolean isUtenteAdmin() {
        return utenteLoggato instanceof Amministratore;
    }

    /**
     * Determina il tipo di un volo (Partenza/Arrivo) in base alla sua istanza
     *
     * @param volo Il volo da controllare
     * @return "Partenza" se è un volo in partenza, "Arrivo" se è in arrivo
     */
    public String getTipoVolo(Volo volo) {
        if (volo instanceof VoloInPartenza) {
            return TIPO_VOLO_PARTENZA;
        } else if (volo instanceof VoloInArrivo) {
            return "Arrivo";
        }
        return "--- Sconosciuto ---";       // I trattini rendono più visibile la "particolarità"
    }

    /**
     * Verifica se un volo è in partenza
     *
     * @param volo Il volo da controllare
     * @return true se è un volo in partenza
     */
    public boolean isVoloInPartenza(Volo volo) {
        return volo instanceof VoloInPartenza;
    }

    /**
     * Verifica se un volo è prenotabile
     *
     * @param numeroVolo Il numero del volo
     * @return true se il volo è prenotabile, false altrimenti
     */
    public boolean isVoloPrenotabile(String numeroVolo) {
        ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);
        return datiVolo != null && "PROGRAMMATO".equals(datiVolo.get(5));
    }

    /**
     * Ottiene il gate di imbarco per un volo in partenza
     *
     * @param volo Il volo
     * @return Il gate di imbarco o null se non disponibile
     */
    public Short getGateImbarco(Volo volo) {
        if (volo instanceof VoloInPartenza) {
            return ((VoloInPartenza) volo).getGateImbarco();
        }
        return null;
    }

    /**
     * Converte la data da formato dd/mm/yyyy o dd-mm-yyyy a dd-MM-yyyy
     */
    private String convertiFormatoData(String data) {
        try {
            LocalDate dataConvertita;
            if (data.contains("/")) {
                dataConvertita = LocalDate.parse(data, FORMATTER_SLASH);
            } else {
                dataConvertita = LocalDate.parse(data, FORMATTER_DASH);
            }
            return dataConvertita.format(FORMATTER_DASH);
        } catch (DateTimeParseException e) {
            logger.info("Errore nella conversione della data: " + data);
            return data;            // Restituisce la data originale se non riesce a convertirla
        }
    }

    private Volo convertiArrayListInVolo(ArrayList<String> datiVolo) {
        if (datiVolo == null) {
            return null;
        }

        try {
            String numeroVolo = datiVolo.get(0);
            String compagniaAerea = datiVolo.get(1);
            String orarioPrevisto = datiVolo.get(2);
            String data = datiVolo.get(3);
            int ritardo = Integer.parseInt(datiVolo.get(4));
            StatoVolo stato = StatoVolo.valueOf(datiVolo.get(5));
            String partenza = datiVolo.get(6);
            String destinazione = datiVolo.get(7);
            String tipoVolo = datiVolo.get(9);

            if ("PARTENZA".equals(tipoVolo)) {
                Short gateImbarco = null;
                if (datiVolo.get(8) != null && !datiVolo.get(8).equals("null")) {
                    gateImbarco = Short.valueOf(datiVolo.get(8));
                }
                return new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto, data,
                        ritardo, stato, destinazione, gateImbarco);
            } else {
                return new VoloInArrivo(numeroVolo, compagniaAerea, orarioPrevisto, data,
                        ritardo, stato, partenza);
            }
        } catch (Exception e) {
            logger.info("Errore nella conversione volo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inserisce un nuovo volo (disponibile solo per amministratori)
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param stato          the stato
     * @param partenza       the partenza
     * @param destinazione   the destinazione
     * @param gateImbarco    the gate imbarco
     * @return the boolean
     */
    public boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                                 String data, String stato, String partenza, String destinazione,
                                 Short gateImbarco) {
        if (!isUtenteAdmin()) {
            return false;
        }

        // Per prima cosa verifichiamo che partenza e destinazione non coincidano
        if (partenza.equalsIgnoreCase(destinazione)) {
            return false;
        }

        String dataConvertita = convertiFormatoData(data);
        if (dataConvertita == null || !isStatoVoloValido(stato)) {
            return false;
        }

        StatoVolo statoVolo = stringToStatoVolo(stato);

        // Ora che abbiamo validato tutto, creiamo l'istanza del Volo (e verifichiamone il tipo)
        Volo nuovoVolo;
        String tipoVolo;

        if (partenza.equalsIgnoreCase(NAPOLI)) {
            nuovoVolo = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
                    dataConvertita, 0, statoVolo, destinazione, gateImbarco);
        } else {
            nuovoVolo = new VoloInArrivo(numeroVolo, compagniaAerea, orarioPrevisto,
                    dataConvertita, 0, statoVolo, partenza);
        }

        tipoVolo = (nuovoVolo instanceof VoloInPartenza) ? "PARTENZA" : "ARRIVO";

        return voloDAO.inserisciVolo(numeroVolo, compagniaAerea, orarioPrevisto,
                dataConvertita, stato, partenza, destinazione, gateImbarco, tipoVolo);
    }

    /**
     * Aggiorna il gate di imbarco di un volo in partenza (disponibile solo per amministratori)
     *
     * @param numeroVolo the numero volo
     * @param nuovoGate  the nuovo gate
     * @return the boolean
     */
    public boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate) {
        if (!isUtenteAdmin()) {
            return false;
        }
        return voloDAO.aggiornaGateImbarco(numeroVolo, nuovoGate);
    }

    /**
     * Crea una nuova prenotazione per un volo
     *
     * @param codiceVolo       the codice volo
     * @param numeroPasseggeri the numero passeggeri
     * @param email            the email
     * @return the prenotazione
     */
    public Prenotazione creaPrenotazione(String codiceVolo, int numeroPasseggeri, String email) {

        // Validazioni di input
        if (codiceVolo == null || codiceVolo.trim().isEmpty()) {
            return null;
        }
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        if (numeroPasseggeri <= 0) {
            return null;
        }

        // Generiamo il codice della prenotazione seguendo la logica "P00000" (con numeri progressivi)
        String codicePrenotazione = generaProssimoCodicePrenotazione();

        boolean successo = prenotazioneDAO.inserisciPrenotazione(
                codicePrenotazione, email, codiceVolo, "IN_ATTESA", numeroPasseggeri
        );

        if (successo) {
            return new Prenotazione(codicePrenotazione, codiceVolo, StatoPrenotazione.IN_ATTESA,
                    numeroPasseggeri, email);
        }
        return null;
    }

    /**
     * Genera il prossimo codice prenotazione in formato P20001, P20002, etc.
     * Versione semplificata
     */
    private String generaProssimoCodicePrenotazione() {
        try {
            ArrayList<ArrayList<String>> tuttePrenotazioni = prenotazioneDAO.getTuttePrenotazioni();

            // "Configuriamo" il codice della prima prenotazione! Solo se quando richiamato non ci dovessero essere prenotazioni esistenti
            // Si potrebbe gestire anche tramite database, ma lo abbiamo ritenuto valido a livello di programmazione!
            if (tuttePrenotazioni == null || tuttePrenotazioni.isEmpty()) {
                return "P20001";                // Prima prenotazione
            }

            // Conta semplicemente le prenotazioni esistenti! Numero progressivo...
            int numeroSuccessivo = 20001 + tuttePrenotazioni.size();
            return "P" + numeroSuccessivo;

        } catch (Exception e) {
            logger.info("Errore nella generazione del codice: " + e.getMessage());
            return "P" + (20000 + (int)(Math.random() * 9999)); // Fallback
        }
    }

    /**
     * Aggiunge un ticket a una prenotazione esistente
     *
     * @param prenotazione    the prenotazione
     * @param nome            the nome
     * @param cognome         the cognome
     * @param numeroDocumento the numero documento
     * @param dataNascita     the data nascita
     * @param postoAssegnato  the posto assegnato
     * @return the boolean
     */
    public boolean aggiungiTicket(Prenotazione prenotazione, String nome, String cognome,
                                  String numeroDocumento, String dataNascita, String postoAssegnato) {
        return ticketDAO.inserisciTicket(prenotazione.getCodicePrenotazione(), nome, cognome,
                numeroDocumento, dataNascita, postoAssegnato);
    }

    /**
     * Ottiene un volo per numero
     *
     * @param numeroVolo the numero volo
     * @return the volo per numero
     */
    public Volo getVoloPerNumero(String numeroVolo) {
        ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);
        return convertiArrayListInVolo(datiVolo);
    }

    /**
     * Ottiene tutti i voli per l'amministratore
     *
     * @return the tutti i voli
     */
    public ArrayList<Volo> getTuttiIVoli() {
        ArrayList<ArrayList<String>> datiVoli = voloDAO.getTuttiVoli();
        return convertiListaArrayListInVoli(datiVoli);
    }

    /**
     * Completa una prenotazione aggiungendo tutti i ticket necessari
     *
     * @param codiceVolo       the codice volo
     * @param numeroPasseggeri the numero passeggeri
     * @param datiPasseggeri   the dati passeggeri
     * @return the string
     */
    public String completaPrenotazione(String codiceVolo, int numeroPasseggeri, String[][] datiPasseggeri) {
        if (utenteLoggato == null) {
            return null;
        }

        // Viene creata la prenotazione
        Prenotazione prenotazione = creaPrenotazione(codiceVolo, numeroPasseggeri, utenteLoggato.getEmail());

        if (prenotazione == null) {
            return null;
        }

        // Aggiunge tutti i ticket...
        for (int i = 0; i < numeroPasseggeri; i++) {
            String postoAssegnato = generaPostoCasuale();

            // Convertiamo la data nel formato che vuole il database!
            String dataNascita = datiPasseggeri[i][3];
            if (dataNascita.contains("/")) {
                dataNascita = convertiFormatoData(dataNascita);
            }

            boolean ticketAggiunto = aggiungiTicket(
                    prenotazione,
                    datiPasseggeri[i][0],           //nome
                    datiPasseggeri[i][1],           //cognome
                    datiPasseggeri[i][2],           //numero documento
                    dataNascita,                    //data di nascita
                    postoAssegnato
            );

            if (!ticketAggiunto) {
                return null;
            }
        }

        return prenotazione.getCodicePrenotazione();
    }

    /**
     * Valida il formato della data (dd/mm/yyyy)
     * Riusciamo a gestire anche il formato (dd-mm-yyyy) tramite un Try-Catch
     *
     * @param data the data
     * @return the boolean
     */
    public boolean isValidDateFormat(String data) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }

        try {
            // Creiamo i pattern come "dd/mm/yyyy" e "dd-mm-yyyy"
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            try {
                LocalDate.parse(data, formatter1);
                return true;
            } catch (DateTimeParseException e) {
                try {
                    LocalDate.parse(data, formatter2);
                    return true;
                } catch (DateTimeParseException e1) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Genera un posto casuale da A1 a T6
     * Ci basiamo sulla logica ASCII per la generazione di un intero e il suo casting in char
     */
    private String generaPostoCasuale() {

        char lettera = (char) ('A' + this.random.nextInt(20)); // A-T
        int numero = random.nextInt(6) + 1; // 1-6
        return String.valueOf(lettera) + numero;
    }

    /**
     * Elimina un volo dal sistema (disponibile solo per amministratori)
     *
     * @param numeroVolo Il numero del volo da eliminare
     * @return true se l'eliminazione è riuscita, false altrimenti
     */
    public boolean eliminaVolo(String numeroVolo) {
        if (utenteLoggato == null || !isUtenteAdmin()) {
            return false;
        }

        try {
            // Verifica se il volo esiste
            if (voloDAO.getVoloPerNumero(numeroVolo) == null) {
                return false;
            }

            // Verifica se ci sono prenotazioni attive per questo volo
            var prenotazioni = prenotazioneDAO.getPrenotazioniPerVolo(numeroVolo);
            if (prenotazioni != null && !prenotazioni.isEmpty()) {
                // Se ci sono prenotazioni attive, non permettiamo l'eliminazione
                return false;
            }

            // Procedi con l'eliminazione
            return voloDAO.eliminaVolo(numeroVolo);

        } catch (Exception e) {
            logger.info("Errore durante l'eliminazione del volo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se un volo può essere eliminato (nessuna prenotazione attiva)
     *
     * @param numeroVolo Il numero del volo
     * @return true se il volo può essere eliminato, false altrimenti
     */
    public boolean puoEliminareVolo(String numeroVolo) {
        if (utenteLoggato == null || !isUtenteAdmin()) {
            return false;
        }

        try {
            // Verifica se il volo esiste
            if (voloDAO.getVoloPerNumero(numeroVolo) == null) {
                return false;
            }

            // Controlla se ci sono prenotazioni per questo volo
            var prenotazioni = prenotazioneDAO.getPrenotazioniPerVolo(numeroVolo);
            return prenotazioni == null || prenotazioni.isEmpty();

        } catch (Exception e) {
            logger.info("Errore nella verifica possibilità eliminazione volo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ottiene i dettagli dei ticket di una prenotazione
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the string [ ] [ ]
     */
    public String[][] getTicketsPrenotazione(String codicePrenotazione) {
        ArrayList<ArrayList<String>> datiTickets = ticketDAO.getTicketsPerPrenotazione(codicePrenotazione);

        // Se dovessimo avere (improbabile!) un ticket "vuoto" allora ritorniamo un valore "vuoto" compatibile al tipo di ritorno del metodo
        if (datiTickets.isEmpty()) {
            return new String[0][0];
        }

        String[][] tickets = new String[datiTickets.size()][6];

        // "Popoliamo" il ticket da restituire per poi visualizzarlo
        for (int i = 0; i < datiTickets.size(); i++) {
            ArrayList<String> ticket = datiTickets.get(i);
            // Ticket.get(0) equivale al codice prenotazione, che in questo caso non ci serve!
            tickets[i][0] = ticket.get(0);          // nome
            tickets[i][1] = ticket.get(1);          // cognome
            tickets[i][2] = ticket.get(2);          // documento
            tickets[i][3] = ticket.get(3);          // data nascita
            tickets[i][4] = ticket.get(4);          // posto
        }

        return tickets;
    }

    /**
     * Ottiene i dati delle prenotazioni dell'utente loggato per la visualizzazione in tabella
     *
     * @return the object [ ] [ ]
     */
    public Object[][] getDatiPrenotazioniUtente() {
        if (utenteLoggato == null) {
            return new Object[0][0];
        }

        // Attenzione! Bisogna restituire ESCLUSIVAMENTE le prenotazioni associate al relativo utente loggato... È molto importante!
        ArrayList<ArrayList<String>> prenotazioni = prenotazioneDAO.getPrenotazioniPerUtente(utenteLoggato.getEmail());
        Object[][] datiTabella = new Object[prenotazioni.size()][8];

        for (int i = 0; i < prenotazioni.size(); i++) {
            ArrayList<String> prenotazione = prenotazioni.get(i);
            String numeroVolo = prenotazione.get(2);
            ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);

            if (datiVolo != null) {
                String partenza = datiVolo.get(6);
                String destinazione = datiVolo.get(7);
                String tratta = partenza + " → " + destinazione;
                String statoVolo = datiVolo.get(5);
                String orario = datiVolo.get(2);
                int ritardo = Integer.parseInt(datiVolo.get(4));
                String ritardoStr = ritardo > 0 ? ritardo + " min" : "Nessun ritardo";

                datiTabella[i] = new Object[]{
                        prenotazione.get(0),        //Codice prenotazione
                        prenotazione.get(4),        //Numero passeggeri
                        prenotazione.get(3),        //Stato prenotazione
                        numeroVolo,                 //Codice volo
                        tratta,                     //Tratta
                        statoVolo,                  //Stato volo
                        orario,                     //Orario partenza
                        ritardoStr                  //Ritardo eventuale
                };
            } else {
                // Vediamo cosa fare se il Volo non è stato trovato (Alternativa estrema per visualizzare qualcosa invece di ricevere errore)
                datiTabella[i] = new Object[]{
                        prenotazione.get(0), "N/A", prenotazione.get(3),
                        numeroVolo, "N/A → N/A", "N/A", "N/A", "N/A"
                };
            }
        }

        return datiTabella;
    }

    /**
     * Ottiene il numero di prenotazioni dell'utente loggato
     *
     * @return the numero prenotazioni utente
     */
    public int getNumeroPrenotazioniUtente() {
        if (utenteLoggato == null) {
            return 0;
        }
        ArrayList<ArrayList<String>> prenotazioni = prenotazioneDAO.getPrenotazioniPerUtente(utenteLoggato.getEmail());
        return prenotazioni.size();
    }

    /**
     * Ottiene i dettagli di una prenotazione per codice
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the string [ ]
     */
    public String[] getDettagliPrenotazione(String codicePrenotazione) {
        ArrayList<String> datiPrenotazione = prenotazioneDAO.getPrenotazionePerCodice(codicePrenotazione);
        if (datiPrenotazione == null) {
            return new String[0];       //null
        }

        String numeroVolo = datiPrenotazione.get(2);
        ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);

        if (datiVolo == null) {
            return new String[]{
                    datiPrenotazione.get(0),        //[0] codice prenotazione
                    numeroVolo,
                    "N/A",
                    "N/A → N/A",
                    datiPrenotazione.get(3),        //[4] stato prenotazione
                    datiPrenotazione.get(4)         //[5] numero passeggeri
            };
        }

        String partenza = datiVolo.get(6);
        String destinazione = datiVolo.get(7);
        String data = datiVolo.get(3);
        String tratta = partenza + " → " + destinazione;

        return new String[]{
                datiPrenotazione.get(0),        //[0] codice prenotazione
                numeroVolo,                     //[1] numero volo
                data,                           //[2] data
                tratta,                         //[3] tratta
                datiPrenotazione.get(3),        //[4] stato prenotazione
                datiPrenotazione.get(4)         //[5] numero passeggeri
        };
    }

    /**
     * Ottiene una prenotazione specifica tramite il codice
     *
     * @param codicePrenotazione the codice prenotazione
     * @return the prenotazione per codice
     */
    public Prenotazione getPrenotazionePerCodice(String codicePrenotazione) {
        ArrayList<String> datiPrenotazione = prenotazioneDAO.getPrenotazionePerCodice(codicePrenotazione);

        if (datiPrenotazione == null || datiPrenotazione.isEmpty()) {
            return null;
        }

        return convertiArrayListInPrenotazione(datiPrenotazione);
    }

    /**
     * Ottiene i dettagli di una prenotazione per la visualizzazione nel dialog
     *
     * @param prenotazione the prenotazione
     * @return the string [ ]
     */
    public String[] getDettagliPrenotazioneDialog(Object prenotazione) {

        // Verifichiamo sempre se l'oggetto passato non sia "null"!
        if (prenotazione == null) {
            return new String[0];       //null
        }

        // La prima verifica può risultare superflua (caso IMPROBABILE) ma è sempre meglio pensare al caso peggiore...
        String codicePrenotazione;
        if (prenotazione instanceof String) {
            codicePrenotazione = (String) prenotazione;
        } else if (prenotazione instanceof Prenotazione) {
            codicePrenotazione = ((Prenotazione) prenotazione).getCodicePrenotazione();
        } else {
            return new String[0];       //null
        }

        // Richiamiamo il metodo del DAO per evitare l'import della classe del model
        ArrayList<String> datiPrenotazione = prenotazioneDAO.getPrenotazionePerCodice(codicePrenotazione);
        if (datiPrenotazione == null) {
            return new String[0];       //null
        }

        String numeroVolo = datiPrenotazione.get(2);
        ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);

        if (datiVolo == null) {
            return new String[]{
                    codicePrenotazione,
                    numeroVolo,
                    "N/A",
                    "N/A",
                    datiPrenotazione.get(3)             // è lo stato della prenotazione
            };
        }

        // Formattiamo i dati cosi da visualizzarli (potremmo evitare di assegnare nuove variabili, ma così è più leggibile)
        String partenza = datiVolo.get(6);
        String destinazione = datiVolo.get(7);
        String data = datiVolo.get(3);
        String tratta = partenza + " → " + destinazione;

        return new String[]{
                codicePrenotazione,             //[0] codice
                numeroVolo,                     //[1] volo
                data,                           //[2] data
                tratta,                         //[3] tratta
                datiPrenotazione.get(3)         //[4] stato prenotazione
        };
    }

    /**
     * Ottiene la lista dei ticket di una prenotazione formattata per la visualizzazione
     *
     * @param prenotazione the prenotazione
     * @return the string [ ]
     */
    public String[] getTicketsFormattati(Object prenotazione) {

        String codicePrenotazione;

        // La prima verifica può risultare superflua (caso IMPROBABILE) ma è sempre meglio pensare al caso peggiore...
        if (prenotazione instanceof String) {
            codicePrenotazione = (String) prenotazione;
        } else if (prenotazione instanceof Prenotazione) {
            codicePrenotazione = ((Prenotazione) prenotazione).getCodicePrenotazione();
        } else {
            return new String[0];
        }

        // Come prima, questo approccio ci permette di non importare le classi del package model
        ArrayList<ArrayList<String>> datiTickets = ticketDAO.getTicketsPerPrenotazione(codicePrenotazione);

        if (datiTickets.isEmpty()) {
            return new String[0];
        }

        String[] ticketsFormattati = new String[datiTickets.size()];

        // Effettuo l'operazione per ciascun ticket presente nella prenotazione!
        for (int i = 0; i < datiTickets.size(); i++) {
            ArrayList<String> ticket = datiTickets.get(i);

            // Formato: "Nome Cognome - Posto: Y - Documento: X"
            ticketsFormattati[i] = String.format("%s %s - Posto: %s - Documento: %s",
                    ticket.get(0),      // nome
                    ticket.get(1),      // cognome
                    ticket.get(4),      // posto
                    ticket.get(2)       // documento
            );
        }

        return ticketsFormattati;
    }

    /**
     * Ottiene un ticket specifico da una prenotazione per indice
     *
     * @param prenotazione the prenotazione
     * @param indice       the indice
     * @return the ticket per indice
     */
    public Object getTicketPerIndice(Object prenotazione, int indice) {

        // Avremmo potuto dichiarare il metodo con il tipo "Ticket", però visti alcuni errori di compilazione
        // Abbiamo dovuto generalizzare prima il tipo, e successivamente effettuare i controlli
        // In questo modo possiamo gestire anche i vari casi "improbabili"

        String codicePrenotazione;
        if (prenotazione instanceof String) {
            codicePrenotazione = (String) prenotazione;
        } else if (prenotazione instanceof Prenotazione) {
            codicePrenotazione = ((Prenotazione) prenotazione).getCodicePrenotazione();
        } else {
            return null;
        }

        ArrayList<ArrayList<String>> datiTickets = ticketDAO.getTicketsPerPrenotazione(codicePrenotazione);

        if (indice < 0 || indice >= datiTickets.size()) {
            return null;
        }

        ArrayList<String> datiTicket = datiTickets.get(indice);
        return convertiArrayListInTicket(datiTicket);
    }

    /**
     * Elimina una prenotazione
     *
     * @param prenotazione the prenotazione
     * @return the boolean
     */
    public boolean eliminaPrenotazione(Object prenotazione) {
        if (!isUtenteAdmin() && utenteLoggato == null) {
            return false;
        }

        String codicePrenotazione;
        if (prenotazione instanceof String) {
            codicePrenotazione = (String) prenotazione;
        } else if (prenotazione instanceof Prenotazione) {
            codicePrenotazione = ((Prenotazione) prenotazione).getCodicePrenotazione();
        } else {
            return false;
        }

        // Prima eliminiamo tutti i tickets associati
        ticketDAO.eliminaTicketsPerPrenotazione(codicePrenotazione);

        // E solo dopo (importante) eliminiamo la prenotazione
        return prenotazioneDAO.eliminaPrenotazione(codicePrenotazione);

        // NB.: Se per caso l'eliminazione di un ticket dovesse fallire, interromperemo l'operazione
        //      Quindi eliminando prima i ticket e poi la prenotazione, noi non avremo problemi con la prenotazione!
    }

    /**
     * Aggiorna i dati di un ticket
     *
     * @param ticket           the ticket
     * @param nuovoNome        the nuovo nome
     * @param nuovoCognome     the nuovo cognome
     * @param nuovoDocumento   the nuovo documento
     * @param nuovaDataNascita the nuova data nascita
     * @param postoAttuale     the posto attuale
     * @return the boolean
     */
    public boolean aggiornaTicket(Object ticket, String nuovoNome, String nuovoCognome,
                                  String nuovoDocumento, String nuovaDataNascita, String postoAttuale) {

        // Il controllo è abbastanza superfluo, è improbabile, ma come al solito meglio ipotizzare il "peggiore dei casi".
        if (ticket == null) {
            return false;
        }

        Ticket t = (Ticket) ticket;

        return ticketDAO.aggiornaTicket(
                t.getCodicePrenotazione(),
                postoAttuale,
                nuovoNome,
                nuovoCognome,
                nuovoDocumento,
                nuovaDataNascita
        );
    }


    /**
     * Ottiene i dettagli di un ticket per la visualizzazione nel dialog di modifica
     *
     * @param ticket the ticket
     * @return the string [ ]
     */
    public String[] getDettagliTicket(Object ticket) {
        if (ticket == null) {
            return new String[0];       //null
        }

        Ticket t = (Ticket) ticket;

        return new String[]{
                t.getNome(),
                t.getCognome(),
                t.getDataNascita(),
                t.getNumeroDocumento(),
                t.getPostoAssegnato()
            };
    }

    /**
     * Ottiene i dati di un volo come array di stringhe per la GUI
     *
     * @param numeroVolo the numero volo
     * @return the string [ ]
     */
    public String[] getDatiVolo(String numeroVolo) {
        ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);

        if (datiVolo == null) {
            return new String[0];       //null
        }

        return datiVolo.toArray(new String[0]);
    }

    /**
     * Aggiorna tutti i dati di un volo (disponibile solo per amministratori)
     *
     * @param numeroVoloOriginale the numero volo originale
     * @param nuovaCompagnia      the nuova compagnia
     * @param nuovoOrario         the nuovo orario
     * @param nuovaData           the nuova data
     * @param nuovoRitardo        the nuovo ritardo
     * @param nuovoStato          the nuovo stato
     * @param nuovaPartenza       the nuova partenza
     * @param nuovaDestinazione   the nuova destinazione
     * @return the boolean
     */
    public boolean aggiornaVolo(String numeroVoloOriginale, String nuovaCompagnia,
                                String nuovoOrario, String nuovaData, int nuovoRitardo,
                                String nuovoStato, String nuovaPartenza, String nuovaDestinazione) {
        if (!isUtenteAdmin()) {
            return false;
        }

        // Validazione extra. Verifichiamo che almeno una città sia Napoli, e che non lo siano entrambe
        if (!nuovaPartenza.equalsIgnoreCase(NAPOLI) && !nuovaDestinazione.equalsIgnoreCase(NAPOLI)) {
            logger.info("Errore: Almeno una tra partenza e destinazione deve essere 'Napoli'");
            return false;
        }
        if (nuovaPartenza.equalsIgnoreCase(nuovaDestinazione)) {
            logger.info("Errore: Partenza e destinazione non possono essere uguali");
            return false;
        }

        ArrayList<String> datiVoloEsistente = voloDAO.getVoloPerNumero(numeroVoloOriginale);
        if (datiVoloEsistente == null) {
            return false;
        }

        // Ora bisogna determinare il gate. Controlliamo se è un volo in partenza
        // Se non è un volo in partenza, allora non ci interessa!
        Short gate = null;
        if (nuovaPartenza.equalsIgnoreCase(NAPOLI) &&
                datiVoloEsistente.get(8) != null &&
                !datiVoloEsistente.get(8).equals("null")) {
            try {
                gate = Short.parseShort(datiVoloEsistente.get(8));
            } catch (NumberFormatException e) {
                gate = null;
            }
        }

        return voloDAO.aggiornaVolo(numeroVoloOriginale, nuovaCompagnia, nuovoOrario,
                nuovaData, nuovoStato, nuovaPartenza, nuovaDestinazione, gate, nuovoRitardo);
    }

    /**
     * Ottiene il gate di un volo come stringa
     *
     * @param numeroVolo the numero volo
     * @return the gate volo stringa
     */
    public String getGateVoloStringa(String numeroVolo) {
        ArrayList<String> datiVolo = voloDAO.getVoloPerNumero(numeroVolo);

        if (datiVolo == null || datiVolo.get(8) == null || "null".equals(datiVolo.get(8))) {
            return "N/A";
        }

        return datiVolo.get(8);
    }

    /**
     * Get stati volo disponibili string [ ].
     *
     * @return the string [ ]
     */

    // Metodi per gestire gli Stati del Volo
    public String[] getStatiVoloDisponibili() {
        StatoVolo[] stati = StatoVolo.values();
        String[] risultato = new String[stati.length];
        for (int i = 0; i < stati.length; i++) {
            risultato[i] = stati[i].toString();
        }
        return risultato;
    }

    /**
     * Is stato volo valido boolean.
     *
     * @param stato the stato
     * @return the boolean
     */
    public boolean isStatoVoloValido(String stato) {
        try {
            StatoVolo.valueOf(stato.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private StatoVolo stringToStatoVolo(String stato) {
        return StatoVolo.valueOf(stato.toUpperCase());
    }

    /**
     * Ottiene tutti i voli che partono da una specifica città
     * (indipendentemente dal tipo di volo - partenza o arrivo)
     *
     * @param cittaPartenza the citta partenza
     * @return the object [ ] [ ]
     */
    public Object[][] getVoliInPartenza(String cittaPartenza) {
        ArrayList<ArrayList<String>> datiVoli = voloDAO.getVoliPerCittaPartenza(cittaPartenza);

        if (datiVoli.isEmpty()) {
            return new Object[0][0];
        }

        Object[][] risultati = new Object[datiVoli.size()][8];

        for (int i = 0; i < datiVoli.size(); i++) {
            ArrayList<String> datiVolo = datiVoli.get(i);

            // Formatto l'orario con eventuale ritardo
            String orarioCompleto = datiVolo.get(2); // orario previsto
            int ritardo = Integer.parseInt(datiVolo.get(4));
            if (ritardo > 0) {
                orarioCompleto += " (+" + ritardo + " min)";
            }

            risultati[i][0] = datiVolo.get(0);      //numero volo
            risultati[i][1] = datiVolo.get(1);      //compagnia aerea
            risultati[i][2] = datiVolo.get(6);      //partenza
            risultati[i][3] = datiVolo.get(7);      //destinazione
            risultati[i][4] = datiVolo.get(3);      //data
            risultati[i][5] = orarioCompleto;       //orario con ritardo
            risultati[i][6] = datiVolo.get(5);      //stato
            risultati[i][7] = datiVolo.get(9);      //tipo volo
        }

        return risultati;
    }

    /**
     * Ottiene tutti i voli che arrivano in una specifica città
     * (indipendentemente dal tipo di volo - partenza o arrivo)
     *
     * @param cittaDestinazione the citta destinazione
     * @return the object [ ] [ ]
     */
    public Object[][] getVoliInArrivo(String cittaDestinazione) {
        ArrayList<ArrayList<String>> datiVoli = voloDAO.getVoliPerCittaDestinazione(cittaDestinazione);

        // Effettuiamo sempre questi controlli!
        if (datiVoli.isEmpty()) {
            return new Object[0][0];
        }

        Object[][] risultati = new Object[datiVoli.size()][8];

        for (int i = 0; i < datiVoli.size(); i++) {
            ArrayList<String> datiVolo = datiVoli.get(i);

            // Formatto l'orario con eventuale ritardo
            String orarioCompleto = datiVolo.get(2);        //orario previsto
            int ritardo = Integer.parseInt(datiVolo.get(4));
            if (ritardo > 0) {
                orarioCompleto += " (+" + ritardo + " min)";
            }

            risultati[i][0] = datiVolo.get(0);      //numero volo
            risultati[i][1] = datiVolo.get(1);      //compagnia aerea
            risultati[i][2] = datiVolo.get(6);      //partenza
            risultati[i][3] = datiVolo.get(7);      //destinazione
            risultati[i][4] = datiVolo.get(3);      //data
            risultati[i][5] = orarioCompleto;       //orario con ritardo
            risultati[i][6] = datiVolo.get(5);      //stato
            risultati[i][7] = datiVolo.get(9);      //tipo volo
        }

        return risultati;
    }

    /**
     * Conta i voli che partono da una specifica città
     *
     * @param cittaPartenza the citta partenza
     * @return the int
     */
    public int contaVoliInPartenza(String cittaPartenza) {
        ArrayList<ArrayList<String>> voli = voloDAO.getVoliPerCittaPartenza(cittaPartenza);
        return voli.size();
    }

    /**
     * Conta i voli che arrivano in una specifica città
     *
     * @param cittaDestinazione the citta destinazione
     * @return the int
     */
    public int contaVoliInArrivo(String cittaDestinazione) {
        ArrayList<ArrayList<String>> voli = voloDAO.getVoliPerCittaDestinazione(cittaDestinazione);
        return voli.size();
    }


    /**
     * Ottiene le colonne per la tabella dei voli in partenza
     *
     * @return the string [ ]
     */
    public String[] getColonneVoliPartenza() {
        return new String[]{
                "Numero Volo", "Compagnia", TIPO_VOLO_PARTENZA, "Destinazione",
                "Data", "Orario", "Stato", "Tipo"
        };
    }

    /**
     * Ottiene le colonne per la tabella dei voli in arrivo
     *
     * @return the string [ ]
     */
    public String[] getColonneVoliArrivo() {
        return new String[]{
                "Numero Volo", "Compagnia", TIPO_VOLO_PARTENZA, "Destinazione",
                "Data", "Orario", "Stato", "Tipo"
        };
    }



    // Metodi di conversione che abbiamo utilizzato per rispettare il modello del pattern importo (BCE + DAO come da lezione)
    // Questo è stato necessario per evitare l'importazione delle classi del package "model"
    private Ticket convertiArrayListInTicket(ArrayList<String> datiTicket) {
        try {
            String nome = datiTicket.get(0);                    // nome
            String cognome = datiTicket.get(1);                 // cognome
            String numeroDocumento = datiTicket.get(2);         // numero_documento
            String dataNascita = datiTicket.get(3);             // data_nascita (già formattata)
            String postoAssegnato = datiTicket.get(4);          // posto_assegnato
            String codicePrenotazione = datiTicket.get(5);      // codice_prenotazione

            return new Ticket(nome, cognome, numeroDocumento, dataNascita, postoAssegnato, codicePrenotazione);

        } catch (Exception e) {
            logger.info("Errore nella conversione ticket: " + e.getMessage());
            return null;
        }
    }
    private Prenotazione convertiArrayListInPrenotazione(ArrayList<String> datiPrenotazione) {
        try {
            String codicePrenotazione = datiPrenotazione.get(0);
            String email = datiPrenotazione.get(1);
            String numeroVolo = datiPrenotazione.get(2);
            StatoPrenotazione stato = StatoPrenotazione.valueOf(datiPrenotazione.get(3));
            int numeroPasseggeri = Integer.parseInt(datiPrenotazione.get(4));

            return new Prenotazione(codicePrenotazione, numeroVolo, stato, numeroPasseggeri, email);
        } catch (Exception e) {
            logger.info("Errore nella conversione prenotazione: " + e.getMessage());
            return null;
        }
    }
    private ArrayList<Volo> convertiListaArrayListInVoli(ArrayList<ArrayList<String>> datiVoli) {
        ArrayList<Volo> voli = new ArrayList<>();

        for (ArrayList<String> datiVolo : datiVoli) {
            Volo volo = convertiArrayListInVolo(datiVolo);
            if (volo != null) {
                voli.add(volo);
            }
        }

        return voli;
    }






}
