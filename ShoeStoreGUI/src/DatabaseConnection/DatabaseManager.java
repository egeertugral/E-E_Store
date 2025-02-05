package DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManager {

    //burayı değiştirmen lazım. veritabanının hangi path altında olmasını istiyorsan onu vermen lazım
    //adını değiştirebilirsin.Stock db yerine başka bir isim verebilirsin. Eğer veritabanı yoksa kendisi otomatik oluşturur.
    //tablolarda veya veritabanında değişiklik yapmak istiyorsan önce veritabanını bulunduğu path altınta sil.

    private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/HP/OneDrive/Masaüstü/ShoeStoreGUI/Stock.db";


    private static Connection connection;


    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Veritabanına başarıyla bağlanıldı.");
            } catch (SQLException e) {
                System.err.println("Bağlantı hatası: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }


    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            }
        } catch (SQLException e) {
            System.err.println("Bağlantı kapatma hatası: " + e.getMessage());
        }
    }

    //bu metot veritabanı objelerini oluşturur. veritabanı değişiklikerini buraya ekleyebilirsin
    public static void initializeDatabase() throws SQLException {

        try {
        String createProductTableSQL = "CREATE TABLE IF NOT EXISTS Product ("
                + "ProductId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "brand TEXT NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "size TEXT NOT NULL"
                + ");";


        String createSaleTableSQL = "CREATE TABLE IF NOT EXISTS Sale ("
                + "SaleId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ProductName TEXT NOT NULL, "
                + "quantitySold INTEGER NOT NULL, "
                + "price REAL NOT NULL, "
                + "purchaseDate TEXT NOT NULL"
                + ");";

        Connection conn = getConnection();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createProductTableSQL);
                System.out.println("Product tablosu başarıyla oluşturuldu.");
                stmt.execute(createSaleTableSQL);
                System.out.println("Sale tablosu başarıyla oluşturuldu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

}
