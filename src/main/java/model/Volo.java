package model;

/**
 * The type Volo.
 */
public class Volo {

    private String numeroVolo;      // Codice Alfanumerico [a-zA-Z0-9]
    private String compagniaAerea;
    private String orarioPrevisto;
    private String data;
    private int ritardo;            // espresso in minuti!
    private StatoVolo stato;
    private String partenza;        // Aeroporto di partenza
    private String destinazione;    // Aeroporto di destinazione

    /**
     * Instantiates a new Volo.
     *
     * @param numeroVolo     the numero volo
     * @param compagniaAerea the compagnia aerea
     * @param orarioPrevisto the orario previsto
     * @param data           the data
     * @param ritardo        the ritardo
     * @param stato          the stato
     * @param partenza       the partenza
     * @param destinazione   the destinazione
     */
    public Volo(String numeroVolo, String compagniaAerea, String orarioPrevisto,
                String data, int ritardo, StatoVolo stato, String partenza, String destinazione) {
        this.numeroVolo = numeroVolo;
        this.compagniaAerea = compagniaAerea;
        this.orarioPrevisto = orarioPrevisto;
        this.data = data;
        this.ritardo = ritardo;
        this.stato = stato;
        this.partenza = partenza;
        this.destinazione = destinazione;
    }

    /**
     * Gets numero volo.
     *
     * @return the numero volo
     */
    public String getNumeroVolo() {
        return numeroVolo;
    }

    /**
     * Sets numero volo.
     *
     * @param numeroVolo the numero volo
     */
    public void setNumeroVolo(String numeroVolo) {
        this.numeroVolo = numeroVolo;
    }

    /**
     * Gets compagnia aerea.
     *
     * @return the compagnia aerea
     */
    public String getCompagniaAerea() {
        return compagniaAerea;
    }

    /**
     * Sets compagnia aerea.
     *
     * @param compagniaAerea the compagnia aerea
     */
    public void setCompagniaAerea(String compagniaAerea) {
        this.compagniaAerea = compagniaAerea;
    }

    /**
     * Gets orario previsto.
     *
     * @return the orario previsto
     */
    public String getOrarioPrevisto() {
        return orarioPrevisto;
    }

    /**
     * Sets orario previsto.
     *
     * @param orarioPrevisto the orario previsto
     */
    public void setOrarioPrevisto(String orarioPrevisto) {
        this.orarioPrevisto = orarioPrevisto;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets ritardo.
     *
     * @return the ritardo
     */
    public int getRitardo() {
        return ritardo;
    }

    /**
     * Sets ritardo.
     *
     * @param ritardo the ritardo
     */
    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    /**
     * Gets stato.
     *
     * @return the stato
     */
    public StatoVolo getStato() {
        return stato;
    }

    /**
     * Sets stato.
     *
     * @param stato the stato
     */
    public void setStato(StatoVolo stato) {
        this.stato = stato;
    }

    /**
     * Gets partenza.
     *
     * @return the partenza
     */
    public String getPartenza() {
        return partenza;
    }

    /**
     * Sets partenza.
     *
     * @param partenza the partenza
     */
    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    /**
     * Gets destinazione.
     *
     * @return the destinazione
     */
    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Sets destinazione.
     *
     * @param destinazione the destinazione
     */
    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    @Override
    public String toString() {
        return "- Volo: " + this.getCompagniaAerea() + " " + this.getNumeroVolo()
                + "\n- Data: " + this.getData()
                + "\n- Tratta: " + this.getPartenza() + " → " + this.getDestinazione()
                + "\n- Stato: " + this.getStato();
    }

}
