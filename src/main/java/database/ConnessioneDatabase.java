package database;

// Utilizziamo un logger per gestire gli output a schermo secondo quanto consigliato da Sonarqube
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Connessione database.
 */
public class ConnessioneDatabase {

    // logger!
    Logger logger = Logger.getLogger(getClass().getName());

    // ATTRIBUTI
    private static ConnessioneDatabase instance;
    /**
     * The Connection.
     */
    public Connection connection = null;
    private String nome = "postgres";
    private String password = "Admin3!";
    private String url = "jdbc:postgresql://localhost:5432/gestione_voli";      // La porta fornita dal fork di git era 5433 (ma quella corretta per noi è 5432)
    private String driver = "org.postgresql.Driver";

    // COSTRUTTORE
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);

        } catch (ClassNotFoundException ex) {
            logger.warning("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    /**
     * Gets instance.
     * Il pattern singleton ci causa un errore se correggiamo la segnalazione di SonarQube
     * Pertanto è stato scelto di non effettuare alcuna modifica (errore segnalato "lieve", quindi va bene così)
     *
     * @return the instance
     * @throws SQLException the sql exception
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
}