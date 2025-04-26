package model;

public class VoloInArrivo extends Volo{
    private String aeroportoOrigine;

    public VoloInArrivo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                        String data, int ritardo, String stato, String tipoVolo, String aeroportoOrigine) {
        super(numeroVolo, compagniaAerea, orarioPrevisto, data, ritardo, stato, tipoVolo);
        this.aeroportoOrigine = aeroportoOrigine;
    }

    public String getAeroportoOrigine() {
        return aeroportoOrigine;
    }

    public void setAeroportoOrigine(String aeroportoOrigine) {
        this.aeroportoOrigine = aeroportoOrigine;
    }

    // OVERRIDE TEMPORANEO - TUTTI I METODI "toString()" VERRANNO SUCCESSIVAMENTE ELIMINATI
    @Override
    public String toString() {
        return super.toString()
                + "\n- TipoVolo: Arrivo"
                + "\n- Origine: " + aeroportoOrigine
                + "\n- Orario Arrivo" + getOrarioPrevisto()
                + "\n- Ritardo: " + getRitardo() + " minuti" + "\n";
    }

}
