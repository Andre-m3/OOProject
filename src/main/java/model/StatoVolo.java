package model;

/**
 * The enum Stato volo.
 */
public enum StatoVolo {
    /**
     * Programmato stato volo.
     */
    PROGRAMMATO,        // [enum 0] Unico stato valido per la prenotazione da parte dell'utente!
    /**
     * Decollato stato volo.
     */
    DECOLLATO,          // [enum 1]
    /**
     * In ritardo stato volo.
     */
    IN_RITARDO,         // [enum 2]
    /**
     * Atterrato stato volo.
     */
    ATTERRATO,          // [enum 3]
    /**
     * The Cancellato.
     */
    CANCELLATO          // [enum 4]
}
