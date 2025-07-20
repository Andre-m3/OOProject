package model;

/**
 * The enum Stato prenotazione.
 */
public enum StatoPrenotazione {
    /**
     * Confermata stato prenotazione.
     */
    CONFERMATA,
    /**
     * In attesa stato prenotazione.
     */
    IN_ATTESA,  // Quando una prenotazione viene effettuata, questa viene creata con lo stato "IN_ATTESA" sempre!
    /**
     * Cancellata stato prenotazione.
     */
    CANCELLATA
}
