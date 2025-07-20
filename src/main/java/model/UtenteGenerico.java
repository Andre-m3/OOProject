package model;

import java.util.ArrayList;

/**
 * The type Utente generico.
 */
public class UtenteGenerico extends Utente {

    private ArrayList<Prenotazione> prenotazioni;

    /**
     * Instantiates a new Utente generico.
     *
     * @param email    the email
     * @param username the username
     * @param password the password
     */
    public UtenteGenerico(String email, String username, String password) {
        super(email, username, password);
        this.prenotazioni = new ArrayList<>();
    }

    /**
     * Sets prenotazione.
     *
     * @param prenotazione the prenotazione
     */
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
}