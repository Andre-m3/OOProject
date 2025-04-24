package model;

public class VoloInPartenza {
    private int gateImbarco;
    private String destinazione;

    public VoloInPartenza(int gateImbarco, String destinazione) {
        this.gateImbarco = gateImbarco;
        this.destinazione = destinazione;
    }

    public int getGateImbarco() {
        return gateImbarco;
    }

    public void setGateImbarco(int gateImbarco) {
        this.gateImbarco = gateImbarco;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }
}
