import DatabaseConnection.DatabaseManager;

import java.sql.SQLException;
public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseManager manager=new DatabaseManager();
        manager.initializeDatabase();
        new ShoeStoreGUI();
    }
}