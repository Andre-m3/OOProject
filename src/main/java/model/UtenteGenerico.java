package model;

import java.util.ArrayList;
import java.util.Scanner;

public class UtenteGenerico extends Utente {

    private ArrayList<Prenotazione> prenotazioni;

    public UtenteGenerico(String email, String username, String password) {
        super(email, username, password);
        this.prenotazioni = new ArrayList<>();
    }

    public ArrayList<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazioni.add(prenotazione);
    }

    public void prenotaVolo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Prenotazione Volo ===");

        // Visualizziamo i voli disponibili
        ArrayList<Volo> listaVoli = Volo.getListaVoli();

        // Verifico che ci siano voli disponibili
        if (listaVoli.isEmpty()) {
            System.out.println("Non ci sono voli disponibili per la prenotazione.");
            return;
        }

        // Mostro solo i voli in partenza (non ha senso prenotare voli in arrivo!)
        boolean voliInPartenzaPresenti = false;
        System.out.println("\nVoli disponibili per la prenotazione:");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-12s %-15s %-15s %-20s %-10s%n", "Numero Volo", "Compagnia", "Data", "Destinazione", "Orario");
        System.out.println("-----------------------------------------------------------------------");

        for (Volo volo : listaVoli) {
            if (volo instanceof VoloInPartenza voloPartenza) { // Parliamo di pattern quando andiamo ad aggiungere voloPartenza cosi!
                voliInPartenzaPresenti = true;
                System.out.printf("%-12s %-15s %-15s %-20s %-10s%n",
                        volo.getNumeroVolo(),
                        volo.getCompagniaAerea(),
                        volo.getData(),
                        voloPartenza.getAeroportoDestinazione(),
                        volo.getOrarioPrevisto());
            }
        }
        System.out.println("-----------------------------------------------------------------------");

        if (!voliInPartenzaPresenti) {
            System.out.println("Non ci sono voli in partenza disponibili per essere prenotatati.");
            return;     // Concludiamo il metodo
        }

        // Chiedo all'utente di inserire il numero del volo da prenotare
        System.out.print("\nInserisci il numero del volo che desideri prenotare: ");
        String codiceVoloDaPrenotare = scanner.nextLine();

        // Cerco il volo corrispondente
        VoloInPartenza voloDaPrenotare = null;
        for (Volo volo : listaVoli) {
            if (volo instanceof VoloInPartenza && volo.getNumeroVolo().equals(codiceVoloDaPrenotare)) {
                voloDaPrenotare = (VoloInPartenza) volo;
                break;      // Una volta trovato il volo da prenotare, non serve continuare a scorrere la lista dei voli!
            }
        }

        // Verifico se il volo è stato trovato
        if (voloDaPrenotare == null) {      // Se il volo cercato non è presente, allora mostriamo un messaggio e concludiamo il metodo
            System.out.println("Volo non trovato. Verifica il numero del volo e riprova.");
            return;
        }

        // Mostro i dettagli del volo selezionato
        System.out.println("\nHai selezionato il seguente volo:");
        System.out.println("- Volo: " + voloDaPrenotare.getCompagniaAerea() + " " + voloDaPrenotare.getNumeroVolo());
        System.out.println("- Data: " + voloDaPrenotare.getData());
        System.out.println("- Destinazione: " + voloDaPrenotare.getAeroportoDestinazione());
        System.out.println("- Orario partenza: " + voloDaPrenotare.getOrarioPrevisto());

        // Chiedo il numero di passeggeri. Ci servirà per capire quanti ticket creare!
        System.out.print("\nInserisci il numero di passeggeri: ");
        int numeroPasseggeri;
        try {
            numeroPasseggeri = Integer.parseInt(scanner.nextLine());
            if (numeroPasseggeri <= 0) {
                System.out.println("Il numero di passeggeri deve essere maggiore di zero.");
                return;
            }
            if (numeroPasseggeri > Prenotazione.MAX_PASSEGGERI) {
                System.out.println("Il numero di passeggeri non può essere maggiore di " + Prenotazione.MAX_PASSEGGERI + ".");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato non valido per il numero di passeggeri.");
            return;
        }

        // Genero un codice prenotazione univoco
        String codicePrenotazione = Prenotazione.generaNuovoCodice();


        // Creo l'oggetto prenotazione
        Prenotazione nuovaPrenotazione = new Prenotazione(
                codicePrenotazione,
                voloDaPrenotare.getNumeroVolo(),
                voloDaPrenotare.getData(),
                voloDaPrenotare.getAeroportoDestinazione(),
                "Confermata",
                numeroPasseggeri
        );

        // Ora raccolgo i dati per ogni passeggero
        System.out.println("\n=== Inserimento dati passeggeri ===");
        for (int i = 0; i < numeroPasseggeri; i++) {
            System.out.println("\nPasseggero " + (i + 1) + ":");

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Cognome: ");
            String cognome = scanner.nextLine();

            System.out.print("Numero documento (alfanumerico): ");
            String numeroDocumento = scanner.nextLine();

            System.out.print("Data di nascita (dd/mm/aaaa): ");
            String dataNascita = scanner.nextLine();

            // Per ora ipotizziamo che il posto venga scelto dall'utente che effettua la prenotazione
            // Si potrebbe aggiungere un metodo per assegnare un posto in automatico a ogni passeggero della prenotazione
            System.out.print("Posto assegnato (es. A1, B2): ");
            String postoAssegnato = scanner.nextLine();

            // Creo il ticket per questo passeggero
            Ticket ticket = new Ticket(
                    nome,                   // Nome completo. In presenza di due nomi, vanno inseriti entrambi
                    cognome,                // Cognome completo. Discorso analogo.
                    numeroDocumento,        // Si tratta di un codice (più che "numero") alfanumerico presente sul documento di ciascun passeggero
                    dataNascita,            // Formato "dd-mm-yyyy" (esempio: 03/09/2003)
                    postoAssegnato,         // Formato "X0" (esempio: B2)
                    voloDaPrenotare.getNumeroVolo()     // Si riferisce al codice del volo che si sta prenotando (già esistente!!)
            );

            // Aggiungo il ticket alla prenotazione
            nuovaPrenotazione.aggiungiTicket(ticket);
        }

        // Aggiungo la prenotazione alla lista delle prenotazioni dell'utente
        this.setPrenotazione(nuovaPrenotazione);

        // Confermo la prenotazione
        System.out.println("\n=== Prenotazione completata con successo! ===");
        System.out.println("Codice prenotazione: " + codicePrenotazione);
        System.out.println("Volo: " + voloDaPrenotare.getCompagniaAerea() + " " + voloDaPrenotare.getNumeroVolo());
        System.out.println("Data: " + voloDaPrenotare.getData());
        System.out.println("Destinazione: " + voloDaPrenotare.getAeroportoDestinazione());
        System.out.println("Passeggeri: " + numeroPasseggeri);
    }

    public void cercaPrenotazionePerUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cerca Prenotazioni per Username ===");

        System.out.print("Inserisci l'username dell'utente: ");
        String username = scanner.nextLine();

        // Verifico se l'username corrisponde all'utente corrente
        if (this.getUsername().equals(username)) {
            // Mostro tutte le prenotazioni dell'utente corrente
            mostraPrenotazioniUtente();
        } else {
            System.out.println("Non sei autorizzato a visualizzare le prenotazioni di altri utenti.");
        }
    }

    public void cercaPrenotazionePerVolo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cerca Prenotazioni per Numero Volo ===");

        System.out.print("Inserisci il numero del volo: ");
        String numeroVolo = scanner.nextLine();

        // Verifico se l'utente ha prenotazioni
        if (this.prenotazioni.isEmpty()) {
            System.out.println("Non hai alcuna prenotazione registrata.");
            return;
        }

        // Cerco prenotazioni per il volo specificato
        boolean trovato = false;
        System.out.println("\nPrenotazioni trovate per il volo " + numeroVolo + ":");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s %-10s%n", "Codice", "Data", "Destinazione", "Passeggeri");
        System.out.println("------------------------------------------------------------------");

        for (Prenotazione prenotazione : this.prenotazioni) {
            if (prenotazione.getCodiceVolo().equals(numeroVolo)) {
                System.out.printf("%-15s %-15s %-20s %-10d%n",
                        prenotazione.getCodicePrenotazione(),
                        prenotazione.getDataVolo(),
                        prenotazione.getPartenzaDestinazione(),
                        prenotazione.getNumeroPasseggeri());
                trovato = true;
            }
        }
        System.out.println("------------------------------------------------------------------");

        if (!trovato) {
            System.out.println("Non hai prenotazioni per il volo " + numeroVolo + ".");
        }
    }

    // Metodo principale che gestisce entrambe le opzioni
    public void cercaPrenotazione() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Ricerca Prenotazioni ===");
        System.out.println("1. Cerca per username");
        System.out.println("2. Cerca per numero volo");
        System.out.print("Seleziona un'opzione (1-2): ");

        int scelta;
        try {
            scelta = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opzione non valida. Inserisci un numero.");
            return;
        }

        switch (scelta) {
            case 1:
                cercaPrenotazionePerUsername();
                break;
            case 2:
                cercaPrenotazionePerVolo();
                break;
            default:
                System.out.println("Opzione non valida.");
                break;
        }
    }

    // Metodo helper per mostrare tutte le prenotazioni dell'utente corrente
    private void mostraPrenotazioniUtente() {
        if (this.prenotazioni.isEmpty()) {
            System.out.println("Non hai alcuna prenotazione registrata.");
            return;
        }

        System.out.println("\nLe tue prenotazioni:");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-15s %-12s %-15s %-20s %-10s%n", "Codice", "Volo", "Data", "Destinazione", "Stato");
        System.out.println("------------------------------------------------------------------");

        for (Prenotazione prenotazione : this.prenotazioni) {
            System.out.printf("%-15s %-12s %-15s %-20s %-10s%n",
                    prenotazione.getCodicePrenotazione(),
                    prenotazione.getCodiceVolo(),
                    prenotazione.getDataVolo(),
                    prenotazione.getPartenzaDestinazione(),
                    prenotazione.getStato());
        }
        System.out.println("------------------------------------------------------------------");

        // Offro la possibilità di visualizzare i dettagli di una prenotazione specifica
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nVuoi visualizzare i dettagli di una prenotazione specifica? (s/n): ");
        String risposta = scanner.nextLine();

        if (risposta.equalsIgnoreCase("s")) {
            System.out.print("Inserisci il codice della prenotazione: ");
            String codicePrenotazione = scanner.nextLine();

            // Cerco la prenotazione
            Prenotazione prenotazioneTrovata = null;
            for (Prenotazione prenotazione : this.prenotazioni) {
                if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                    prenotazioneTrovata = prenotazione;
                    break;
                }
            }

            if (prenotazioneTrovata == null) {
                System.out.println("Prenotazione non trovata.");
                return;
            }

            // Mostro i dettagli completi della prenotazione
            System.out.println("\n=== Dettagli Prenotazione ===");
            System.out.println(prenotazioneTrovata);

            // Mostro i dettagli dei ticket
            ArrayList<Ticket> tickets = prenotazioneTrovata.getTickets();
            if (!tickets.isEmpty()) {
                System.out.println("\n=== Passeggeri ===");
                for (int i = 0; i < tickets.size(); i++) {
                    Ticket ticket = tickets.get(i);
                    System.out.println("Passeggero " + (i + 1) + ": " + ticket.getNome() + " " + ticket.getCognome() +
                            " (Posto: " + ticket.getPostoAssegnato() + ")");
                }
            }
        }
    }

    public void modificaPrenotazione() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Modifica Prenotazione ===");

        // Verifico se l'utente ha prenotazioni
        if (this.prenotazioni.isEmpty()) {
            System.out.println("Non hai alcuna prenotazione registrata.");
            return;
        }

        // Chiedo all'utente di inserire il codice della prenotazione
        System.out.print("Inserisci il codice della prenotazione da modificare: ");
        String codicePrenotazione = scanner.nextLine();

        // Cerco la prenotazione corrispondente
        Prenotazione prenotazioneDaModificare = null;
        int indicePrenDaModificare = -1;

        for (int i = 0; i < this.prenotazioni.size(); i++) {
            Prenotazione prenotazione = this.prenotazioni.get(i);
            if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                prenotazioneDaModificare = prenotazione;
                indicePrenDaModificare = i;
                break;
            }
        }

        // Verifico se la prenotazione è stata trovata
        if (prenotazioneDaModificare == null) {
            System.out.println("Prenotazione non trovata. Verifica il codice e riprova.");
            return;
        }

        // Visualizzo i dettagli della prenotazione
        System.out.println("\n=== Dettagli Prenotazione ===");
        System.out.println("Codice: " + prenotazioneDaModificare.getCodicePrenotazione());
        System.out.println("Volo: " + prenotazioneDaModificare.getCodiceVolo());
        System.out.println("Data: " + prenotazioneDaModificare.getDataVolo());
        System.out.println("Tratta: " + prenotazioneDaModificare.getPartenzaDestinazione());
        System.out.println("Stato: " + prenotazioneDaModificare.getStato());
        System.out.println("Numero passeggeri: " + prenotazioneDaModificare.getNumeroPasseggeri());

        // Chiedo se l'utente vuole cancellare l'intera prenotazione
        System.out.print("\nVuoi cancellare l'intera prenotazione? (s/n): ");
        String risposta = scanner.nextLine();

        if (risposta.equalsIgnoreCase("s")) {
            // Cancello la prenotazione
            this.prenotazioni.remove(indicePrenDaModificare);
            System.out.println("Prenotazione cancellata con successo. Tutti i ticket associati sono stati eliminati.");
            return;
        }

        // Mostro i passeggeri della prenotazione
        ArrayList<Ticket> tickets = prenotazioneDaModificare.getTickets();
        if (tickets.isEmpty()) {
            System.out.println("Non ci sono passeggeri associati a questa prenotazione.");
            return;
        }

        System.out.println("\n=== Passeggeri della prenotazione ===");
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            System.out.println((i + 1) + ". " + ticket.getNome() + " " + ticket.getCognome());
        }

        // Chiedo il nome e cognome del passeggero da modificare
        System.out.print("\nInserisci nome e cognome completo del passeggero da modificare: ");
        String nomeCompletoDaCercare = scanner.nextLine();

        // Cerco il ticket del passeggero
        Ticket ticketDaModificare = null;
        int indiceTicket = -1;

        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            String nomeCompleto = ticket.getNome() + " " + ticket.getCognome();
            if (nomeCompleto.equalsIgnoreCase(nomeCompletoDaCercare)) {
                ticketDaModificare = ticket;
                indiceTicket = i;
                break;
            }
        }

        // Verifico se il passeggero è stato trovato
        if (ticketDaModificare == null) {
            System.out.println("Passeggero non trovato. Verifica il nome e cognome e riprova.");
            return;
        }

        // Visualizzo i dettagli del ticket
        System.out.println("\n=== Dettagli Ticket ===");
        System.out.println("Nome: " + ticketDaModificare.getNome());
        System.out.println("Cognome: " + ticketDaModificare.getCognome());
        System.out.println("Documento: " + ticketDaModificare.getNumeroDocumento());
        System.out.println("Data Nascita: " + ticketDaModificare.getDataNascita());
        System.out.println("Posto: " + ticketDaModificare.getPostoAssegnato());
        System.out.println("Codice Volo: " + ticketDaModificare.getCodiceVolo());

        // Chiedo se l'utente vuole cancellare il ticket
        System.out.print("\nVuoi cancellare il ticket di questo passeggero? (s/n): ");
        risposta = scanner.nextLine();

        if (risposta.equalsIgnoreCase("s")) {
            // Rimuovo il ticket
            tickets.remove(indiceTicket);

            // Aggiorno il numero di passeggeri
            prenotazioneDaModificare.setNumeroPasseggeri(tickets.size());

            System.out.println("Ticket cancellato con successo.");

            // Se non ci sono più ticket, cancello la prenotazione
            if (tickets.isEmpty()) {
                this.prenotazioni.remove(indicePrenDaModificare);
                System.out.println("La prenotazione è stata cancellata poiché non ci sono più biglietti associati.");
            }
            return;
        }

        // Modifico i dati del ticket
        System.out.println("\n=== Modifica dati del passeggero ===");
        System.out.println("Lascia vuoto il campo e premi INVIO per mantenere il valore attuale.");

        // Modifico il nome
        System.out.print("Nome [" + ticketDaModificare.getNome() + "]: ");
        String nuovoNome = scanner.nextLine();
        if (!nuovoNome.isEmpty()) {
            ticketDaModificare.setNome(nuovoNome);
        }

        // Modifico il cognome
        System.out.print("Cognome [" + ticketDaModificare.getCognome() + "]: ");
        String nuovoCognome = scanner.nextLine();
        if (!nuovoCognome.isEmpty()) {
            ticketDaModificare.setCognome(nuovoCognome);
        }

        // Modifico il numero documento
        System.out.print("Numero documento [" + ticketDaModificare.getNumeroDocumento() + "]: ");
        String nuovoNumDoc = scanner.nextLine();
        if (!nuovoNumDoc.isEmpty()) {
            ticketDaModificare.setNumeroDocumento(nuovoNumDoc);
        }

        // Modifico la data di nascita
        System.out.print("Data di nascita [" + ticketDaModificare.getDataNascita() + "]: ");
        String nuovaDataNascita = scanner.nextLine();
        if (!nuovaDataNascita.isEmpty()) {
            ticketDaModificare.setDataNascita(nuovaDataNascita);
        }

        System.out.println("\nModifiche salvate con successo.");

        // Visualizzo lo stato aggiornato del ticket
        System.out.println("\n=== Ticket aggiornato ===");
        System.out.println("Nome: " + ticketDaModificare.getNome());
        System.out.println("Cognome: " + ticketDaModificare.getCognome());
        System.out.println("Documento: " + ticketDaModificare.getNumeroDocumento());
        System.out.println("Data Nascita: " + ticketDaModificare.getDataNascita());
        System.out.println("Posto: " + ticketDaModificare.getPostoAssegnato());
        System.out.println("Codice Volo: " + ticketDaModificare.getCodiceVolo());
    }               // DA CANCELLARE!

    public void visualizzaTicket() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Visualizzazione Ticket ===");

        // Verifico se l'utente ha prenotazioni
        if (this.prenotazioni.isEmpty()) {
            System.out.println("Non hai alcuna prenotazione registrata.");
            return;
        }

        // Mostro le prenotazioni disponibili
        System.out.println("\nLe tue prenotazioni:");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-15s %-12s %-15s %-20s%n", "Codice", "Volo", "Data", "Destinazione");
        System.out.println("------------------------------------------------------------------");

        for (Prenotazione prenotazione : this.prenotazioni) {
            System.out.printf("%-15s %-12s %-15s %-20s%n",
                    prenotazione.getCodicePrenotazione(),
                    prenotazione.getCodiceVolo(),
                    prenotazione.getDataVolo(),
                    prenotazione.getPartenzaDestinazione());
        }
        System.out.println("------------------------------------------------------------------");

        // Chiedo all'utente di inserire il codice della prenotazione
        System.out.print("\nInserisci il codice della prenotazione per visualizzare i ticket: ");
        String codicePrenotazione = scanner.nextLine();

        // Cerco la prenotazione corrispondente
        Prenotazione prenotazioneTrovata = null;
        for (Prenotazione prenotazione : this.prenotazioni) {
            if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                prenotazioneTrovata = prenotazione;
                break;
            }
        }

        // Verifico se la prenotazione è stata trovata
        if (prenotazioneTrovata == null) {
            System.out.println("Prenotazione non trovata. Verifica il codice e riprova.");
            return;
        }

        // Recupero i ticket associati alla prenotazione
        ArrayList<Ticket> tickets = prenotazioneTrovata.getTickets();

        // Verifico se ci sono ticket
        if (tickets.isEmpty()) {
            System.out.println("Non ci sono ticket associati a questa prenotazione.");
            return;
        }

        // Visualizzo i dettagli della prenotazione
        System.out.println("\n=== Dettagli Prenotazione ===");
        System.out.println("Codice: " + prenotazioneTrovata.getCodicePrenotazione());
        System.out.println("Volo: " + prenotazioneTrovata.getCodiceVolo());
        System.out.println("Data: " + prenotazioneTrovata.getDataVolo());
        System.out.println("Tratta: " + prenotazioneTrovata.getPartenzaDestinazione());
        System.out.println("Stato: " + prenotazioneTrovata.getStato());
        System.out.println("Numero passeggeri: " + prenotazioneTrovata.getNumeroPasseggeri());

        // Visualizzo i dettagli di ogni ticket
        System.out.println("\n=== Ticket associati ===");
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            System.out.println("\nTicket " + (i + 1) + ":");
            System.out.println("- Nome: " + ticket.getNome());
            System.out.println("- Cognome: " + ticket.getCognome());
            System.out.println("- Documento: " + ticket.getNumeroDocumento());
            System.out.println("- Data Nascita: " + ticket.getDataNascita());
            System.out.println("- Posto: " + ticket.getPostoAssegnato());
            System.out.println("- Codice Volo: " + ticket.getCodiceVolo());
        }
    }
}
