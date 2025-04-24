package model;

public class VoloInPartenza {
    private int gateImbarco;
    private String aeroportoDestinazione;

    public VoloInPartenza(int gateImbarco, String aeroportoDestinazione) {
        this.gateImbarco = gateImbarco;
        this.aeroportoDestinazione = aeroportoDestinazione;
    }

    public int getGateImbarco() {
        return gateImbarco;
    }

    public void setGateImbarco(int gateImbarco) {
        this.gateImbarco = gateImbarco;
    }

    public String getAeroportoDestinazione() {
        return aeroportoDestinazione;
    }

    public void setAeroportoDestinazione(String aeroportoDestinazione) {
        this.aeroportoDestinazione = aeroportoDestinazione;
    }
}
