package DAO;
import java.io.*;
import DatabaseConnection.DatabaseManager;
import Model.Product;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.stream.Collectors;

// product tablosu ile ilgili veritabanı işlemlerin yönetildiği sınıf
public class ProductDAO {

    public void insertProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product(name, brand, quantity, size) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getBrand());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setString(4, product.getSize());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Ürün başarıyla eklendi.");
            } else {
                System.out.println("Ürün eklenemedi.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
    }

    public void deleteProductById(int id) throws SQLException {
        String sql = "DELETE FROM Product WHERE ProductId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Ürün başarıyla silindi.");
            } else {
                System.out.println("Belirtilen id'ye sahip ürün bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Product SET name = ?, brand = ?, quantity = ?, size = ? WHERE ProductId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getBrand());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setString(4, product.getSize());
            pstmt.setInt(5, product.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Ürün başarıyla güncellendi.");
            } else {
                System.out.println("Belirtilen id'ye sahip ürün bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT ProductId, name, brand, quantity, size FROM Product";
        List<Product> productList = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("ProductId"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSize(rs.getString("size"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }

        return productList;
    }
 // Export products to a CSV file
    public void exportProductsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ProductId,Name,Brand,Quantity,Size\n");
            for (Product product : getAllProducts()) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getBrand() + "," + product.getQuantity() + "," + product.getSize() + "\n");
            }
            System.out.println("Ürünler başarıyla dosyaya aktarıldı: " + filePath);
        } catch (IOException | SQLException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // Import products from a CSV file
    public void importProductsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Skip the header line

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length == 5) {
                    Product product = new Product();
                    product.setId(Integer.parseInt(fields[0]));
                    product.setName(fields[1]);
                    product.setBrand(fields[2]);
                    product.setQuantity(Integer.parseInt(fields[3]));
                    product.setSize(fields[4]);

                    insertProduct(product);
                }
            }
            System.out.println("Ürünler başarıyla dosyadan içe aktarıldı: " + filePath);
        } catch (IOException | SQLException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
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
    //verilen isme benzer ürünleri listeleme
    public List<Product> getAllProductsByName(String name) throws SQLException{
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT ProductId, name, brand, quantity, size FROM Product where name like '%"+name+"%'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("ProductId"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSize(rs.getString("size"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
        return productList;
    }

    //verilen size a uygun ürünleri listeleme
    public List<Product> getAllProductsBySize(String size) throws SQLException{
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT ProductId, name, brand, quantity, size FROM Product where size ='"+size+"'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("ProductId"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSize(rs.getString("size"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
        return productList;
    }

    //verilen adet e göre ürünleri listeleme
    public List<Product> getAllProductsByQuantity(int quantity) throws SQLException{
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT ProductId, name, brand, quantity, size FROM Product where quantity ='"+String.valueOf(quantity)+"'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("ProductId"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSize(rs.getString("size"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
        return productList;
    }

    //verilen markaya göre ürünleri listeleme
    public List<Product> getAllProductsByBrand(String brand) throws SQLException{
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT ProductId, name, brand, quantity, size FROM Product where brand like '%"+brand+"%'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("ProductId"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setQuantity(rs.getInt("quantity"));
                product.setSize(rs.getString("size"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
            throw e;
        }
        return productList;
    }

    public List<String> getAllProductNames() throws SQLException {
        String sql = "SELECT name FROM Product";
        List<String> productNames = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                productNames.add(rs.getString("name"));
            }
        }
        return productNames.stream().collect(Collectors.toList());
    }
}
