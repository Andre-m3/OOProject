package model;

public class Volo {
    private int numeroVolo;
    private String compagniaAerea;
    private String origine;
    private String destinazione;
    private String orarioPartenza;
    private String dataPartenza;
    private String ritardo;
    private String stato;

    public Volo(int numeroVolo, String compagniaAerea, String origine, String destinazione, String orarioPartenza, String dataPartenza, String ritardo, String stato) {
        this.numeroVolo = numeroVolo;
        this.compagniaAerea = compagniaAerea;
        this.origine = origine;
        this.destinazione = destinazione;
        this.orarioPartenza = orarioPartenza;
        this.dataPartenza = dataPartenza;
        this.ritardo = ritardo;
        this.stato = stato;
    }

    public int getNumeroVolo() {
        return numeroVolo;
    }

    public void setNumeroVolo(int numeroVolo) {
        this.numeroVolo = numeroVolo;
    }

    public String getCompagniaAerea() {
        return compagniaAerea;
    }

    public void setCompagniaAerea(String compagniaAerea) {
        this.compagniaAerea = compagniaAerea;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getOrarioPartenza() {
        return orarioPartenza;
    }

    public void setOrarioPartenza(String orarioPartenza) {
        this.orarioPartenza = orarioPartenza;
    }

    public String getDataPartenza() {
        return dataPartenza;
    }

    public void setDataPartenza(String dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public String getRitardo() {
        return ritardo;
    }

    public void setRitardo(String ritardo) {
        this.ritardo = ritardo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
