package model;

import java.util.ArrayList;

public class Volo {
    // Lista statica che conterrà tutti i voli
    private static ArrayList<Volo> listaVoli = new ArrayList<>();

    private String numeroVolo;      // Codice Alfanumerico [a-zA-Z0-9]
    private String compagniaAerea;
    private String orarioPrevisto;
    private String data;
    private int ritardo;            // espresso in minuti!
    private String stato;
    private String partenza;        // Aeroporto di partenza
    private String destinazione;    // Aeroporto di destinazione

    public Volo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                String data, int ritardo, String stato, String partenza, String destinazione) {
        this.numeroVolo = numeroVolo;
        this.compagniaAerea = compagniaAerea;
        this.orarioPrevisto = orarioPrevisto;
        this.data = data;
        this.ritardo = ritardo;
        this.stato = stato;
        this.partenza = partenza;
        this.destinazione = destinazione;
    }

    public String getNumeroVolo() {
        return numeroVolo;
    }
    public void setNumeroVolo(String numeroVolo) {
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
    public void setData(String data) {
        this.data = data;
    }

    public int getRitardo() {
        return ritardo;
    }
    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getPartenza() {
        return partenza;
    }
    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    public String getDestinazione() {
        return destinazione;
    }
    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    // Metodo per aggiungere un volo alla lista
    public static void aggiungiVolo(Volo volo) {
        listaVoli.add(volo);
    }

    // Metodo per ottenere tutti i voli
    public static ArrayList<Volo> getListaVoli() {
        return listaVoli;
    }

    @Override
    public String toString() {
        return "- Volo: " + this.getCompagniaAerea() + " " + this.getNumeroVolo()
                + "\n- Data: " + this.getData()
                + "\n- Tratta: " + this.getPartenza() + " → " + this.getDestinazione()
                + "\n- Stato: " + this.getStato();
    }

}
