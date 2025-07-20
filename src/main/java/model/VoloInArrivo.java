package model;

/**
 * The type Volo in arrivo.
 */
public class VoloInArrivo extends Volo{

    /**
     * Instantiates a new Volo in arrivo.
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param ritardo        the ritardo
     * @param stato          the stato
     * @param origine        the origine
     */
    public VoloInArrivo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                        String data, int ritardo, StatoVolo stato, String origine) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, origine, "Napoli");
    }

    @Override
    public String toString() {
        return super.toString()
                + "\n- TipoVolo: Arrivo"
                + "\n- Orario Arrivo: " + this.getOrarioPrevisto()
                + "\n- Ritardo: " + this.getRitardo() + " minuti" + "\n";
    }

}
