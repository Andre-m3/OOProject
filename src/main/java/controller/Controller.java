package controller;

import model.*;

import java.util.ArrayList;
import java.util.Random;

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
        // Voli in partenza da Napoli
        Volo.aggiungiVolo(new VoloInPartenza("AZ123", "Alitalia", "14:30", "29-04-2025", 0, "ATTERRATO", "Milano", (short) 5));
        Volo.aggiungiVolo(new VoloInPartenza("FR456", "Ryanair", "16:45", "15-07-2025", 10, "RITARDATO", "Roma", (short) 12));
        Volo.aggiungiVolo(new VoloInPartenza("LH789", "Lufthansa", "09:15", "19-08-2025", 0, "PROGRAMMATO", "Monaco"));
        Volo.aggiungiVolo(new VoloInPartenza("BA321", "British Airways", "11:20", "16-07-2025", 45, "RITARDATO", "Londra", (short) 8));

        // Voli in arrivo a Napoli
        Volo.aggiungiVolo(new VoloInArrivo("EK654", "Emirates", "18:30", "30-07-2025", 0, "PROGRAMMATO", "Dubai"));
        Volo.aggiungiVolo(new VoloInArrivo("AF987", "Air France", "20:15", "21-07-2025", 32, "RITARDATO", "Parigi"));
        Volo.aggiungiVolo(new VoloInArrivo("KL432", "KLM", "13:45", "03-08-2025", 0, "PROGRAMMATO", "Amsterdam"));
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
     * Determina il tipo di un volo (Partenza/Arrivo) in base alla sua istanza
     * @param volo Il volo da controllare
     * @return "Partenza" se è un volo in partenza, "Arrivo" se è in arrivo
     */
    public String getTipoVolo(Volo volo) {
        if (volo instanceof VoloInPartenza) {
            return "Partenza";
        } else if (volo instanceof VoloInArrivo) {
            return "Arrivo";
        } else {
            // Per voli generici, determina in base a partenza/destinazione
            if ("Napoli".equalsIgnoreCase(volo.getPartenza())) {
                return "Partenza";
            } else if ("Napoli".equalsIgnoreCase(volo.getDestinazione())) {
                return "Arrivo";
            } else {
                return "Generico";
            }
        }
    }

    /**
     * Verifica se un volo è in partenza da Napoli
     * @param volo Il volo da controllare
     * @return true se è un volo in partenza
     */
    public boolean isVoloInPartenza(Volo volo) {
        return volo instanceof VoloInPartenza || "Napoli".equalsIgnoreCase(volo.getPartenza());
    }

    /**
     * Verifica se un volo è prenotabile
     * @param numeroVolo Il numero del volo
     * @return true se il volo è prenotabile, false altrimenti
     */
    public boolean isVoloPrenotabile(String numeroVolo) {
        ArrayList<Volo> voli = Volo.getListaVoli();

        for (Volo volo : voli) {
            if (volo.getNumeroVolo().equals(numeroVolo)) {
                // Un volo è disponibile alla prenotazione se è PROGRAMMATO
                String stato = volo.getStato();
                return stato.equals("PROGRAMMATO");
            }
        }

        return false;
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
        if (volo instanceof VoloInPartenza) {
            ((VoloInPartenza) volo).setGateImbarco(nuovoGate);
            return true;
        }
        return false;
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
     * @param partenza Aeroporto di partenza
     * @param destinazione Aeroporto di destinazione
     * @param gateImbarco Gate di imbarco (opzionale, solo per voli in partenza)
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    public boolean inserisciVolo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                                 String data, String stato, String partenza, String destinazione,
                                 Short gateImbarco) {

        if (!isUtenteAdmin()) {
            return false;
        }

        try {
            Volo nuovoVolo;

            if ("Napoli".equalsIgnoreCase(partenza)) {
                // Volo in partenza da Napoli
                if (gateImbarco != null) {
                    nuovoVolo = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
                            data, 0, stato, destinazione, gateImbarco);
                } else {
                    nuovoVolo = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
                            data, 0, stato, destinazione);
                }
            } else {
                // Volo in arrivo a Napoli
                nuovoVolo = new VoloInArrivo(numeroVolo, compagniaAerea, orarioPrevisto,
                        data, 0, stato, partenza);
            }


            Volo.aggiungiVolo(nuovoVolo);
            return true;

        } catch (Exception e) {
            return false;
        }
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
     */
    public Prenotazione creaPrenotazione(String codiceVolo, int numeroPasseggeri) {

        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return null;
        }

        if (numeroPasseggeri <= 0 || numeroPasseggeri > Prenotazione.MAX_PASSEGGERI) {
            return null;
        }

        // Trova il volo
        Volo volo = getVoloPerNumero(codiceVolo);
        if (volo == null || !"PROGRAMMATO".equalsIgnoreCase(volo.getStato())) {
            return null;
        }

        // Crea la stringa della tratta
        String tratta = volo.getPartenza() + " → " + volo.getDestinazione();

        // Crea la prenotazione
        String codicePrenotazione = Prenotazione.generaNuovoCodice();
        Prenotazione nuovaPrenotazione = new Prenotazione(
                codicePrenotazione,
                codiceVolo,
                volo.getData(),
                tratta,
                "CONFERMATA",
                numeroPasseggeri
        );

        // Aggiungi la prenotazione all'utente
        ((UtenteGenerico) utenteLoggato).setPrenotazione(nuovaPrenotazione);

        return nuovaPrenotazione;
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
     * Ottiene tutti i voli per l'amministratore (sia passati che futuri)
     * In via temporanea, è disponibile anche all'utente generico (visualizza voli)
     * Ottenere anche i voli passati potrebbe risultare utile a un amministratore!
     * @return Lista di tutti i voli
     */
    public ArrayList<Volo> getTuttiIVoli() {
        return Volo.getListaVoli();
    }

    /**
     * Completa una prenotazione aggiungendo tutti i ticket necessari
     * @param codiceVolo Codice del volo
     * @param numeroPasseggeri Numero di passeggeri
     * @param datiPasseggeri Array di dati dei passeggeri [nome, cognome, documento, dataNascita] per ogni passeggero
     * @return Il codice della prenotazione creata o null se fallita
     */
    public String completaPrenotazione(String codiceVolo, int numeroPasseggeri, String[][] datiPasseggeri) {
        // Crea la prenotazione
        Prenotazione prenotazione = creaPrenotazione(codiceVolo, numeroPasseggeri);
        if (prenotazione == null) {
            return null;
        }

        // Aggiungi tutti i ticket
        for (int i = 0; i < datiPasseggeri.length; i++) {
            String nome = datiPasseggeri[i][0];
            String cognome = datiPasseggeri[i][1];
            String documento = datiPasseggeri[i][2];
            String dataNascita = datiPasseggeri[i][3];

            // Genera posto casuale
            String posto = generaPostoCasuale();

            boolean ticketAggiunto = aggiungiTicket(prenotazione, nome, cognome, documento, dataNascita, posto);
            if (!ticketAggiunto) {
                return null; // Fallimento nella creazione del ticket
            }
        }

        return prenotazione.getCodicePrenotazione();
    }

    /**
     * Valida il formato di una data
     * @param data Data da validare nel formato dd/mm/yyyy
     * @return true se la data è valida, false altrimenti
     */
    public boolean isValidDateFormat(String data) {
        // Controllo base del formato dd/mm/yyyy
        if (data == null || data.length() != 10) return false;
        if (data.charAt(2) != '/' || data.charAt(5) != '/') return false;

        try {
            String[] parti = data.split("/");
            if (parti.length != 3) return false;

            int giorno = Integer.parseInt(parti[0]);
            int mese = Integer.parseInt(parti[1]);
            int anno = Integer.parseInt(parti[2]);

            // Validazione base dei valori
            if (giorno < 1 || giorno > 31) return false;
            if (mese < 1 || mese > 12) return false;
            if (anno < 1900 || anno > 2030) return false;

            // Validazione più specifica per i mesi
            if (mese == 2) { // Febbraio
                boolean bisestile = (anno % 4 == 0 && anno % 100 != 0) || (anno % 400 == 0);
                if (giorno > (bisestile ? 29 : 28)) return false;
            } else if (mese == 4 || mese == 6 || mese == 9 || mese == 11) { // Mesi con 30 giorni
                if (giorno > 30) return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Genera un posto casuale da A1 a T6
     * @return Posto alfanumerico casuale
     */
    private String generaPostoCasuale() {
        // Lettere da 'A' a 'T' (20 lettere, che corrispondono a 20 ipotetiche file dell'aereo)
        char[] lettere = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

        // Numeri da 1 a 6
        int[] numeri = {1, 2, 3, 4, 5, 6};

        Random random = new Random();
        char lettera = lettere[random.nextInt(lettere.length)];
        int numero = numeri[random.nextInt(numeri.length)];

        return lettera + String.valueOf(numero);

    }

    /**
     * Ottiene i dettagli dei ticket di una prenotazione
     * @param codicePrenotazione Codice della prenotazione
     * @return Array di array con i dettagli dei ticket [nome, cognome, documento, dataNascita, posto]
     */
    public String[][] getTicketsPrenotazione(String codicePrenotazione) {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return null;
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        for (Prenotazione prenotazione : utente.getPrenotazioni()) {
            if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                ArrayList<Ticket> tickets = prenotazione.getTickets();
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
        }
        return null;
    }



    /**
     * Ottiene i dati delle prenotazioni dell'utente loggato per la visualizzazione in tabella
     * @return Array bidimensionale con i dati delle prenotazioni
     */
    public Object[][] getDatiPrenotazioniUtente() {
        if (utenteLoggato == null || !(utenteLoggato instanceof UtenteGenerico)) {
            return new Object[0][8]; // Tabella vuota
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        ArrayList<Prenotazione> prenotazioni = utente.getPrenotazioni();

        Object[][] dati = new Object[prenotazioni.size()][8];

        for (int i = 0; i < prenotazioni.size(); i++) {
            Prenotazione prenotazione = prenotazioni.get(i);
            String[] datiVolo = getDatiVolo(prenotazione.getCodiceVolo());

            dati[i][0] = prenotazione.getCodicePrenotazione();
            dati[i][1] = prenotazione.getNumeroPasseggeri();
            dati[i][2] = prenotazione.getStato().toString();
            dati[i][3] = prenotazione.getCodiceVolo();
            dati[i][4] = prenotazione.getPartenzaDestinazione();
            dati[i][5] = datiVolo != null ? datiVolo[5] : "N/A";
            dati[i][6] = datiVolo != null ? datiVolo[2] : "N/A";
            dati[i][7] = datiVolo != null ?
                    (Integer.parseInt(datiVolo[4]) > 0 ? datiVolo[4] + " min" : "Nessun ritardo") : "N/A";
        }

        return dati;
    }

    /**
     * Ottiene il numero di prenotazioni dell'utente loggato
     * @return Numero di prenotazioni
     */
    public int getNumeroPrenotazioniUtente() {
        if (utenteLoggato == null || !(utenteLoggato instanceof UtenteGenerico)) {
            return 0;
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        return utente.getPrenotazioni().size();
    }


    /**
     * Ottiene i dettagli di una prenotazione per codice
     * @param codicePrenotazione Codice della prenotazione
     * @return Array con i dettagli della prenotazione o null se non trovata
     * [codice, volo, data, tratta, stato, numeroPasseggeri]
     */
    public String[] getDettagliPrenotazione(String codicePrenotazione) {
        if (!(utenteLoggato instanceof UtenteGenerico)) {
            return null;
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;

        // Cerchiamo la prenotazione tra quelle dell'utente
        for (Prenotazione prenotazione : utente.getPrenotazioni()) {
            if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                return new String[] {
                        prenotazione.getCodicePrenotazione(),
                        prenotazione.getCodiceVolo(),
                        prenotazione.getDataVolo(),
                        prenotazione.getPartenzaDestinazione(),
                        prenotazione.getStato(),
                        String.valueOf(prenotazione.getNumeroPasseggeri())
                };
            }
        }

        return null; // Prenotazione non trovata
    }

    /**
     * Ottiene una prenotazione per codice (per compatibilità con i dialog esistenti)
     * @param codicePrenotazione Codice della prenotazione
     * @return La prenotazione trovata o null se non esiste
     */
    public Prenotazione getPrenotazionePerCodice(String codicePrenotazione) {
        if (utenteLoggato == null || !(utenteLoggato instanceof UtenteGenerico)) {
            return null;
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        ArrayList<Prenotazione> prenotazioni = utente.getPrenotazioni();

        for (Prenotazione prenotazione : prenotazioni) {
            if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                return prenotazione;
            }
        }

        return null;
    }




    /**
     * Ottiene i dettagli di una prenotazione per la visualizzazione nel dialog
     * @param prenotazione La prenotazione (passata come Object per evitare import)
     * @return Array con i dettagli [codice, volo, data, tratta, stato]
     */
    public String[] getDettagliPrenotazioneDialog(Object prenotazione) {
        if (!(prenotazione instanceof Prenotazione)) {
            return null;
        }

        Prenotazione p = (Prenotazione) prenotazione;
        return new String[] {
                p.getCodicePrenotazione(),
                p.getCodiceVolo(),
                p.getDataVolo(),
                p.getPartenzaDestinazione(),
                p.getStato()
        };
    }


    /**
     * Ottiene la lista dei ticket di una prenotazione formattata per la visualizzazione
     * @param prenotazione La prenotazione (passata come Object)
     * @return Array di stringhe con i dati dei ticket formattati
     */
    public String[] getTicketsFormattati(Object prenotazione) {
        if (prenotazione == null || !(prenotazione instanceof Prenotazione)) {
            return new String[0];
        }

        Prenotazione p = (Prenotazione) prenotazione;
        ArrayList<Ticket> tickets = p.getTickets();
        String[] ticketsFormattati = new String[tickets.size()];

        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            ticketsFormattati[i] = ticket.getNome() + " " + ticket.getCognome() +
                    " - " + ticket.getNumeroDocumento();
        }

        return ticketsFormattati;
    }

    /**
     * Ottiene un ticket specifico da una prenotazione per indice
     * @param prenotazione La prenotazione (passata come Object)
     * @param indice L'indice del ticket nella lista
     * @return Il ticket o null se non trovato
     */
    public Object getTicketPerIndice(Object prenotazione, int indice) {
        if (prenotazione == null || !(prenotazione instanceof Prenotazione)) {
            return null;
        }

        Prenotazione p = (Prenotazione) prenotazione;
        ArrayList<Ticket> tickets = p.getTickets();

        if (indice < 0 || indice >= tickets.size()) {
            return null;
        }

        return tickets.get(indice);
    }

    /**
     * Elimina una prenotazione dell'utente loggato
     * @param prenotazione La prenotazione da eliminare (passata come Object)
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    public boolean eliminaPrenotazione(Object prenotazione) {
        if (!(utenteLoggato instanceof UtenteGenerico) || prenotazione == null || !(prenotazione instanceof Prenotazione)) {
            return false;
        }

        UtenteGenerico utente = (UtenteGenerico) utenteLoggato;
        Prenotazione p = (Prenotazione) prenotazione;

        return utente.getPrenotazioni().remove(p);
    }


    /**
     * Aggiorna i dati di un ticket
     * @param ticket Il ticket da aggiornare (passato come Object)
     * @param nome Nuovo nome
     * @param cognome Nuovo cognome
     * @param numeroDocumento Nuovo numero documento
     * @param dataNascita Nuova data di nascita
     * @param postoAssegnato Nuovo posto assegnato
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
     */
    public boolean aggiornaTicket(Object ticket, String nome, String cognome,
                                  String numeroDocumento, String dataNascita, String postoAssegnato) {
        if (ticket == null || !(ticket instanceof Ticket)) {
            return false;
        }

        try {
            Ticket t = (Ticket) ticket;
            t.setNome(nome);
            t.setCognome(cognome);
            t.setNumeroDocumento(numeroDocumento);
            t.setDataNascita(dataNascita);
            t.setPostoAssegnato(postoAssegnato);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ottiene i dettagli di un ticket per la visualizzazione nel dialog di modifica
     * @param ticket Il ticket (passato come Object)
     * @return Array con i dettagli [nome, cognome, documento, dataNascita, posto]
     */
    public String[] getDettagliTicket(Object ticket) {
        if (ticket == null || !(ticket instanceof Ticket)) {
            return null;
        }

        Ticket t = (Ticket) ticket;
        return new String[] {
                t.getNome(),
                t.getCognome(),
                t.getNumeroDocumento(),
                t.getDataNascita(),
                t.getPostoAssegnato()
        };
    }



    /**
     * Ottiene i dati di un volo come array di stringhe per la GUI
     * @param numeroVolo Numero del volo da cercare
     * @return Array con i dati del volo o null se non trovato
     * [0] = numeroVolo, [1] = compagnia, [2] = orario, [3] = data,
     * [4] = ritardo, [5] = stato, [6] = partenza, [7] = destinazione
     */
    public String[] getDatiVolo(String numeroVolo) {
        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(numeroVolo)) {
                return new String[] {
                        volo.getNumeroVolo(),
                        volo.getCompagniaAerea(),
                        volo.getOrarioPrevisto(),
                        volo.getData(),
                        String.valueOf(volo.getRitardo()),
                        volo.getStato(),
                        volo.getPartenza(),
                        volo.getDestinazione()
                };
            }
        }
        return null;
    }

    /**
     * Aggiorna tutti i dati di un volo (disponibile solo per amministratori)
     * @param numeroVoloOriginale Numero del volo da modificare
     * @param nuovoNumeroVolo Nuovo numero volo
     * @param nuovaCompagnia Nuova compagnia
     * @param nuovoOrario Nuovo orario
     * @param nuovaData Nuova data
     * @param nuovoRitardo Nuovo ritardo
     * @param nuovoStato Nuovo stato
     * @param nuovaPartenza Nuova partenza
     * @param nuovaDestinazione Nuova destinazione
     * @return true se l'aggiornamento è riuscito
     */
    public boolean aggiornaVolo(String numeroVoloOriginale, String nuovoNumeroVolo,
                                String nuovaCompagnia, String nuovoOrario, String nuovaData,
                                int nuovoRitardo, String nuovoStato, String nuovaPartenza,
                                String nuovaDestinazione) {
        if (!isUtenteAdmin()) {
            return false;
        }

        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(numeroVoloOriginale)) {
                volo.setNumeroVolo(nuovoNumeroVolo);
                volo.setCompagniaAerea(nuovaCompagnia);
                volo.setOrarioPrevisto(nuovoOrario);
                volo.setData(nuovaData);
                volo.setRitardo(nuovoRitardo);
                volo.setStato(nuovoStato);
                volo.setPartenza(nuovaPartenza);
                volo.setDestinazione(nuovaDestinazione);
                return true;
            }
        }
        return false;
    }

    /**
     * Ottiene il gate di un volo come stringa
     * @param numeroVolo Numero del volo
     * @return Gate come stringa o null se non disponibile
     */
    public String getGateVoloStringa(String numeroVolo) {
        for (Volo volo : Volo.getListaVoli()) {
            if (volo.getNumeroVolo().equals(numeroVolo) && volo instanceof VoloInPartenza) {
                Short gate = ((VoloInPartenza) volo).getGateImbarco();
                return gate != null ? String.valueOf(gate) : null;
            }
        }
        return null;
    }

}