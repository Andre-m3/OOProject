package model;

public class Prenotazione {
    private String codicePrenotazione;
    private String stato;

    public Prenotazione(String codicePrenotazione, String stato) {
        this.codicePrenotazione = codicePrenotazione;
        this.stato = stato;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public void setCodicePrenotazione(String codicePrenotazione) {
        this.codicePrenotazione = codicePrenotazione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
