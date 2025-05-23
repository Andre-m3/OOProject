package model;

public class UtenteGenerico extends Utente {

    public UtenteGenerico(String email, String username, String password) {
        super(email, username, password);
    }

    public void prenotaVolo() {
        System.out.println("Prenota: ");
    }                                       // DA IMPLEMENTARE

    public Volo cercaPrenotazione(String ricerca) {

        // in base alla stringa fornita, effettueremo un check sul parametro passato
        // la procedura devierà in base al check (se è numero volo, oppure se è username)

        /*  Potremmo utilizzare un "Overloaded Method" per gestire i due casi.
         *  Siccome il "numVolo" tratta una stringa esadecimale, quindi non un numero intero
         *  Avremmo avuto due metodi nominati uguale, con anche lo stesso tipo di parametro.
         *  Questo ci obbliga a non poter utilizzare un "Overloaded Method", ma a gestire il caso interamente
         */

        System.out.println("Cerca prenotazione username/numVolo: ");
        return null;    // nessun oggetto creato, struttura scheletrica
    }                   // DA IMPLEMENTARE

    public void modificaPrenotazione() {
        System.out.println("Modifica: ");
    }                              // DA IMPLEMENTARE
}
