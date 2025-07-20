package model;

/**
 * The type Prenotazione.
 */
public class Prenotazione {

    // NB.: Una prenotazione ha un massimo di 9 passeggeri. Vincolo imposto da noi per gestire con chiarezza eventuali problemi di prenotazione del volo!

    private String codicePrenotazione;
    private String codiceVolo;
    private StatoPrenotazione stato;
    private int numeroPasseggeri;
    private String email;

    /**
     * Instantiates a new Prenotazione.
     *
     * @param codicePrenotazione the codice prenotazione
     * @param codiceVolo         the codice volo
     * @param stato              the stato
     * @param numeroPasseggeri   the numero passeggeri
     * @param email              the email
     */
    public Prenotazione(String codicePrenotazione, String codiceVolo,
                        StatoPrenotazione stato, int numeroPasseggeri, String email) {
        this.codicePrenotazione = codicePrenotazione;
        this.codiceVolo = codiceVolo;
        this.stato = stato;
        this.numeroPasseggeri = numeroPasseggeri;
        this.email = email;
    }

    /**
     * Gets codice prenotazione.
     *
     * @return the codice prenotazione
     */
    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    /**
     * Sets codice prenotazione.
     *
     * @param codicePrenotazione the codice prenotazione
     */
    public void setCodicePrenotazione(String codicePrenotazione) {
        this.codicePrenotazione = codicePrenotazione;
    }

    /**
     * Gets codice volo.
     *
     * @return the codice volo
     */
    public String getCodiceVolo() {
        return codiceVolo;
    }

    /**
     * Sets codice volo.
     *
     * @param codiceVolo the codice volo
     */
    public void setCodiceVolo(String codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    /**
     * Gets stato.
     *
     * @return the stato
     */
    public StatoPrenotazione getStato() {
        return stato;
    }

    /**
     * Sets stato.
     *
     * @param stato the stato
     */
    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    /**
     * Gets numero passeggeri.
     *
     * @return the numero passeggeri
     */
    public int getNumeroPasseggeri() {
        return numeroPasseggeri;
    }

    /**
     * Sets numero passeggeri.
     *
     * @param numeroPasseggeri the numero passeggeri
     */
    public void setNumeroPasseggeri(int numeroPasseggeri) {
        this.numeroPasseggeri = numeroPasseggeri;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
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
