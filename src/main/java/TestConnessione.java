import database.ConnessioneDatabase;
import implementazioniPostgresDAO.*;
import java.sql.SQLException;

public class TestConnessione {
    public static void main(String[] args) {
        try {
            System.out.println("🔄 Tentativo di connessione...");
            ConnessioneDatabase db = ConnessioneDatabase.getInstance();

            if (db.connection != null && !db.connection.isClosed()) {
                System.out.println("✅ Connessione al database riuscita!");
                System.out.println("📊 Database: " + db.connection.getCatalog());

                // Test dell'implementazione DAO
                System.out.println("\n🧪 Test dell'implementazione DAO...");
                ImplementazioneUtenteDAO dao = new ImplementazioneUtenteDAO();
                dao.getTuttiUtenti();

                System.out.println("✅ Test completato con successo!");

            } else {
                System.out.println("❌ Connessione fallita");
            }
        } catch (SQLException e) {
            System.out.println("❌ Errore SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Errore generico: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
