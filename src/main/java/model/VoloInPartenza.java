package model;

/**
 * The type Volo in partenza.
 */
public class VoloInPartenza extends Volo{
    private Short gateImbarco;          // Short è Wrapped, non Primitivo!

    /**
     * Instantiates a new Volo in partenza.
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param ritardo        the ritardo
     * @param stato          the stato
     * @param destinazione   the destinazione
     * @param gateImbarco    the gate imbarco
     */
// Abbiamo un primo Costruttore che accetta l'inserimento del Gate Imbarco (Short)
    public VoloInPartenza(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, int ritardo, StatoVolo stato, String destinazione,
                          Short gateImbarco) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, "Napoli", destinazione);
        this.gateImbarco = gateImbarco;
    }

    /**
     * Instantiates a new Volo in partenza.
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param ritardo        the ritardo
     * @param stato          the stato
     * @param destinazione   the destinazione
     */
// Costruttore che omette il Gate (settato automaticamente a null)
    public VoloInPartenza(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                          String data, int ritardo, StatoVolo stato, String destinazione) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, "Napoli", destinazione);
        this.gateImbarco = null;
    }

    /**
     * Gets gate imbarco.
     *
     * @return the gate imbarco
     */
    public Short getGateImbarco() {
        return gateImbarco;
    }

    /**
     * Sets gate imbarco.
     *
     * @param gateImbarco the gate imbarco
     */
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
