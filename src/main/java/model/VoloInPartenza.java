package model;

public class VoloInPartenza {
    private short gateImbarco;
    private String aeroportoDestinazione;

    public VoloInPartenza(short gateImbarco, String aeroportoDestinazione) {
        this.gateImbarco = gateImbarco;
        this.aeroportoDestinazione = aeroportoDestinazione;
    }

    public int getGateImbarco() {
        return gateImbarco;
    }

    public void setGateImbarco(short gateImbarco) {
        this.gateImbarco = gateImbarco;
    }

    public String getAeroportoDestinazione() {
        return aeroportoDestinazione;
    }

    public void setAeroportoDestinazione(String aeroportoDestinazione) {
        this.aeroportoDestinazione = aeroportoDestinazione;
    }
}
