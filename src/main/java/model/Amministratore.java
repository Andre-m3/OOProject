package model;

import java.util.ArrayList;
import java.util.Scanner;

public class Amministratore extends Utente {
    public Amministratore(String email, String username, String password) {
        super(email, username, password);
    }

    public void inserimentoVolo() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Inserimento Nuovo Volo ===");

        // Inserimento di tutti i dati del volo (numero, compagnia, orario, data, ritardo (default 0), stato)
        System.out.print("Inserisci il numero del volo: ");
        String numeroVolo = scanner.nextLine();
        System.out.print("Inserisci la compagnia aerea: ");
        String compagniaAerea = scanner.nextLine();
        System.out.print("Inserisci l'orario previsto (hh:mm): ");
        String orarioPrevisto = scanner.nextLine();
        System.out.print("Inserisci la data (dd/mm/yyyy): ");
        String data = scanner.nextLine();

        // Quando il volo viene inserito per la prima volta, il ritardo è di DEFAULT '0', sempre!
        int ritardo = 0;        // L'amministratore potrà modificarne l'eventuale valore in seguito tramite il metodo "aggiornaVolo()"

        // Visualizziamo gli 'stati di volo' disponibili
        System.out.print("Stati volo: [");
        for (StatoVolo stato : StatoVolo.values()) {
            System.out.print(" " + stato + " ");
        } System.out.println("]");

        System.out.print("Inserisci lo stato del volo tra quelli elencati: ");
        String statoInput = scanner.nextLine().toUpperCase();
        StatoVolo statoVolo = StatoVolo.valueOf(statoInput);

        // Chiediamo se è un volo in partenza o in arrivo
        String tipoVolo;
        do {
            System.out.print("Il volo è in partenza o in arrivo? (P/A): ");
            tipoVolo = scanner.nextLine();
        } while (!(tipoVolo.equalsIgnoreCase("P")) && !(tipoVolo.equalsIgnoreCase("A")));


