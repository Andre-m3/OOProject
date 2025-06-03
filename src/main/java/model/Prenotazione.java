package model;

import java.util.ArrayList;

public class Prenotazione {

    // Costante che definisce il numero massimo di passeggeri per prenotazione
    public static final int MAX_PASSEGGERI = 10;
    // Contatore statico per generare codici prenotazione progressivi
    private static int contatoreCodici = 20000;

    private String codicePrenotazione;
    private String codiceVolo;
    private String dataVolo;
    private String partenzaDestinazione;
    private String stato;
    private int numeroPasseggeri;
    private ArrayList<Ticket> tickets;
    
    public Prenotazione(String codicePrenotazione, String codiceVolo, String dataVolo,
                        String partenzaDestinazione, String stato, int numeroPasseggeri) {
        this.codicePrenotazione = codicePrenotazione;
        this.codiceVolo = codiceVolo;
        this.dataVolo = dataVolo;
        this.partenzaDestinazione = partenzaDestinazione;
        this.stato = stato;
        this.numeroPasseggeri = numeroPasseggeri;
        this.tickets = new ArrayList<>();
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }
    public void setCodicePrenotazione(String codicePrenotazione) {
        this.codicePrenotazione = codicePrenotazione;
    }

    public String getCodiceVolo() {
        return codiceVolo;
    }
    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    public String getDataVolo() {
        return dataVolo;
    }
    public void setDataVolo(String dataVolo) {
        this.dataVolo = dataVolo;
    }

    public String getPartenzaDestinazione() {
        return partenzaDestinazione;
    }
    public void setPartenzaDestinazione(String partenzaDestinazione) {
        this.partenzaDestinazione = partenzaDestinazione;
    }

    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getNumeroPasseggeri() {
        return numeroPasseggeri;
    }
    public void setNumeroPasseggeri(int numeroPasseggeri) {
        this.numeroPasseggeri = numeroPasseggeri;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }
    public void aggiungiTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    // Metodo statico MOMENTANEO per generare un nuovo codice prenotazione (in assenza di un db)
    public static String generaNuovoCodice() {      // Essendo statico (progressivo) non dovrebbero mai ripetersi codici uguali
        contatoreCodici++;
        return "P" + contatoreCodici;
    }

    @Override
    public String toString() {
        return "Prenotazione: " + codicePrenotazione +
                "\n- Volo: " + codiceVolo +
                "\n- Data: " + dataVolo +
                "\n- Tratta: " + partenzaDestinazione +
                "\n- Stato: " + stato +
                "\n- Passeggeri: " + numeroPasseggeri;
    }

}
