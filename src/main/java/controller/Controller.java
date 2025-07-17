package controller;

import implementazioniPostgresDAO.*;
import model.*;
import dao.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Controller che fa da intermediario tra il pacchetto GUI e il pacchetto model.
 * Viene utilizzato il pattern 'Singleton' per garantire una singola istanza.
 */
public class Controller {

    private static Controller instance = null;      // istanza Singleton
    private Utente utenteLoggato;                   // Utente corrente

    // DAO per l'accesso ai dati
    private VoloDAO voloDAO = new ImplementazioneVoloDAO();
    private UtenteDAO utenteDAO = new ImplementazioneUtenteDAO();
    private PrenotazioneDAO prenotazioneDAO = new ImplementazionePrenotazioneDAO();
    private TicketDAO ticketDAO = new ImplementazioneTicketDAO();

    /**
     * Costruttore privato per il pattern Singleton
     */
    private Controller() {
        // Inizializza utenti di default nel database se non esistono
        // inizializzaUtentiDefault();
        // Inizializza voli di default nel database se non esistono
        // inizializzaVoliDefault();
    }

    /**
     * Inizializza utenti di default nel database
     */
//    private void inizializzaUtentiDefault() {
//        // Verifica se gli utenti esistono già
//        if (utenteDAO.getUtentePerUsername("user") == null) {
//            UtenteGenerico utente = new UtenteGenerico("user@example.com", "user", "password123");
//            utenteDAO.inserisciUtente(utente);
//        }
//
//        if (utenteDAO.getUtentePerUsername("admin") == null) {
//            Amministratore admin = new Amministratore("admin@example.com", "admin", "admin123");
//            utenteDAO.inserisciUtente(admin);
//        }
//    }

