package model;

public class VoloInPartenza extends Volo{
    private Short gateImbarco;          // Short è Wrapped, non Primitivo!

    // Abbiamo un primo Costruttore che accetta l'inserimento del Gate Imbarco (Short)
    public VoloInPartenza(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, int ritardo, StatoVolo stato, String destinazione,
                          Short gateImbarco) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, "Napoli", destinazione);
        this.gateImbarco = gateImbarco;
    }

    // Costruttore che omette il Gate (settato automaticamente a null)
    public VoloInPartenza(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, int ritardo, StatoVolo stato, String destinazione) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, "Napoli", destinazione);
        this.gateImbarco = null;
    }

    public Short getGateImbarco() {
        return gateImbarco;
    }
    public void setGateImbarco(Short gateImbarco) {
        this.gateImbarco = gateImbarco;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\n- TipoVolo: Partenza"
                + "\n- Orario Partenza: " + this.getOrarioPrevisto()
                + "\n- Ritardo: " + this.getRitardo() + " minuti"
                + "\n- Gate Imbarco: " + (this.getGateImbarco() == null ? "non assegnato" : this.getGateImbarco()) + "\n";
    }


}
