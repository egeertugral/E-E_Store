package DAO;
import Model.Sale;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DatabaseConnection.DatabaseManager;


//Sale tablosu için veritabanı işlemlerin yönetildiği sınıf
public class SaleDAO {

    public void insertSale(Sale sale) throws SQLException {
        String sql = "INSERT INTO Sale (productName, quantitySold, price, purchaseDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, sale.getProductName());
            pstmt.setInt(2, sale.getQuantitySold());
            pstmt.setDouble(3, sale.getPrice());
            pstmt.setString(4, sale.getDate());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Satış işlemi başarılı.");
            } else {
                System.out.println("satış gerçekleşemedi !.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
    }

    public void updateSale(Sale sale) throws SQLException {
        String sql = "UPDATE Sale SET productName = ?, quantitySold = ?, price = ?, purchaseDate = ? WHERE SaleId = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, sale.getProductName());
            pstmt.setInt(2, sale.getQuantitySold());
            pstmt.setDouble(3, sale.getPrice());
            pstmt.setString(4, sale.getDate());
            pstmt.setInt(5, sale.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Satış başarıyla güncellendi.");
            } else {
                System.out.println("Belirtilen id'ye sahip satış bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
    }

    public void deleteSale(int id) throws SQLException {
        String sql = "DELETE FROM Sale WHERE SaleId = ?";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Satış başarıyla silindi.");
            } else {
                System.out.println("Belirtilen id'ye sahip satış bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
    }

    public List<Sale> getAllSales() throws SQLException {
        String sql = "SELECT * FROM Sale";
        List<Sale> sales = new ArrayList<>();
        try (Statement stmt = DatabaseManager.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("SaleId"));
                sale.setProductName(rs.getString("productName"));
                sale.setQuantitySold(rs.getInt("quantitySold"));
                sale.setPrice(rs.getDouble("price"));
                sale.setDate(rs.getString("purchaseDate"));
                sales.add(sale);
            }
        }  catch (SQLException e) {
        System.err.println("Veritabanı hatası: " + e.getMessage());
        throw e;
    }

        return sales;
    }
    public void exportSalesToFile(String filePath) throws IOException, SQLException {
        List<Sale> sales = getAllSales();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Sale sale : sales) {
                writer.write(sale.getId() + "," + sale.getProductName() + "," + sale.getQuantitySold() + "," + sale.getPrice() + "," + sale.getDate());
                writer.newLine();
            }
            System.out.println("Satış verileri başarıyla dışa aktarıldı.");
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
            throw e;
        }
    }
    // Validate CSV content
    private void validateCSVContent(String[] fields) {
        if (fields.length != 5) {
            throw new IllegalArgumentException("Geçersiz veri yapısı. Her satırda 5 alan olmalıdır.");
        }

        try {
            Integer.parseInt(fields[0]); // ProductId
            Integer.parseInt(fields[3]); // Quantity
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Geçersiz sayı formatı: " + e.getMessage());
        }

        if (fields[1].isEmpty() || fields[2].isEmpty() || fields[4].isEmpty()) {
            throw new IllegalArgumentException("Boş alanlar tespit edildi.");
        }
    }
    public void importSalesFromFile(String filePath) throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    Sale sale = new Sale();
                    sale.setId(Integer.parseInt(data[0]));
                    sale.setProductName(data[1]);
                    sale.setQuantitySold(Integer.parseInt(data[2]));
                    sale.setPrice(Double.parseDouble(data[3]));
                    sale.setDate(data[4]);
                    insertSale(sale);
                }
            }
            System.out.println("Satış verileri başarıyla içe aktarıldı.");
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
            throw e;
        }
    }


    public List<Sale> getAllSalesByProductName(String name) throws SQLException {
        String sql = "SELECT * FROM Sale where productName like '%"+name+"%'";
        List<Sale> sales = new ArrayList<>();
        try (Statement stmt = DatabaseManager.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("SaleId"));
                sale.setProductName(rs.getString("productName"));
                sale.setQuantitySold(rs.getInt("quantitySold"));
                sale.setPrice(rs.getDouble("price"));
                sale.setDate(rs.getString("purchaseDate"));
                sales.add(sale);
            }
        }  catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }

        return sales;
    }

    public List<Sale> getAllSalesByDate(String date) throws SQLException {
        String sql = "SELECT * FROM Sale where  saleDate  ='"+date+"'";
        List<Sale> sales = new ArrayList<>();
        try (Statement stmt = DatabaseManager.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("SaleId"));
                sale.setProductName(rs.getString("productName"));
                sale.setQuantitySold(rs.getInt("quantitySold"));
                sale.setPrice(rs.getDouble("price"));
                sale.setDate(rs.getString("purchaseDate"));
                sales.add(sale);
            }
        }  catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }

        return sales;
    }

    public List<Sale> getAllSalesByQuantity(int quantity) throws SQLException {
        String sql = "SELECT * FROM Sale where quantitySold='"+String.valueOf(quantity)+"'";
        List<Sale> sales = new ArrayList<>();
        try (Statement stmt = DatabaseManager.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("SaleId"));
                sale.setProductName(rs.getString("productName"));
                sale.setQuantitySold(rs.getInt("quantitySold"));
                sale.setPrice(rs.getDouble("price"));
                sale.setDate(rs.getString("purchaseDate"));
                sales.add(sale);
            }
        }  catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }

        return sales;
    }

    public List<Sale> getAllSalesByPrice(double price) throws SQLException {
        String sql = "SELECT * FROM Sale where  price ='"+String.valueOf(price)+"'";
        List<Sale> sales = new ArrayList<>();
        try (Statement stmt = DatabaseManager.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("SaleId"));
                sale.setProductName(rs.getString("productName"));
                sale.setQuantitySold(rs.getInt("quantitySold"));
                sale.setPrice(rs.getDouble("price"));
                sale.setDate(rs.getString("purchaseDate"));
                sales.add(sale);
            }
        }  catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }

        return sales;
    }


}
