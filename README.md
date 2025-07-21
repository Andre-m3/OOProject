# OOProject
Un'applicazione Java sviluppata seguendo i principi della programmazione orientata agli oggetti, con la relativa implementazione di interfacce grafiche e integrazione del database in PostgreSQL.

## Panoramica
Questo progetto implementa un'applicazione desktop in Java che utilizza un'architettura BCE + DAO (Boundary-Control-Entity + DAO) per la gestione dei dati tramite database PostgreSQL.

## Caratteristiche Principali
- **Interfaccia Grafica**: GUI intuitiva "User-Friendly" per l'interazione utente, ridotta al minimo indispensabile
- **Database Integration**: Connessione e gestione dati tramite Database, implementato nel DBMS Postrgres
- **Architettura Utilizzata**: Separazione logica e responsabilità tra gui, controller e classi del model
- **Pattern DAO**: Implementazione del "Data Access Object" pattern (acronimo di DAO)
- **Object-Oriented Design**: Struttura modulare di facile manutenzione, che preserva le 3 principali proprietà (Incapsulamento, Ereditarietà, Polimorfismo)

## Tecnologie Utilizzate
- **Java 23** - Versione del modulo JDK utilizzato per l'intero progetto
- **Maven** - Gestione dipendenze e build automation, imposta e studiata nel corso
- **PostgreSQL** - Database relazionale visto nel dettaglio nel corso di Basi di Dati
- **PostgreSQL JDBC Driver** (v42.7.5) - Driver predisposto alla connessione al database in Postgres
- **IntelliJ Idea Ultimate** (v2025.1.3) - IDE sotto licenza ufficiale visto nel corso

## Esecuzione da IDE
1. Importare il progetto come "progetto Maven"
2. Configurare il JDK 23
3. Eseguire la classe principale con il metodo `main()` (LandingPageLogin)

## Come utilizzare il programma
1. **Avvio dell'Applicazione**: Esegui il programma seguendo le istruzioni sopra
2. **Login/Registrazione Utente**: Effettua il login con le tue credenziali, o crea un nuovo account
3. **Interfaccia Grafica**: Utilizza la GUI Dashboard (relativa al tuo livello di accesso) per navigare tra le funzionalità
4. **Gestione Dati**: L'applicazione si connette automaticamente al database PostgreSQL configurato
5. **Operazioni C-R-U-D**: Esegui operazioni di Create, Read, Update, Delete tramite l'interfaccia apposita

## Configurazione Database
Assicurati che il database PostgreSQL sia configurato correttamente:
- **Host**: localhost (Nel nostro caso il Database non è gestito da Server attivi h24, bensì su macchina locale)
- **Porta**: 5432 (porta default PostgreSQL, nel nostro caso non è stata 5433 come suggerito a lezione)
- **Nome Database**: gestione_voli
- **Username/Password**: Credenziali fornite privatamente!

## Autori del Progetto
Antonio Andrea Montella - N86005652
Adriano Montella        - N86005823
