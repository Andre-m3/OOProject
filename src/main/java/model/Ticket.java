package model;

public class Ticket {
    private String nome;
    private String cognome;
    private String numeroDocumento;
    private String dataNascita;
    private String postoAssegnato;          // Il "postoAssegnato" a un passeggero è sempre alfanumerico!
    private String codicePrenotazione;

    public Ticket(String nome, String cognome, String numeroDocumento, String dataNascita, String postoAssegnato, String codicePrenotazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.numeroDocumento = numeroDocumento;
        this.dataNascita = dataNascita;
        this.postoAssegnato = postoAssegnato;
        this.codicePrenotazione = codicePrenotazione;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getDataNascita() {
        return dataNascita;
    }
    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getPostoAssegnato() {
        return postoAssegnato;
    }
    public void setPostoAssegnato(String postoAssegnato) {
        this.postoAssegnato = postoAssegnato;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }
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