package model;

public class VoloInArrivo extends Volo{

    public VoloInArrivo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                        String data, int ritardo, String stato, String origine) {
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
