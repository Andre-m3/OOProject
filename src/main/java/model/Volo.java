package model;

public class Volo {
    private int numeroVolo;
    private String compagniaAerea;
    private String orarioPrevisto;
    private String data;
    private String ritardo;
    private String stato;

    public Volo(int numeroVolo, String compagniaAerea, String orarioPrevisto, String data, String ritardo, String stato) {
        this.numeroVolo = numeroVolo;
        this.compagniaAerea = compagniaAerea;
        this.orarioPrevisto = orarioPrevisto;
        this.data = data;
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

    public String getOrarioPrevisto() {
        return orarioPrevisto;
    }

    public void setOrarioPrevisto(String orarioPrevisto) {
        this.orarioPrevisto = orarioPrevisto;
    }

    public String getData() {
        return data;
    }

    public void setDataa(String data) {
        this.data = data;
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
