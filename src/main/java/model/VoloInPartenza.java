package model;

public class VoloInPartenza extends Volo{
    private short gateImbarco;
    private String aeroportoDestinazione;

    public VoloInPartenza(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, int ritardo, String stato, String tipoVolo,
                          short gateImbarco, String aeroportoDestinazione
    ) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, tipoVolo);
        this.gateImbarco = gateImbarco;
        this.aeroportoDestinazione = aeroportoDestinazione;
//        this.setTipoVolo("Partenza");
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

    @Override
    public String toString() {
        return super.toString()
                + "\n- TipoVolo: Partenza"
                + "\n- Destinazione: " + this.getAeroportoDestinazione()
                + "\n- Orario Partenza: " + this.getOrarioPrevisto()
                + "\n- Ritardo: " + this.getRitardo() + " minuti"
                + "\n- Gate Imbarco: " + this.getGateImbarco() + "\n";
    }

}
