package model;

/**
 * The type Ticket.
 */
public class Ticket {
    private String nome;
    private String cognome;
    private String numeroDocumento;
    private String dataNascita;
    private String postoAssegnato;          // Il "postoAssegnato" a un passeggero è sempre alfanumerico!
    private String codicePrenotazione;

    /**
     * Instantiates a new Ticket.
     *
     * @param nome               the nome
     * @param cognome            the cognome
     * @param numeroDocumento    the numero documento
     * @param dataNascita        the data nascita
     * @param postoAssegnato     the posto assegnato
     * @param codicePrenotazione the codice prenotazione
     */
    public Ticket(String nome, String cognome, String numeroDocumento, String dataNascita, String postoAssegnato, String codicePrenotazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.numeroDocumento = numeroDocumento;
        this.dataNascita = dataNascita;
        this.postoAssegnato = postoAssegnato;
        this.codicePrenotazione = codicePrenotazione;
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets cognome.
     *
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Sets cognome.
     *
     * @param cognome the cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Gets numero documento.
     *
     * @return the numero documento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Sets numero documento.
     *
     * @param numeroDocumento the numero documento
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * Gets data nascita.
     *
     * @return the data nascita
     */
    public String getDataNascita() {
        return dataNascita;
    }

    /**
     * Sets data nascita.
     *
     * @param dataNascita the data nascita
     */
    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Gets posto assegnato.
     *
     * @return the posto assegnato
     */
    public String getPostoAssegnato() {
        return postoAssegnato;
    }

    /**
     * Sets posto assegnato.
     *
     * @param postoAssegnato the posto assegnato
     */
    public void setPostoAssegnato(String postoAssegnato) {
        this.postoAssegnato = postoAssegnato;
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

    @Override
    public String toString() {
        return "Ticket:" +
                "\n- Nome: " + nome +
                "\n- Cognome: " + cognome +
                "\n- Documento: " + numeroDocumento +
                "\n- Data Nascita: " + dataNascita +
                "\n- Posto: " + postoAssegnato +
                "\n- Codice Volo: " + codicePrenotazione;
    }
}