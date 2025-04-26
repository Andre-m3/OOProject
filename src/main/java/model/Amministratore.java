package model;

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
        System.out.println("\nStati volo:[");
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
            tipoVolo = scanner.nextLine().toUpperCase();
        } while (!(tipoVolo.equals("P")) && !(tipoVolo.equals("A")));

        if (tipoVolo.equals("P")) {     // Per confrontare il contenuto è sempre meglio utilizzare equals(). Potrebbero sempre esserci riferimenti diversi!
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
        System.out.println("Aggiorna volo: ");
    }

    public void modificaGateImbarco() {
        System.out.println("Modifica gate imbarco: ");
    }
}
