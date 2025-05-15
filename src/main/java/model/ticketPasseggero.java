package model;

public class ticketPasseggero {
    private int numeroTicket;
    private String nomeCompleto;
    private String cognome;
    private String dataNascita;
    private String numeroDocumento;
    private int postoAssegnato;

    public ticketPasseggero(int numeroTicket, String nomeCompleto, String cognome, String dataNascita, String numeroDocumento, int postoAssegnato) {
        this.numeroTicket = numeroTicket;
        this.nomeCompleto = nomeCompleto;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.numeroDocumento = numeroDocumento;
        this.postoAssegnato = postoAssegnato;
    }

    public int getNumeroTicket() {
        return numeroTicket;
    }
    public void setNumeroTicket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }
    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getPostoAssegnato() {
        return postoAssegnato;
    }
    public void setPostoAssegnato(int postoAssegnato) {
        this.postoAssegnato = postoAssegnato;
    }
}
