package model;

public enum StatoVolo {
    PROGRAMMATO,        // [enum 0] Unico stato "prenotabile" da un'utente!
    DECOLLATO,          // [enum 1]
    IN_RITARDO,         // [enum 2]
    ATTERRATO,          // [enum 3]
    CANCELLATO          // [enum 4]
}
