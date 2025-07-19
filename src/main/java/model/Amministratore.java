package model;

public class Amministratore extends Utente {
    public Amministratore(String email, String username, String password) {
        super(email, username, password);
    }

    /* I METODI COMMENTATI DI SEGUITO SONO STATI IMPLEMENTATI NEL CONTROLLER
     * EFFETTUARE UNA VERIFICA GENERALE E RIPORTARLI QUI.
     * I METODI "LOGICI" ANDREBBERO IMPLEMENTATI NELLE CLASSI QUI DEL PACKAGE "model"
     * NELLA CREAZIONE DELLE VARIE INTERFACCE, ABBIAMO "erroneamente" (tra tante virgolette)
     * IMPLEMENTATO I METODI LI, SENZA TENERE CONTO DEI METODI DELLE CLASSI DEL PACKAGE "model"
     * DISCORSO VALIDO PER TUTTE LE CLASSI
     */
//    public void inserimentoVolo() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("=== Inserimento Nuovo Volo ===");
//
//        // Inserimento dati comuni
//        System.out.print("Inserisci il numero del volo: ");
//        String numeroVolo = scanner.nextLine();
//        System.out.print("Inserisci la compagnia aerea: ");
//        String compagniaAerea = scanner.nextLine();
//        System.out.print("Inserisci l'orario previsto (hh:mm): ");
//        String orarioPrevisto = scanner.nextLine();
//        System.out.print("Inserisci la data (dd/mm/yyyy): ");
//        String data = scanner.nextLine();
//
//        int ritardo = 0;
//
//        // Visualizza gli stati disponibili
//        System.out.print("Stati volo: [");
//        for (StatoVolo stato : StatoVolo.values()) {
//            System.out.print(" " + stato + " ");
//        }
//        System.out.println("]");
//
//        System.out.print("Inserisci lo stato del volo: ");
//        String statoInput = scanner.nextLine().toUpperCase();
//        StatoVolo statoVolo = StatoVolo.valueOf(statoInput);
//
//        // Chiedi il tipo di volo
//        String tipoVolo;
//        do {
//            System.out.print("Il volo è in partenza o in arrivo? (P/A): ");
//            tipoVolo = scanner.nextLine();
//        } while (!tipoVolo.equalsIgnoreCase("P") && !tipoVolo.equalsIgnoreCase("A"));
//
//        if (tipoVolo.equalsIgnoreCase("P")) {
//            // Volo in partenza (da Napoli)
//            System.out.print("Inserisci l'aeroporto di destinazione: ");
//            String destinazione = scanner.nextLine();
//
//            System.out.print("Inserisci il gate d'imbarco (premi INVIO per non assegnarlo): ");
//            String inputGate = scanner.nextLine();
//
//            VoloInPartenza voloPartenza;
//            if (inputGate.isEmpty()) {
//                voloPartenza = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
//                        data, ritardo, statoVolo.toString(), destinazione);
//            } else {
//                try {
//                    Short gateImbarco = Short.parseShort(inputGate);
//                    voloPartenza = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
//                            data, ritardo, statoVolo.toString(), destinazione, gateImbarco);
//                } catch (NumberFormatException e) {
//                    System.out.println("Formato non valido per il gate. Il gate verrà impostato come non assegnato.");
//                    voloPartenza = new VoloInPartenza(numeroVolo, compagniaAerea, orarioPrevisto,
//                            data, ritardo, statoVolo.toString(), destinazione);
//                }
//            }
//
//            Volo.aggiungiVolo(voloPartenza);
//            System.out.println("Volo in partenza inserito con successo!\n");
//
//        } else {
//            // Volo in arrivo (verso Napoli)
//            System.out.print("Inserisci l'aeroporto di origine: ");
//            String origine = scanner.nextLine();
//
//            VoloInArrivo voloArrivo = new VoloInArrivo(numeroVolo, compagniaAerea, orarioPrevisto,
//                    data, ritardo, statoVolo.toString(), origine);
//
//            Volo.aggiungiVolo(voloArrivo);
//            System.out.println("Volo in arrivo inserito con successo!\n");
//        }
//    }
//
//    public void aggiornaVolo() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("=== Aggiornamento Volo Esistente ===");
//
//        visualizzaVoli();
//
//        ArrayList<Volo> listaVoli = Volo.getListaVoli();
//        if (listaVoli.isEmpty()) {
//            return;
//        }
//
//        System.out.print("Inserisci il codice del volo da aggiornare: ");
//        String codiceDaCercare = scanner.nextLine();
//
//        Volo voloDaAggiornare = null;
//        for (Volo volo : listaVoli) {
//            if (volo.getNumeroVolo().equals(codiceDaCercare)) {
//                voloDaAggiornare = volo;
//                break;
//            }
//        }
//
//        if (voloDaAggiornare == null) {
//            System.out.println("Volo non trovato. Verifica il codice e riprova.");
//            return;
//        }
//
//        System.out.println("\nDati attuali del volo:");
//        System.out.println(voloDaAggiornare);
//
//        // Aggiornamento dati comuni
//        System.out.print("Nuova compagnia aerea (INVIO per mantenere " + voloDaAggiornare.getCompagniaAerea() + "): ");
//        String input = scanner.nextLine();
//        if (!input.isEmpty()) {
//            voloDaAggiornare.setCompagniaAerea(input);
//        }
//
//        System.out.print("Nuovo orario (INVIO per mantenere " + voloDaAggiornare.getOrarioPrevisto() + "): ");
//        input = scanner.nextLine();
//        if (!input.isEmpty()) {
//            voloDaAggiornare.setOrarioPrevisto(input);
//        }
//
//        System.out.print("Nuova data (INVIO per mantenere " + voloDaAggiornare.getData() + "): ");
//        input = scanner.nextLine();
//        if (!input.isEmpty()) {
//            voloDaAggiornare.setData(input);
//        }
//
//        System.out.print("Nuovo ritardo in minuti (INVIO per mantenere " + voloDaAggiornare.getRitardo() + "): ");
//        input = scanner.nextLine();
//        if (!input.isEmpty()) {
//            try {
//                voloDaAggiornare.setRitardo(Integer.parseInt(input));
//            } catch (NumberFormatException e) {
//                System.out.println("Formato non valido per il ritardo.");
//            }
//        }
//
//        System.out.print("Nuovo stato (INVIO per mantenere " + voloDaAggiornare.getStato() + "): ");
//        input = scanner.nextLine();
//        if (!input.isEmpty()) {
//            voloDaAggiornare.setStato(input);
//        }
//
//        // Aggiornamento specifico per voli in partenza
//        if (voloDaAggiornare instanceof VoloInPartenza) {
//            VoloInPartenza voloPartenza = (VoloInPartenza) voloDaAggiornare;
//            System.out.print("Nuovo gate (INVIO per mantenere " + voloPartenza.getGateImbarco() + "): ");
//            input = scanner.nextLine();
//            if (!input.isEmpty()) {
//                try {
//                    voloPartenza.setGateImbarco(Short.parseShort(input));
//                } catch (NumberFormatException e) {
//                    System.out.println("Formato non valido per il gate.");
//                }
//            }
//        }
//
//        System.out.println("Volo aggiornato con successo!");
//    }
//
//    public void visualizzaVoli() {
//        ArrayList<Volo> listaVoli = Volo.getListaVoli();
//        if (listaVoli.isEmpty()) {
//            System.out.println("Non ci sono voli disponibili.");
//            return;
//        }
//
//        System.out.println("=== Lista Voli ===");
//        for (Volo volo : listaVoli) {
//            System.out.println(volo);
//            System.out.println("---");
//        }
//    }
}