    /**
     * Inizializza voli di default nel database
     */
//    private void inizializzaVoliDefault() {
//        // Verifica se i voli esistono già
//        if (voloDAO.getVoloPerNumero("AZ123") == null) {
//            // Voli in partenza da Napoli
//            voloDAO.inserisciVolo(new VoloInPartenza("AZ123", "Alitalia", "14:30", "29-04-2025", 0, StatoVolo.ATTERRATO, "Milano", (short) 5));
//            voloDAO.inserisciVolo(new VoloInPartenza("FR456", "Ryanair", "16:45", "15-07-2025", 10, StatoVolo.IN_RITARDO, "Roma", (short) 12));
//            voloDAO.inserisciVolo(new VoloInPartenza("LH789", "Lufthansa", "09:15", "19-08-2025", 0, StatoVolo.PROGRAMMATO, "Monaco", (short) 3));
//            voloDAO.inserisciVolo(new VoloInPartenza("BA321", "British Airways", "11:20", "16-07-2025", 45, StatoVolo.IN_RITARDO, "Londra", (short) 8));
//
//            // Voli in arrivo a Napoli
//            voloDAO.inserisciVolo(new VoloInArrivo("EK654", "Emirates", "18:30", "30-07-2025", 0, StatoVolo.PROGRAMMATO, "Dubai"));
//            voloDAO.inserisciVolo(new VoloInArrivo("AF987", "Air France", "20:15", "21-07-2025", 32, StatoVolo.IN_RITARDO, "Parigi"));
//            voloDAO.inserisciVolo(new VoloInArrivo("KL432", "KLM", "13:45", "03-08-2025", 0, StatoVolo.PROGRAMMATO, "Amsterdam"));
//        }
//    }

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
        if (emailUsername == null || password == null ||
                emailUsername.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        utenteLoggato = utenteDAO.getUtentePerCredenziali(emailUsername, password);
        return utenteLoggato != null;
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
        if (email == null || username == null || password == null ||
                email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        // Verifica se l'utente esiste già
        if (utenteDAO.esisteUtente(username, email)) {
            return false;
        }

        // Crea l'utente appropriato
        Utente nuovoUtente;
        if (isAdmin) {
            nuovoUtente = new Amministratore(email, username, password);
        } else {
            nuovoUtente = new UtenteGenerico(email, username, password);
        }

        return utenteDAO.inserisciUtente(nuovoUtente);
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
        return utenteLoggato != null && utenteLoggato instanceof Amministratore;
    }

    /**
     * Determina il tipo di un volo (Partenza/Arrivo) in base alla sua istanza
     * @param volo Il volo da controllare
     * @return "Partenza" se è un volo in partenza, "Arrivo" se è in arrivo
     */
    public String getTipoVolo(Volo volo) {
        if (volo instanceof VoloInPartenza) {
            return "Partenza";
        } else if (volo instanceof VoloInArrivo) {
            return "Arrivo";
        }
        return "Sconosciuto";
    }

    /**
     * Verifica se un volo è in partenza
     * @param volo Il volo da controllare
     * @return true se è un volo in partenza
     */
    public boolean isVoloInPartenza(Volo volo) {
        return volo instanceof VoloInPartenza;
    }

    /**
     * Verifica se un volo è prenotabile
     * @param numeroVolo Il numero del volo
     * @return true se il volo è prenotabile, false altrimenti
     */
    public boolean isVoloPrenotabile(String numeroVolo) {
        Volo volo = voloDAO.getVoloPerNumero(numeroVolo);
        return volo != null && volo.getStato() == StatoVolo.PROGRAMMATO;
    }

    /**
     * Ottiene il gate di imbarco per un volo in partenza
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
     * Aggiorna il gate di imbarco per un volo in partenza
     * @param volo Il volo
     * @param nuovoGate Il nuovo gate
     * @return true se l'aggiornamento è riuscito
     */
    public boolean setGateImbarco(Volo volo, Short nuovoGate) {
        if (volo instanceof VoloInPartenza && isUtenteAdmin()) {
            return voloDAO.aggiornaGateImbarco(volo.getNumeroVolo(), nuovoGate);
        }
        return false;
    }

    /**
     * Restituisce la lista di tutti i voli disponibili
     * @return Lista di voli
     */
    public ArrayList<Volo> getListaVoli() {
        List<Volo> voli = voloDAO.getVoliDisponibili();
        return new ArrayList<>(voli);
    }

    /**
     * Inserisce un nuovo volo (disponibile solo per amministratori)
     */
    public boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                                 String data, String stato, String partenza, String destinazione,
                                 Short gateImbarco) {
        if (!isUtenteAdmin()) {
            return false;
        }

        try {
            StatoVolo statoVolo = StatoVolo.valueOf(stato.toUpperCase());

            Volo nuovoVolo;
            if ("Napoli".equalsIgnoreCase(partenza)) {
                // Volo in partenza da Napoli
                nuovoVolo = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
                        data, 0, statoVolo, destinazione, gateImbarco);
            } else {
                // Volo in arrivo a Napoli
                nuovoVolo = new VoloInArrivo(numeroVolo, compagniaAerea, orarioPrevisto,
                        data, 0, statoVolo, partenza);
            }

            return voloDAO.inserisciVolo(nuovoVolo);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Aggiorna il gate di imbarco di un volo in partenza (disponibile solo per amministratori)
     */
    public boolean aggiornaGateImbarco(String numeroVolo, Short nuovoGate) {
        if (!isUtenteAdmin()) {
            return false;
        }
        return voloDAO.aggiornaGateImbarco(numeroVolo, nuovoGate);
    }

    /**
     * Crea una nuova prenotazione per un volo
     */
    public Prenotazione creaPrenotazione(String codiceVolo, int numeroPasseggeri, String email) {
        if (utenteLoggato == null || !isVoloPrenotabile(codiceVolo)) {
            return null;
        }

        String codicePrenotazione = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String dataPrenotazione = LocalDate.now().toString();

        return new Prenotazione(codicePrenotazione, codiceVolo, StatoPrenotazione.CONFERMATA,
                                numeroPasseggeri, email);
    }

    /**
     * Aggiunge un ticket a una prenotazione esistente
     */
    public boolean aggiungiTicket(Prenotazione prenotazione, String nome, String cognome,
                                  String numeroDocumento, String dataNascita, String postoAssegnato) {
        if (prenotazione == null) {
            return false;
        }

        Ticket nuovoTicket = new Ticket(nome, cognome, numeroDocumento, dataNascita,
                postoAssegnato, prenotazione.getCodicePrenotazione());

        return ticketDAO.inserisciTicket(nuovoTicket);
    }

    /**
     * Ottiene un volo per numero
     */
    public Volo getVoloPerNumero(String numeroVolo) {
        return voloDAO.getVoloPerNumero(numeroVolo);
    }

    /**
     * Ottiene tutti i voli per l'amministratore
     */
    public ArrayList<Volo> getTuttiIVoli() {
        List<Volo> voli = voloDAO.getTuttiVoli();
        return new ArrayList<>(voli);
    }

    /**
     * Completa una prenotazione aggiungendo tutti i ticket necessari
     */
    public String completaPrenotazione(String codiceVolo, int numeroPasseggeri, String[][] datiPasseggeri) {
        if (utenteLoggato == null || !isVoloPrenotabile(codiceVolo)) {
            return null;
        }

        // Crea la prenotazione
        Prenotazione nuovaPrenotazione = creaPrenotazione(codiceVolo, numeroPasseggeri, utenteLoggato.getEmail());
        if (nuovaPrenotazione == null) {
            return null;
        }

        // Inserisci la prenotazione nel database
        if (!prenotazioneDAO.inserisciPrenotazione(nuovaPrenotazione)) {
            return null;
        }

        // Aggiungi i ticket
        for (int i = 0; i < numeroPasseggeri; i++) {
            String nome = datiPasseggeri[i][0];
            String cognome = datiPasseggeri[i][1];
            String documento = datiPasseggeri[i][2];
            String dataNascita = datiPasseggeri[i][3];
            String posto = generaPostoCasuale();

            if (!aggiungiTicket(nuovaPrenotazione, nome, cognome, documento, dataNascita, posto)) {
                return null;
            }
        }

        return nuovaPrenotazione.getCodicePrenotazione();
    }

    /**
     * Valida il formato di una data
     */
    public boolean isValidDateFormat(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(data, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Genera un posto casuale da A1 a T6
     */
    private String generaPostoCasuale() {
        Random random = new Random();
        char lettera = (char) ('A' + random.nextInt(20)); // A-T
        int numero = random.nextInt(6) + 1; // 1-6
        return String.valueOf(lettera) + numero;
    }

    /**
     * Ottiene i dettagli dei ticket di una prenotazione
     */
    public String[][] getTicketsPrenotazione(String codicePrenotazione) {
        List<Ticket> tickets = ticketDAO.getTicketsPerPrenotazione(codicePrenotazione);

        String[][] risultato = new String[tickets.size()][5];
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            risultato[i][0] = ticket.getNome();
            risultato[i][1] = ticket.getCognome();
            risultato[i][2] = ticket.getNumeroDocumento();
            risultato[i][3] = ticket.getDataNascita();
            risultato[i][4] = ticket.getPostoAssegnato();
        }

        return risultato;
    }

    /**
     * Ottiene i dati delle prenotazioni dell'utente loggato per la visualizzazione in tabella
     */
    public Object[][] getDatiPrenotazioniUtente() {
        if (utenteLoggato == null) {
            return new Object[0][0];
        }

        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniPerUtente(utenteLoggato.getEmail());

        Object[][] dati = new Object[prenotazioni.size()][8];
        for (int i = 0; i < prenotazioni.size(); i++) {
            Prenotazione p = prenotazioni.get(i);

            // Otteniamo i dettagli del volo
            Volo volo = voloDAO.getVoloPerNumero(p.getCodiceVolo());

            String tratta = "";
            String statoVolo = "";
            String orario = "";
            String ritardo = "";            // In caso di errore con il volo ( == null ), avremo stringhe vuote, il che è "accettabile" rispetto a valori sporchi o non corretti!

            if (volo != null) {
                tratta = volo.getPartenza() + " → " + volo.getDestinazione();
                statoVolo = volo.getStato().toString();
                orario = volo.getOrarioPrevisto();
                ritardo = volo.getRitardo() > 0 ? "+" + volo.getRitardo() + " min" : "";        // Se non c'è ritardo, non visualizziamo dettagli in quelal casella!
            }

            dati[i][0] = p.getCodicePrenotazione();        // Codice Prenotazione
            dati[i][1] = p.getNumeroPasseggeri();          // Numero Passeggeri
            dati[i][2] = p.getStato().toString();          // Stato Prenotazione
            dati[i][3] = p.getCodiceVolo();                // Numero Volo
            dati[i][4] = tratta;                           // Tratta
            dati[i][5] = statoVolo;                        // Stato Volo
            dati[i][6] = orario;                           // Orario
            dati[i][7] = ritardo;                          // Ritardo

        }

        return dati;
    }

    /**
     * Ottiene il numero di prenotazioni dell'utente loggato
     */
    public int getNumeroPrenotazioniUtente() {
        if (utenteLoggato == null) {
            return 0;
        }

        List<Prenotazione> prenotazioni = prenotazioneDAO.getPrenotazioniPerUtente(utenteLoggato.getEmail());
        return prenotazioni.size();
    }

    /**
     * Ottiene i dettagli di una prenotazione per codice
     */
    public String[] getDettagliPrenotazione(String codicePrenotazione) {

        // Otteniamo i dettagli della prenotazione (e verifichiamo che esista)
        Prenotazione prenotazione = prenotazioneDAO.getPrenotazionePerCodice(codicePrenotazione);
        if (prenotazione == null) {
            return null;
        }

        // Stessa cosa per il volo, otteniamo i dettagli e verifichiamo l'esistenza
        Volo volo = voloDAO.getVoloPerNumero(prenotazione.getCodiceVolo());
        String tratta = "";
        String dataVolo = "";
        if (volo != null) {
            tratta = volo.getPartenza() + " → " + volo.getDestinazione();
            dataVolo = volo.getData();
        }


        return new String[] {
                prenotazione.getCodicePrenotazione(),
                prenotazione.getCodiceVolo(),
                dataVolo,
                tratta,
                prenotazione.getStato().toString(),
                String.valueOf(prenotazione.getNumeroPasseggeri())
        };
    }

    /**
     * Ottiene una prenotazione per codice
     */
    public Prenotazione getPrenotazionePerCodice(String codicePrenotazione) {
        return prenotazioneDAO.getPrenotazionePerCodice(codicePrenotazione);
    }

    /**
     * Ottiene i dettagli di una prenotazione per la visualizzazione nel dialog
     */
    public String[] getDettagliPrenotazioneDialog(Object prenotazione) {
        if (prenotazione instanceof Prenotazione) {
            Prenotazione p = (Prenotazione) prenotazione;
            return new String[] {
                    p.getCodicePrenotazione(),
                    p.getCodiceVolo(),
                    p.getStato().toString(),
                    String.valueOf(p.getNumeroPasseggeri()),
                    p.getEmail()
            };
        }
        return null;
    }

    /**
     * Ottiene la lista dei ticket di una prenotazione formattata per la visualizzazione
     */
    public String[] getTicketsFormattati(Object prenotazione) {
        if (prenotazione instanceof Prenotazione) {
            Prenotazione p = (Prenotazione) prenotazione;
            List<Ticket> tickets = ticketDAO.getTicketsPerPrenotazione(p.getCodicePrenotazione());

            String[] risultato = new String[tickets.size()];
            for (int i = 0; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                risultato[i] = ticket.getNome() + " " + ticket.getCognome() + " - Posto: " + ticket.getPostoAssegnato();
            }
            return risultato;
        }
        return new String[0];
    }

    /**
     * Ottiene un ticket specifico da una prenotazione per indice
     */
    public Object getTicketPerIndice(Object prenotazione, int indice) {
        if (prenotazione instanceof Prenotazione) {
            Prenotazione p = (Prenotazione) prenotazione;
            List<Ticket> tickets = ticketDAO.getTicketsPerPrenotazione(p.getCodicePrenotazione());

            if (indice >= 0 && indice < tickets.size()) {
                return tickets.get(indice);
            }
        }
        return null;
    }

    /**
     * Elimina una prenotazione dell'utente loggato
     */
    public boolean eliminaPrenotazione(Object prenotazione) {
        if (utenteLoggato == null || !(prenotazione instanceof Prenotazione)) {
            return false;
        }

        Prenotazione p = (Prenotazione) prenotazione;

        // Prima elimina i ticket
        if (!ticketDAO.eliminaTicketsPerPrenotazione(p.getCodicePrenotazione())) {
            return false;
        }

        // Poi elimina la prenotazione
        return prenotazioneDAO.eliminaPrenotazione(p.getCodicePrenotazione());
    }

    /**
     * Aggiorna i dati di un ticket
     */
    public boolean aggiornaTicket(Object ticket, String nome, String cognome,
                                  String numeroDocumento, String dataNascita, String postoAssegnato) {
        if (!(ticket instanceof Ticket)) {
            return false;
        }

        Ticket t = (Ticket) ticket;
        t.setNome(nome);
        t.setCognome(cognome);
        t.setNumeroDocumento(numeroDocumento);
        t.setDataNascita(dataNascita);
        // Il posto assegnato non si può cambiare facilmente per vincoli di unicità

        return ticketDAO.aggiornaTicket(t);
    }

    /**
     * Ottiene i dettagli di un ticket per la visualizzazione nel dialog di modifica
     */
    public String[] getDettagliTicket(Object ticket) {
        if (ticket instanceof Ticket) {
            Ticket t = (Ticket) ticket;
            return new String[] {
                    t.getNome(),
                    t.getCognome(),
                    t.getNumeroDocumento(),
                    t.getDataNascita(),
                    t.getPostoAssegnato()
            };
        }
        return null;
    }

    /**
     * Ottiene i dati di un volo come array di stringhe per la GUI
     */
    public String[] getDatiVolo(String numeroVolo) {
        Volo volo = voloDAO.getVoloPerNumero(numeroVolo);
        if (volo == null) {
            return null;
        }

        return new String[] {
                volo.getNumeroVolo(),
                volo.getCompagniaAerea(),
                volo.getOrarioPrevisto(),
                volo.getData(),
                String.valueOf(volo.getRitardo()),
                volo.getStato().toString(),
                volo.getPartenza(),
                volo.getDestinazione()
        };
    }

    /**
     * Aggiorna tutti i dati di un volo (disponibile solo per amministratori)
     */
    public boolean aggiornaVolo(String numeroVoloOriginale, String nuovoNumeroVolo,
                                String nuovaCompagnia, String nuovoOrario, String nuovaData,
                                int nuovoRitardo, String nuovoStato, String nuovaPartenza,
                                String nuovaDestinazione) {
        if (!isUtenteAdmin()) {
            return false;
        }

        try {
            Volo volo = voloDAO.getVoloPerNumero(numeroVoloOriginale);
            if (volo == null) {
                return false;
            }

            // Aggiorna i dati del volo
            volo.setCompagniaAerea(nuovaCompagnia);
            volo.setOrarioPrevisto(nuovoOrario);
            volo.setData(nuovaData);
            volo.setRitardo(nuovoRitardo);
            volo.setStato(StatoVolo.valueOf(nuovoStato.toUpperCase()));
            volo.setPartenza(nuovaPartenza);
            volo.setDestinazione(nuovaDestinazione);

            return voloDAO.aggiornaVolo(volo);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ottiene il gate di un volo come stringa
     */
    public String getGateVoloStringa(String numeroVolo) {
        Volo volo = voloDAO.getVoloPerNumero(numeroVolo);
        if (volo instanceof VoloInPartenza) {
            Short gate = ((VoloInPartenza) volo).getGateImbarco();
            return gate != null ? gate.toString() : "N/A";
        }
        return "N/A";
    }

    // Metodi per gestire gli Stati del Volo
    public String[] getStatiVoloDisponibili() {
        StatoVolo[] stati = StatoVolo.values();
        String[] risultato = new String[stati.length];
        for (int i = 0; i < stati.length; i++) {
            risultato[i] = stati[i].toString();
        }
        return risultato;
    }

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

    // Metodi per gestire gli Stati della Prenotazione
    public String[] getStatiPrenotazioneDisponibili() {
        StatoPrenotazione[] stati = StatoPrenotazione.values();
        String[] risultato = new String[stati.length];
        for (int i = 0; i < stati.length; i++) {
            risultato[i] = stati[i].toString();
        }
        return risultato;
    }

    public boolean isStatoPrenotazioneValido(String stato) {
        try {
            StatoPrenotazione.valueOf(stato.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private StatoPrenotazione stringToStatoPrenotazione(String stato) {
        return StatoPrenotazione.valueOf(stato.toUpperCase());
    }

    /**
     * Ottiene i voli che partono da una specifica città per visualizzazione in tabella
     */
    public Object[][] getVoliInPartenza(String citta) {
        List<Volo> voli = voloDAO.getTuttiVoli();
        List<Volo> voliPartenza = new ArrayList<>();

        for (Volo volo : voli) {
            if (volo instanceof VoloInPartenza && citta.equalsIgnoreCase(volo.getPartenza())) {
                voliPartenza.add(volo);
            }
        }

        Object[][] dati = new Object[voliPartenza.size()][7];
        for (int i = 0; i < voliPartenza.size(); i++) {
            Volo volo = voliPartenza.get(i);
            dati[i][0] = volo.getNumeroVolo();
            dati[i][1] = volo.getCompagniaAerea();
            dati[i][2] = volo.getOrarioPrevisto();
            dati[i][3] = volo.getData();
            dati[i][4] = volo.getStato().toString();
            dati[i][5] = volo.getDestinazione();
            dati[i][6] = getGateVoloStringa(volo.getNumeroVolo());
        }

        return dati;
    }

    /**
     * Ottiene i voli che arrivano verso una specifica città per visualizzazione in tabella
     */
    public Object[][] getVoliInArrivo(String citta) {
        List<Volo> voli = voloDAO.getTuttiVoli();
        List<Volo> voliArrivo = new ArrayList<>();

        for (Volo volo : voli) {
            if (volo instanceof VoloInArrivo && citta.equalsIgnoreCase(volo.getDestinazione())) {
                voliArrivo.add(volo);
            }
        }

        Object[][] dati = new Object[voliArrivo.size()][6];
        for (int i = 0; i < voliArrivo.size(); i++) {
            Volo volo = voliArrivo.get(i);
            dati[i][0] = volo.getNumeroVolo();
            dati[i][1] = volo.getCompagniaAerea();
            dati[i][2] = volo.getOrarioPrevisto();
            dati[i][3] = volo.getData();
            dati[i][4] = volo.getStato().toString();
            dati[i][5] = volo.getPartenza();
        }

        return dati;
    }

    /**
     * Conta i voli in partenza da una specifica città
     */
    public int contaVoliInPartenza(String citta) {
        return getVoliInPartenza(citta).length;
    }

    /**
     * Conta i voli in arrivo verso una specifica città
     */
    public int contaVoliInArrivo(String citta) {
        return getVoliInArrivo(citta).length;
    }

    /**
     * Ottiene le colonne per la tabella dei voli in partenza
     */
    public String[] getColonneVoliPartenza() {
        return new String[] {
                "Numero Volo", "Compagnia", "Orario", "Data", "Stato", "Destinazione", "Gate"
        };
    }

    /**
     * Ottiene le colonne per la tabella dei voli in arrivo
     */
    public String[] getColonneVoliArrivo() {
        return new String[] {
                "Numero Volo", "Compagnia", "Orario", "Data", "Stato", "Provenienza"
        };
    }

    /**
     * Ottiene la tratta di un volo
     */
    public String getTrattaDelVolo(String codiceVolo) {
        Volo volo = voloDAO.getVoloPerNumero(codiceVolo);
        if (volo == null) {
            return "N/A";
        }
        return volo.getPartenza() + " → " + volo.getDestinazione();
    }

    // Metodi aggiuntivi che potrebbero essere utili

    /**
     * Ottiene tutti i voli disponibili utilizzando DAO
     */
    public List<Volo> getVoliDisponibili() {
        return voloDAO.getVoliDisponibili();
    }

    /**
     * Login utente specifico per retrocompatibilità
     */
    public boolean loginUtente(String emailUsername, String password) {
        return login(emailUsername, password);
    }

    /**
     * Elimina un volo (solo per amministratori)
     */
    public boolean eliminaVolo(String numeroVolo) {
        if (!isUtenteAdmin()) {
            return false;
        }
        return voloDAO.eliminaVolo(numeroVolo);
    }

    /**
     * Verifica se un volo esiste
     */
    public boolean esisteVolo(String numeroVolo) {
        return voloDAO.esisteVolo(numeroVolo);
    }

    /**
     * Ottiene tutti gli utenti (solo per amministratori)
     */
    public List<Utente> getTuttiUtenti() {
        if (!isUtenteAdmin()) {
            return new ArrayList<>();
        }
        return utenteDAO.getTuttiUtenti();
    }

    /**
     * Ottiene tutte le prenotazioni (solo per amministratori)
     */
    public List<Prenotazione> getTuttePrenotazioni() {
        if (!isUtenteAdmin()) {
            return new ArrayList<>();
        }
        return prenotazioneDAO.getTuttePrenotazioni();
    }
}