        if (tipoVolo.equalsIgnoreCase("P")) {     // Per confrontare il contenuto è sempre meglio utilizzare equals(). Potrebbero sempre esserci riferimenti diversi!
            System.out.print("Inserisci l'aeroporto di destinazione: ");
            String aeroportoDestinazione = scanner.nextLine();

            System.out.print("Inserisci il gate d'imbarco (premi INVIO per non assegnarlo): ");
            String inputGate = scanner.nextLine();

            VoloInPartenza voloPartenza;

            // Verifichiamo se l'utente ha inserito un valore per il gate d'imbarco
            if (inputGate.isEmpty()) {      // inputGate è Short (Wrapper), quindi abbiamo una serie di metodi utilizzabili! A differenza di short primitivo...
                // Se l'utente ha premuto INVIO senza inserire un valore, creiamo il volo senza gate. Ricordiamo che sono presenti due costruttori!
                voloPartenza = new VoloInPartenza(
                        numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, statoVolo.toString(),
                        "Partenza", aeroportoDestinazione
                );
            }
            else {
                try {
                    // Altrimenti, tentiamo di convertire l'input da tastiera in Short. Ricordiamo che le Wrapper Classes vengono trattate diversamente da primitive!
                    Short gateImbarco = Short.parseShort(inputGate);
                    voloPartenza = new VoloInPartenza(
                            numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, statoVolo.toString(),
                            "Partenza", gateImbarco, aeroportoDestinazione
                    );
                } catch (NumberFormatException e) {     // Se la conversione non va a buon fine, allora creiamo un oggetto VoloInPartenza (Volo) senza gate!
                    System.out.println("Formato non valido per il gate. Il gate verrà impostato come non assegnato.");
                    voloPartenza = new VoloInPartenza(
                            numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, statoVolo.toString(),
                            "Partenza", aeroportoDestinazione
                    );
                }
            }

            // Aggiungiamo il volo in partenza alla lista
            Volo.aggiungiVolo(voloPartenza);
            System.out.println("Volo in partenza inserito con successo!\n");

        } else {            // Se il volo è in Arrivo, non dobbiamo preoccuparci di gestire il gate di imbarco!
            System.out.print("Inserisci l'aeroporto di origine: ");
            String aeroportoOrigine = scanner.nextLine();

            // Creiamo direttamente un oggetto VoloInArrivo (Volo)
            VoloInArrivo voloArrivo = new VoloInArrivo(
                    numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, statoVolo.toString(),
                    "Arrivo", aeroportoOrigine
            );

            // Aggiungiamo il volo in arrivo alla lista
            Volo.aggiungiVolo(voloArrivo);
            System.out.println("Volo in arrivo inserito con successo!\n");
        }

    }

    public void aggiornaVolo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Aggiornamento Volo Esistente ===");

        // Mostro i voli esistenti
        visualizzaVoli();

        ArrayList<Volo> listaVoli = Volo.getListaVoli();
        if (listaVoli.isEmpty()) {
            return; // Usciamo dal metodo se non ci sono voli
        }
        System.out.print("Inserisci il codice del volo da aggiornare: ");
        String codiceDaCercare = scanner.nextLine();

        // Cerchiamo il volo nella lista
        Volo voloDaAggiornare = null;
        for (Volo volo : listaVoli) {
            if (volo.getNumeroVolo().equals(codiceDaCercare)) {
                voloDaAggiornare = volo;
                break;
            }
        }

        if (voloDaAggiornare == null) {
            System.out.println("Volo non trovato. Verifica il codice e riprova.");
            return;
        }

        // Mostriamo i dati attuali del volo
        System.out.println("\nDati attuali del volo:");
        System.out.println(voloDaAggiornare);

        // Aggiorniamo i dati comuni a tutti i tipi di volo
        System.out.print("Inserisci la nuova compagnia aerea (premi INVIO per mantenere " + voloDaAggiornare.getCompagniaAerea() + "): ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            voloDaAggiornare.setCompagniaAerea(input);
        }

        System.out.print("Inserisci il nuovo orario previsto (hh:mm) (premi INVIO per mantenere " + voloDaAggiornare.getOrarioPrevisto() + "): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            voloDaAggiornare.setOrarioPrevisto(input);
        }

        System.out.print("Inserisci la nuova data (dd/mm/yyyy) (premi INVIO per mantenere " + voloDaAggiornare.getData() + "): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            voloDaAggiornare.setData(input);
        }

        System.out.print("Inserisci il nuovo ritardo in minuti (premi INVIO per mantenere " + voloDaAggiornare.getRitardo() + "): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            try {       // Proviamo a convertire l'input da tastiera in Integer. Ricordiamo che le Wrapper Classes vengono trattate diversamente da primitive!
                voloDaAggiornare.setRitardo(Integer.parseInt(input));
            } catch (NumberFormatException e) {     // Se falliamo, allora manteniamo il valore precedente!
                System.out.println("Formato non valido per il ritardo. Mantenuto il valore precedente.");
            }
        }

        // Visualizziamo gli 'stati di volo' disponibili
        System.out.print("Stati volo: [");
        for (StatoVolo stato : StatoVolo.values()) {
            System.out.print(" " + stato + " ");
        } System.out.println("]");

        System.out.print("Inserisci il nuovo stato del volo (premi INVIO per mantenere " + voloDaAggiornare.getStato() + "): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            try {
                StatoVolo statoVolo = StatoVolo.valueOf(input.toUpperCase());
                voloDaAggiornare.setStato(statoVolo.toString());
            } catch (IllegalArgumentException e) {
                System.out.println("Stato volo non valido. Mantenuto lo stato precedente.");
            }
        }

        // Modifichiamo (eventualmente) i dati specifici in base al tipo di volo
        if (voloDaAggiornare instanceof VoloInPartenza voloPartenza) {
            // Mostra il valore attuale del gate (o "non assegnato" se è null)
            String gateDisplay = voloPartenza.getGateImbarco() != null ?
                    String.valueOf(voloPartenza.getGateImbarco()) : "non assegnato";

            System.out.print("Inserisci il nuovo gate d'imbarco (premi INVIO per mantenere " + gateDisplay +
                    ", inserisci 0 per rimuovere l'assegnazione): ");
            input = scanner.nextLine();

            if (!input.isEmpty()) {
                try {
                    short gateValue = Short.parseShort(input);

                    if (gateValue == 0) {
                        // Se l'utente ha inserito 0, rimuoviamo l'assegnazione del gate
                        voloPartenza.setGateImbarco(null);
                        System.out.println("Gate rimosso con successo.");
                    } else {
                        // Altrimenti, impostiamo il nuovo valore del gate
                        voloPartenza.setGateImbarco(gateValue);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Formato non valido per il gate. Mantenuto il valore precedente.");
                }
            }

            System.out.print("Inserisci il nuovo aeroporto di destinazione (premi INVIO per mantenere " +
                    voloPartenza.getAeroportoDestinazione() + "): ");
            input = scanner.nextLine();
            if (!input.isEmpty()) {
                voloPartenza.setAeroportoDestinazione(input);
            }

        } else if (voloDaAggiornare instanceof VoloInArrivo voloArrivo) {

            System.out.print("Inserisci il nuovo aeroporto di origine (premi INVIO per mantenere " + voloArrivo.getAeroportoOrigine() + "): ");
            input = scanner.nextLine();
            if (!input.isEmpty()) {
                voloArrivo.setAeroportoOrigine(input);
            }
        }

        System.out.println("\nVolo aggiornato con successo!");
        System.out.println("Nuovi dati del volo:");
        System.out.println(voloDaAggiornare);
    }

    public void modificaGateImbarco() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Modifica Gate di Imbarco ===");

        // Ottengo la lista di tutti i voli
        ArrayList<Volo> listaVoli = Volo.getListaVoli();

        // Verifico se ci sono voli in partenza
        boolean voliInPartenzaPresenti = false;
        System.out.println("\nElenco voli in partenza:");       // Visualizzazione stile tabella. Se i voli sono molti, sarebbe molto meno intuitivo!
        System.out.println("---------------------------------------------------");
        System.out.printf("%-12s %-20s %-15s%n", "Numero Volo", "Destinazione", "Orario Partenza");
        System.out.println("---------------------------------------------------");

        // Scorro la lista dei voli e visualizzo solo i voli in partenza
        for (Volo volo : listaVoli) {
            // Con instanceof verifico se il volo è un'istanza della classe VoloInPartenza, altrimenti non lo visualizzo!
            if (volo instanceof VoloInPartenza voloPartenza) {
                voliInPartenzaPresenti = true;
                System.out.printf("%-12s %-20s %-15s%n",
                        volo.getNumeroVolo(),
                        voloPartenza.getAeroportoDestinazione(),
                        volo.getOrarioPrevisto());
            }
        }
        System.out.println("---------------------------------------------------");

        // Se non ci sono "voli in partenza" significa che la variabile booleana creata sarà ancora "false"!
        if (!voliInPartenzaPresenti) {      // Quindi visualizzo un messaggio all'utente e termino il metodo con return
            System.out.println("Non ci sono voli in partenza registrati nel sistema.");
            return;
        }

        // Chiedo all'amministratore di inserire il numero del volo da modificare
        System.out.print("\nInserisci il numero del volo per cui vuoi modificare il gate di imbarco: ");
        String numeroVoloDaModificare = scanner.nextLine();

        // Cerco il volo in partenza corrispondente, così da poter modificare il gate di imbarco
        VoloInPartenza voloDaModificare = null;
        for (Volo volo : listaVoli) {
            if (volo instanceof VoloInPartenza && volo.getNumeroVolo().equals(numeroVoloDaModificare)) {
                voloDaModificare = (VoloInPartenza) volo;
                break;
            }
        }

        // Verifico se il volo è stato trovato
        if (voloDaModificare == null) {     // Se il numero inserito non è associato ad alcun volo in partenza, allora termino il metodo con return
            System.out.println("Volo in partenza non trovato. Verifica il numero del volo e riprova.");
            return;
        }

        // Mostro il gate attuale
        System.out.println("\nDati del volo selezionato:");
        System.out.println("- Volo: " + voloDaModificare.getCompagniaAerea() + " " + voloDaModificare.getNumeroVolo());
        System.out.println("- Destinazione: " + voloDaModificare.getAeroportoDestinazione());
        System.out.println("- Orario partenza: " + voloDaModificare.getOrarioPrevisto());
        System.out.println("- Gate attuale: " + (voloDaModificare.getGateImbarco() != null ?
                voloDaModificare.getGateImbarco() : "non assegnato"));

        // Chiedo di inserire il nuovo gate, 0 per rimuovere l'assegnazione, o INVIO per mantenere il valore attuale
        String gateAttuale = voloDaModificare.getGateImbarco() != null ?
                voloDaModificare.getGateImbarco().toString() : "non assegnato";
        System.out.print("\nInserisci il nuovo gate di imbarco (Premi INVIO per mantenere gate \" + gateAttuale + \", inserisci 0 per rimuovere l'assegnazione): ");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            // Se l'utente ha premuto INVIO senza inserire un valore, manteniamo il valore attuale
            System.out.println("\nOperazione annullata. Il gate rimane " + gateAttuale + ".");
        } else {
            try {
                short inputGate = Short.parseShort(input);

                if (inputGate == 0) {
                    /* Se l'utente ha inserito 0, rimuoviamo l'assegnazione del gate (gateImbarco=null)
                     *  Potrebbero verificarsi situazioni per le quali c'è bisogno di rimuovere il gate in via precauzionale
                     *  Prima di decidere il nuovo gate da assegnare all'imbarco...
                     */
                    voloDaModificare.setGateImbarco(null);
                    System.out.println("\nAssegnazione del gate rimossa con successo!");
                    System.out.println("Stato gate: Non assegnato");
                } else {
                    // Altrimenti, assegniamo il nuovo gate con il valore inserito dall'amministratore
                    voloDaModificare.setGateImbarco(inputGate);
                    System.out.println("\nGate di imbarco modificato con successo!");
                    System.out.println("Nuovo gate di imbarco: " + inputGate);
                }
            } catch (
                    NumberFormatException e) {     // Se viene inserito un valore non valido, lo facciamo notare con un messaggio
                System.out.println("Formato non valido per il gate. La modifica non è stata effettuata.");
            }
        }
    }           // RIDONDANZA CON "aggiornaVolo()"? VALUTARE SE EFFETTIVAMENTE NECESSARIO!

}