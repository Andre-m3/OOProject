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
            System.out.print("Inserisci il gate d'imbarco: ");
            short gateImbarco = scanner.nextShort();
            scanner.nextLine();         // Consumo il newline

            System.out.print("Inserisci l'aeroporto di destinazione: ");
            String aeroportoDestinazione = scanner.nextLine();

            // Creiamo direttamente un oggetto VoloInPartenza (Volo)
            VoloInPartenza voloPartenza = new VoloInPartenza(
                    numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, statoVolo.toString(),
                    "Partenza", gateImbarco, aeroportoDestinazione
            );

            // Aggiungiamo il volo in partenza alla lista
            Volo.aggiungiVolo(voloPartenza);
            System.out.println("Volo in partenza inserito con successo!\n");

        } else {            // Se (tipoVolo.equals("A") == false) allora tipoVolo == 'A' !!
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
            try {
                voloDaAggiornare.setRitardo(Integer.parseInt(input));
            } catch (NumberFormatException e) {
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

        // Aggiorniamo i dati specifici in base al tipo di volo
        if (voloDaAggiornare instanceof VoloInPartenza voloPartenza) {

            System.out.print("Inserisci il nuovo gate d'imbarco (premi INVIO per mantenere " + voloPartenza.getGateImbarco() + "): ");
            input = scanner.nextLine();
            if (!input.isEmpty()) {
                try {
                    voloPartenza.setGateImbarco(Short.parseShort(input));
                } catch (NumberFormatException e) {
                    System.out.println("Formato non valido per il gate. Mantenuto il valore precedente.");
                }
            }

            System.out.print("Inserisci il nuovo aeroporto di destinazione (premi INVIO per mantenere " + voloPartenza.getAeroportoDestinazione() + "): ");
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
        System.out.println("Modifica gate imbarco: ");
    }                         // DA IMPLEMENTARE
}
