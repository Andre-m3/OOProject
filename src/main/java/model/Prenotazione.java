package model;

import java.util.ArrayList;

public class Prenotazione {

    // Costante che definisce il numero massimo di passeggeri per prenotazione
    public static final int MAX_PASSEGGERI = 9;

    private String codicePrenotazione;
    private String codiceVolo;
    private StatoPrenotazione stato;
    private int numeroPasseggeri;
    private String email;

    public Prenotazione(String codicePrenotazione, String codiceVolo,
                        StatoPrenotazione stato, int numeroPasseggeri, String email) {
        this.codicePrenotazione = codicePrenotazione;
        this.codiceVolo = codiceVolo;
        this.stato = stato;
        this.numeroPasseggeri = numeroPasseggeri;
        this.email = email;
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

    public StatoPrenotazione getStato() {
        return stato;
    }
    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public int getNumeroPasseggeri() {
        return numeroPasseggeri;
    }
    public void setNumeroPasseggeri(int numeroPasseggeri) {
        this.numeroPasseggeri = numeroPasseggeri;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Prenotazione: " + codicePrenotazione +
                "\n- Volo: " + codiceVolo +
                "\n- Stato: " + stato +
                "\n- Passeggeri: " + numeroPasseggeri +
                "\n- Email: " + email;
    }
}
