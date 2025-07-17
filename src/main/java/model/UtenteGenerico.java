
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

//    public void prenotaVolo() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("=== Prenotazione Volo ===");
//
//        // Mostra voli disponibili per la prenotazione
//        ArrayList<Volo> voliDisponibili = new ArrayList<>();
//        for (Volo volo : Volo.getListaVoli()) {
//            if (volo.getStato() == StatoVolo.PROGRAMMATO) {
//                voliDisponibili.add(volo);
//            }
//        }
//
//        if (voliDisponibili.isEmpty()) {
//            System.out.println("Non ci sono voli disponibili per la prenotazione.");
//            return;
//        }
//
//        System.out.println("Voli disponibili:");
//        for (int i = 0; i < voliDisponibili.size(); i++) {
//            Volo volo = voliDisponibili.get(i);
//            System.out.println((i + 1) + ". " + volo.getCompagniaAerea() + " " + volo.getNumeroVolo()
//                    + " - " + volo.getPartenza() + " → " + volo.getDestinazione()
//                    + " - " + volo.getData() + " " + volo.getOrarioPrevisto());
//        }
//
//        System.out.print("Inserisci il numero del volo da prenotare: ");
//        String numeroVolo = scanner.nextLine();
//
//        // Cerca il volo
//        Volo voloSelezionato = null;
//        for (Volo volo : voliDisponibili) {
//            if (volo.getNumeroVolo().equals(numeroVolo)) {
//                voloSelezionato = volo;
//                break;
//            }
//        }
//
//        if (voloSelezionato == null) {
//            System.out.println("Volo non trovato.");
//            return;
//        }
//
//        System.out.print("Inserisci il numero di passeggeri (1-" + Prenotazione.MAX_PASSEGGERI + "): ");
//        int numeroPasseggeri;
//        try {
//            numeroPasseggeri = Integer.parseInt(scanner.nextLine());
//            if (numeroPasseggeri < 1 || numeroPasseggeri > Prenotazione.MAX_PASSEGGERI) {
//                System.out.println("Numero di passeggeri non valido.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Formato numero non valido.");
//            return;
//        }
//
//        // Crea la prenotazione
//        String tratta = voloSelezionato.getPartenza() + " → " + voloSelezionato.getDestinazione();
//        String codicePrenotazione = Prenotazione.generaNuovoCodice();
//
//        Prenotazione nuovaPrenotazione = new Prenotazione(
//                codicePrenotazione,
//                voloSelezionato.getNumeroVolo(),
//                voloSelezionato.getData(),
//                tratta,
//                StatoPrenotazione.CONFERMATA,
//                numeroPasseggeri
//        );
//
//        this.setPrenotazione(nuovaPrenotazione);
//        System.out.println("Prenotazione creata con successo! Codice: " + codicePrenotazione);
//    }

    public void cercaPrenotazionePerUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cerca Prenotazione per Username ===");

        System.out.print("Inserisci lo username: ");
        String username = scanner.nextLine();

        if (this.getUsername().equals(username)) {
            mostraPrenotazioniUtente();
        } else {
            System.out.println("Username non corrispondente all'utente loggato.");
        }
    }

    public void cercaPrenotazionePerVolo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cerca Prenotazione per Volo ===");

        System.out.print("Inserisci il codice del volo: ");
        String codiceVolo = scanner.nextLine();

        boolean trovata = false;
        for (Prenotazione prenotazione : this.prenotazioni) {
            if (prenotazione.getCodiceVolo().equals(codiceVolo)) {
                System.out.println(prenotazione);
                trovata = true;
            }
        }

        if (!trovata) {
            System.out.println("Nessuna prenotazione trovata per il volo " + codiceVolo);
        }
    }

    public void cercaPrenotazione() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cerca Prenotazione ===");
        System.out.println("1. Cerca per username");
        System.out.println("2. Cerca per codice volo");
        System.out.print("Scegli un'opzione: ");

        String scelta = scanner.nextLine();

        switch (scelta) {
            case "1":
                cercaPrenotazionePerUsername();
                break;
            case "2":
                cercaPrenotazionePerVolo();
                break;
            default:
                System.out.println("Opzione non valida.");
        }
    }

    private void mostraPrenotazioniUtente() {
        if (this.prenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni.");
            return;
        }

        System.out.println("=== Le tue prenotazioni ===");
        for (Prenotazione prenotazione : this.prenotazioni) {
            System.out.println(prenotazione);
            System.out.println("---");
        }
    }

    public void modificaPrenotazione() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Modifica Prenotazione ===");

        if (this.prenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni da modificare.");
            return;
        }

        mostraPrenotazioniUtente();

        System.out.print("Inserisci il codice della prenotazione da modificare: ");
        String codicePrenotazione = scanner.nextLine();

        Prenotazione prenotazioneDaModificare = null;
        for (Prenotazione prenotazione : this.prenotazioni) {
            if (prenotazione.getCodicePrenotazione().equals(codicePrenotazione)) {
                prenotazioneDaModificare = prenotazione;
                break;
            }
        }

        if (prenotazioneDaModificare == null) {
            System.out.println("Prenotazione non trovata.");
            return;
        }

        System.out.println("Prenotazione selezionata:");
        System.out.println(prenotazioneDaModificare);

        System.out.println("1. Modifica numero passeggeri");
        System.out.println("2. Cancella prenotazione");
        System.out.print("Scegli un'opzione: ");

        String scelta = scanner.nextLine();

        switch (scelta) {
            case "1":
                System.out.print("Nuovo numero di passeggeri (1-" + Prenotazione.MAX_PASSEGGERI + "): ");
                try {
                    int nuovoNumero = Integer.parseInt(scanner.nextLine());
                    if (nuovoNumero >= 1 && nuovoNumero <= Prenotazione.MAX_PASSEGGERI) {
                        prenotazioneDaModificare.setNumeroPasseggeri(nuovoNumero);
                        System.out.println("Numero passeggeri aggiornato con successo!");
                    } else {
                        System.out.println("Numero non valido.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Formato non valido.");
                }
                break;
            case "2":
                this.prenotazioni.remove(prenotazioneDaModificare);
                System.out.println("Prenotazione cancellata con successo!");
                break;
            default:
                System.out.println("Opzione non valida.");
        }
    }

//    public void visualizzaTicket() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("=== Visualizza Ticket ===");
//
//        if (this.prenotazioni.isEmpty()) {
//            System.out.println("Non hai prenotazioni.");
//            return;
//        }
//
//        mostraPrenotazioniUtente();
//
//        System.out.print("Inserisci il codice della prenotazione: ");
//        String codicePrenotazione = scanner.nextLine();
//
//        Prenotazione prenotazione = null;
//        for (Prenotazione p : this.prenotazioni) {
//            if (p.getCodicePrenotazione().equals(codicePrenotazione)) {
//                prenotazione = p;
//                break;
//            }
//        }
//
//        if (prenotazione == null) {
//            System.out.println("Prenotazione non trovata.");
//            return;
//        }
//
//        if (prenotazione.getTickets().isEmpty()) {
//            System.out.println("Nessun ticket associato a questa prenotazione.");
//            return;
//        }
//
//        System.out.println("=== Ticket della prenotazione " + codicePrenotazione + " ===");
//        for (Ticket ticket : prenotazione.getTickets()) {
//            System.out.println(ticket);
//            System.out.println("---");
//        }
//    }
}