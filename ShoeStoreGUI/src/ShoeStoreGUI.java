import DAO.ProductDAO;
import DAO.SaleDAO;
import Model.Product;
import Model.Sale;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
public class ShoeStoreGUI {

    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Sale> sales = new ArrayList<>();
    private JTable productTable;
    private JTable salesTable;

    ProductDAO productDAO=new ProductDAO();
    SaleDAO saleDao =new SaleDAO();

    public ShoeStoreGUI() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("E-E Store Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        JPanel mainPanel = new JPanel(new CardLayout());

        JPanel welcomePanel = createWelcomePanel(mainPanel);
        JPanel managePanel = createManagePanel();

        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(managePanel, "Manage");

        frame.add(mainPanel);
        frame.setVisible(true);

        // Show welcome panel by default
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "Welcome");
    }
    
public class RoundedButton extends JButton {

    private Color normalColor = new Color(220, 20, 60); // Normal arka plan rengi
    private Color hoverColor = new Color(72,118,255); // Hover rengi

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setBackground(normalColor); // Başlangıç rengi
        setFont(new Font("Arial", Font.BOLD, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Mouse listener ekleniyor
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor); // Hover rengi
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor); // Normal renk
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Antialiasing for smoother edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Draw the text
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Border color
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

        g2.dispose();
    }
}
    public class MovingTextPanel extends JPanel {
        private String text = "- Welcome to the E-E Store Management System -";
        private int x = 0; // Starting position of the text
        private int y = 50; // Vertical position of the text
        private final Timer timer;

        public MovingTextPanel() {
            setBackground(new Color(60, 60, 60));
            setPreferredSize(new Dimension(800, 100)); // Panel size

            // Timer to move text
            timer = new Timer(5, e -> {
                x -= 2; // Move text to the left
                if (x + getFontMetrics(getFont()).stringWidth(text) < 0) {
                    x = getWidth(); // Reset position when text goes off screen
                }
                repaint(); // Repaint the panel
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the text
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);
        }

        public void stopAnimation() {
            timer.stop();
        }

        public void startAnimation() {
            timer.start();
        }
    }
   
    private JPanel createWelcomePanel(JPanel mainPanel) {
    	 JPanel panel = new JPanel(new BorderLayout());
         panel.setBackground(new Color(60, 60, 60));

         ImageIcon originalIcon = new ImageIcon("C:\\Users\\HP\\OneDrive\\Masaüstü\\ayakkabı.png");
         Image scaledImage = originalIcon.getImage().getScaledInstance(550, 350, Image.SCALE_SMOOTH); // Boyutlandırma
         ImageIcon scaledIcon = new ImageIcon(scaledImage);

         JLabel backgroundLabel = new JLabel(scaledIcon, SwingConstants.CENTER);
         panel.add(backgroundLabel, BorderLayout.CENTER);

         MovingTextPanel movingTextPanel = new MovingTextPanel();

         JPanel labelPanel = new JPanel(new BorderLayout());
         labelPanel.setBackground(new Color(60, 60, 60));
         labelPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Üstten 20 piksel boşluk
         labelPanel.add(movingTextPanel, BorderLayout.CENTER);
         panel.add(labelPanel, BorderLayout.NORTH);
         
         RoundedButton continueButton = new RoundedButton("Continue to Dashboard");
         
         continueButton.addActionListener(e -> {
             CardLayout cl = (CardLayout) mainPanel.getLayout();
             cl.show(mainPanel, "Manage");
         });

         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         buttonPanel.setOpaque(false);
         buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Alttan 20 piksel boşluk

         buttonPanel.add(continueButton);

         panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createManagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        JPanel navPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        navPanel.setBackground(new Color(30, 30, 30));

        JButton btnProducts = new JButton("Manage Products");
        JButton btnSales = new JButton("Manage Sales");

        styleButton(btnProducts, new Color(70, 130, 180), 20);
        styleButton(btnSales, new Color(70, 130, 180), 20);

        navPanel.add(btnProducts);
        navPanel.add(btnSales);

        JPanel contentPanel = new JPanel(new CardLayout());
        JPanel productPanel = createProductPanel();
        JPanel salesPanel = createSalesPanel();

        contentPanel.add(productPanel, "Products");
        contentPanel.add(salesPanel, "Sales");

        btnProducts.addActionListener(e -> switchPanel(contentPanel, "Products"));
        btnSales.addActionListener(e -> switchPanel(contentPanel, "Sales"));

        panel.add(navPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProductPanel()  {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 40));

        DefaultTableModel productTableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Brand", "Quantity", "Size"}, 0);
        productTable = new JTable(productTableModel);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        centerTableContent(productTable);

        JScrollPane tableScroll = new JScrollPane(productTable);
        tableScroll.getViewport().setBackground(new Color(60, 60, 60));
        panel.add(tableScroll, BorderLayout.CENTER);

        JPanel filterPanel = createFilterPanel(productTableModel, productTable, products, "Model.Product");
        panel.add(filterPanel, BorderLayout.NORTH);

        JPanel actionPanel = createActionPanel(productTableModel, productTable, products, "Model.Product");
        
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Yeni butonlar
        JButton exportButton = new JButton("Export Products");
        JButton importButton = new JButton("Import Products");

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(panel);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                new ProductDAO().exportProductsToFile(filePath);
                JOptionPane.showMessageDialog(panel, "Products exported successfully!");
            }
        });
        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(panel);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    new ProductDAO().importProductsFromFile(filePath);
                    JOptionPane.showMessageDialog(panel, "Products imported successfully!");
                    updateProductTable(productTableModel); // Update the table after importing
                } catch (SQLException sqlEx) {
                    JOptionPane.showMessageDialog(panel, "Database error during import: " + sqlEx.getMessage());
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(panel, "Invalid file format: " + iae.getMessage());
                } catch (Exception ex) { // General exception for any other unexpected errors
                    JOptionPane.showMessageDialog(panel, "An error occurred during import: " + ex.getMessage());
                }
            }
        });

        actionPanel.add(exportButton);
        actionPanel.add(importButton);
        return panel;
    }

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 40));

        DefaultTableModel salesTableModel = new DefaultTableModel(new Object[]{"Model.Sale ID", "Model.Product Name", "Quantity Sold", "Price (TL)", "Date"}, 0);
        salesTable = new JTable(salesTableModel);
        salesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        centerTableContent(salesTable);

        JScrollPane tableScroll = new JScrollPane(salesTable);
        tableScroll.getViewport().setBackground(new Color(60, 60, 60));
        panel.add(tableScroll, BorderLayout.CENTER);

        JPanel filterPanel = createFilterPanel(salesTableModel, salesTable, sales, "Model.Sale");
        panel.add(filterPanel, BorderLayout.NORTH);

        JPanel actionPanel = createActionPanel(salesTableModel, salesTable, sales, "Model.Sale");
     // Import and Export buttons
        JButton exportButton = new JButton("Export Sales");
        JButton importButton = new JButton("Import Sales");

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(panel);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    new SaleDAO().exportSalesToFile(filePath);
                    JOptionPane.showMessageDialog(panel, "Sales exported successfully!");
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(panel, "Error exporting sales: " + ioe.getMessage());
                } catch (SQLException sqle) {
                    JOptionPane.showMessageDialog(panel, "Database error during export: " + sqle.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Unexpected error: " + ex.getMessage());
                }
            }
        });

        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(panel);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    new SaleDAO().importSalesFromFile(filePath);
                    updateSaleTable(salesTableModel);
                    JOptionPane.showMessageDialog(panel, "Sales imported successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error importing sales: " + ex.getMessage());
                }
            }
        });

        actionPanel.add(exportButton);
        actionPanel.add(importButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

 

    private JPanel createFilterPanel(DefaultTableModel tableModel, JTable table, List<?> data, String type) {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(50, 50, 50));
        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setForeground(Color.WHITE);
        filterPanel.add(filterLabel);

        JTextField filterField = new JTextField(15);
        JComboBox<String> filterType = new JComboBox<>(type.equals("Model.Product") ? new String[]{"Select", "Name", "Brand", "Quantity", "Size"} : new String[]{"Select", "Model.Product Name", "Quantity Sold", "Price", "Date"});
        JButton filterButton = new JButton("Apply Filter");
        JButton clearFilterButton = new JButton("Clear Filter");

        styleButton(filterButton, new Color(34, 139, 34), 14);
        styleButton(clearFilterButton, new Color(220, 20, 60), 14);

        filterPanel.add(filterType);
        filterPanel.add(filterField);
        filterPanel.add(filterButton);
        filterPanel.add(clearFilterButton);

        filterButton.addActionListener(e -> {
            String filterText = filterField.getText().trim().toLowerCase();
            String selectedFilter = (String) filterType.getSelectedItem();
            List<?> filteredData;

            if (type.equals("Model.Product")) {
                try {
                    filteredData= filterProductData(selectedFilter, filterText);  //productfilter düzenlendi
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    filteredData = filterSaleData(selectedFilter, filterText);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

            tableModel.setRowCount(0);
            for (Object obj : filteredData) {
                if (type.equals("Model.Product")) {
                    Product product = (Product) obj;
                    tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getBrand(), product.getQuantity(), product.getSize()});
                } else {
                    Sale sale = (Sale) obj;
                    tableModel.addRow(new Object[]{sale.getId(), sale.getProductName(), sale.getQuantitySold(), sale.getPrice() + " TL", sale.getDate()});
                }
            }
        });

        clearFilterButton.addActionListener(e -> {
            filterField.setText("");
            filterType.setSelectedIndex(0);
            tableModel.setRowCount(0);

                if (type.equals("Model.Product")) {
                    try {
                        updateProductTable(tableModel);  // tüm ürünleri bi daha yükle
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else
                {
                    try {
                        updateSaleTable(tableModel);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

        });

        return filterPanel;
    }

    private List<Product> filterProductData(String selectedFilter, String filterText) throws SQLException {

        List<Product> filteredProductList = new ArrayList<Product>();
        switch (selectedFilter) {
            case "Name":
                filteredProductList=productDAO.getAllProductsByName(filterText);
                break;
            case "Brand":
                filteredProductList=productDAO.getAllProductsByBrand(filterText);
                break;
            case "Quantity":
                filteredProductList=productDAO.getAllProductsByQuantity(Integer.parseInt(filterText));
                break;
            case "Size":
                filteredProductList=productDAO.getAllProductsBySize(filterText);
                break;
            default:
                return filteredProductList;
        }
        return filteredProductList;
    }

    private List<Sale> filterSaleData(String selectedFilter, String filterText) throws SQLException {
        List<Sale> filteredSale = new ArrayList<Sale>();
        switch (selectedFilter) {
            case "Model.Product Name":
                filteredSale=saleDao.getAllSalesByProductName(filterText);
                break;
            case "Quantity Sold":
                filteredSale=saleDao.getAllSalesByQuantity(Integer.parseInt(filterText));
                break;
            case "Price":
                filteredSale=saleDao.getAllSalesByPrice(Double.parseDouble(filterText));
                break;
            case "Date":
                filteredSale=saleDao.getAllSalesByDate(filterText);
                break;
            default:
                return filteredSale;
        }
        return filteredSale;
    }

    private JPanel createActionPanel(DefaultTableModel tableModel, JTable table, List<?> data, String type) {
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(50, 50, 50));
        JButton btnAdd = new JButton("Add " + type);
        JButton btnEdit = new JButton("Edit " + type);
        JButton btnDelete = new JButton("Delete " + type);

        styleButton(btnAdd, new Color(70, 130, 180), 14);
        styleButton(btnEdit, new Color(255, 215, 0), 14);
        styleButton(btnDelete, new Color(220, 20, 60), 14);

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        btnAdd.addActionListener(e -> {
            if (type.equals("Model.Product"))
            {

                JTextField nameField = new JTextField();
                JComboBox<String> brandField = new JComboBox<>(new String[]{"Select", "Nike", "Adidas", "New Balance", "Puma", "Vans"});
                JTextField quantityField = new JTextField();
                JTextField sizeField = new JTextField();

                JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));


                inputPanel.add(new JLabel("Name:"));
                inputPanel.add(nameField);
                inputPanel.add(new JLabel("Brand:"));
                inputPanel.add(brandField);
                inputPanel.add(new JLabel("Quantity:"));
                inputPanel.add(quantityField);
                inputPanel.add(new JLabel("Size:"));
                inputPanel.add(sizeField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Model.Product", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {

                        String name = nameField.getText().trim();
                        String brand = (String) brandField.getSelectedItem();
                        int quantity = Integer.parseInt(quantityField.getText().trim());
                        String size = sizeField.getText().trim();

                        if (name.isEmpty() || brand.equals("Select") || size.isEmpty()) {
                            throw new IllegalArgumentException("All fields must be filled.");
                        }

                        Product product = new Product(name, brand, quantity, size);
                        productDAO.insertProduct(product);  //veritabanına kaydetme
                        updateProductTable(tableModel);   // product listesini güncelleme
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
                    }
                }
            } else {
                JTextField idField = new JTextField();
                JComboBox<String> productField = new JComboBox<>();
                List<String> productNames = null;
                try {
                    productNames = productDAO.getAllProductNames();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


                for (String name : productNames) {
                    productField.addItem(name);
                }
                JTextField quantitySoldField = new JTextField();
                JTextField priceField = new JTextField();
                JTextField dateField = new JTextField();

                JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

                inputPanel.add(new JLabel("Model.Product:"));
                inputPanel.add(productField);
                inputPanel.add(new JLabel("Quantity Sold:"));
                inputPanel.add(quantitySoldField);
                inputPanel.add(new JLabel("Price:"));
                inputPanel.add(priceField);
                inputPanel.add(new JLabel("Date (dd.mm.yyyy):"));
                inputPanel.add(dateField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Model.Sale", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String productName = (String) productField.getSelectedItem();
                        int quantitySold = Integer.parseInt(quantitySoldField.getText().trim());
                        double price = Double.parseDouble(priceField.getText().trim());
                        String date = dateField.getText().trim();

                        if (!date.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                            throw new IllegalArgumentException("Date must be in format dd.mm.yyyy.");
                        }

                        Sale sale = new Sale( productName, quantitySold, price, date);
                        saleDao.insertSale(sale);
                        updateSaleTable(tableModel);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
                    }
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                if (type.equals("Model.Product")) {

                    int modelRowIndex = table.convertRowIndexToModel(selectedRow);
                    int id = (Integer) tableModel.getValueAt(modelRowIndex, 0);          // ID sütunu
                    String name = (String) tableModel.getValueAt(modelRowIndex, 1);      // Name sütunu
                    String brand = (String) tableModel.getValueAt(modelRowIndex, 2);     // Brand sütunu
                    int quantity = (Integer) tableModel.getValueAt(modelRowIndex, 3);    // Quantity sütunu
                    String size = (String) tableModel.getValueAt(modelRowIndex, 4);        // Size sütunu

                    JTextField idField = new JTextField(String.valueOf(id));
                    idField.setEnabled(false);
                    JTextField nameField = new JTextField(name);
                    JComboBox<String> brandField = new JComboBox<>(new String[]{"Select", "Nike", "Adidas", "New Balance", "Puma", "Vans"});
                    brandField.setSelectedItem(brand);
                    JTextField quantityField = new JTextField(String.valueOf(quantity));
                    JTextField sizeField = new JTextField(size);

                    JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
                    inputPanel.add(new JLabel("Model.Product ID:"));
                    inputPanel.add(idField);
                    inputPanel.add(new JLabel("Name:"));
                    inputPanel.add(nameField);
                    inputPanel.add(new JLabel("Brand:"));
                    inputPanel.add(brandField);
                    inputPanel.add(new JLabel("Quantity:"));
                    inputPanel.add(quantityField);
                    inputPanel.add(new JLabel("Size:"));
                    inputPanel.add(sizeField);

                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Edit Model.Product", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            Product updatedProduct=new Product();
                            updatedProduct.setId(Integer.parseInt(idField.getText().trim()));
                            updatedProduct.setName(nameField.getText().trim());
                            updatedProduct.setBrand((String) brandField.getSelectedItem());
                            updatedProduct.setQuantity(Integer.parseInt(quantityField.getText().trim()));
                            updatedProduct.setSize(sizeField.getText().trim());

                           productDAO.updateProduct(updatedProduct);
                           updateProductTable(tableModel);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
                        }
                    }
                } else {

                    int modelRowIndex = table.convertRowIndexToModel(selectedRow);
                    Integer id = (Integer) tableModel.getValueAt(modelRowIndex, 0);
                    String name = (String) tableModel.getValueAt(modelRowIndex, 1);
                    Integer quantitySold = (Integer) tableModel.getValueAt(modelRowIndex, 2);
                    Double price = (Double) tableModel.getValueAt(modelRowIndex, 3);
                    String date = (String) tableModel.getValueAt(modelRowIndex, 4);


                    JTextField idField= new JTextField(String.valueOf(id));
                    idField.setEnabled(false);
                    List<String> productNames = null;
                    try {
                        productNames = productDAO.getAllProductNames();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    JComboBox<String> productField = new JComboBox<>();
                    for (String prname : productNames) {
                        productField.addItem(prname);
                    }

                    productField.setSelectedItem(name);
                    JTextField quantitySoldField = new JTextField(String.valueOf(quantitySold));
                    JTextField priceField = new JTextField(String.valueOf(price));
                    JTextField dateField = new JTextField(date);

                    JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
                    inputPanel.add(new JLabel("Model.Sale ID:"));
                    inputPanel.add(idField);
                    inputPanel.add(new JLabel("Model.Product:"));
                    inputPanel.add(productField);
                    inputPanel.add(new JLabel("Quantity Sold:"));
                    inputPanel.add(quantitySoldField);
                    inputPanel.add(new JLabel("Price:"));
                    inputPanel.add(priceField);
                    inputPanel.add(new JLabel("Date (dd.mm.yyyy):"));
                    inputPanel.add(dateField);

                    int result = JOptionPane.showConfirmDialog(null, inputPanel, "Edit Model.Sale", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            Sale updatedSale=new Sale();
                            updatedSale.setId(Integer.parseInt(idField.getText().trim()));
                            updatedSale.setProductName((String) productField.getSelectedItem());
                            updatedSale.setQuantitySold(Integer.parseInt(quantitySoldField.getText().trim()));
                            updatedSale.setPrice(Double.parseDouble(priceField.getText().trim()));
                            String dateupdate = dateField.getText().trim();
                            if (!date.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                                throw new IllegalArgumentException("Date must be in format dd.mm.yyyy.");
                            }
                            updatedSale.setDate(dateupdate);

                            saleDao.updateSale(updatedSale);
                            updateSaleTable(tableModel);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to edit.");
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                if (type.equals("Model.Product")) {

                    int modelRowIndex = table.convertRowIndexToModel(selectedRow);
                    int idColumnIndex = 0;
                    Object idValue = table.getModel().getValueAt(modelRowIndex, idColumnIndex);
                    int id = (Integer) idValue;  // hangi id seçildi

                    try {
                        productDAO.deleteProductById(id);  // veritabanından sil
                        updateProductTable(tableModel);    // product listesini güncelle
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                } else {

                    int modelRowIndex = table.convertRowIndexToModel(selectedRow);
                    int idColumnIndex = 0;
                    Object idValue = table.getModel().getValueAt(modelRowIndex, idColumnIndex);
                    int id = (Integer) idValue; // hangi id seçildi
                    try {
                        saleDao.deleteSale(id);
                        updateSaleTable(tableModel);  //tekrar güncelle
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            }
        });

        return actionPanel;
    }

    private void switchPanel(JPanel contentPanel, String panelName) {
        CardLayout layout = (CardLayout) contentPanel.getLayout();
        layout.show(contentPanel, panelName);
    }

    private void styleButton(JButton button, Color bgColor, int fontSize) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
    }

    private void centerTableContent(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void updateProductTable(DefaultTableModel tableModel) throws SQLException {

        tableModel.setRowCount(0); // table modelin için boşaltma
        List<Product> productList = productDAO.getAllProducts();  // tüm ürünleri çekme
        for (Product productItem : productList) {
            Object[] rowData = {
                    productItem.getId(),
                    productItem.getName(),
                    productItem.getBrand(),
                    productItem.getQuantity(),
                    productItem.getSize()
            };
            tableModel.addRow(rowData);
        }
    }

    private void updateSaleTable(DefaultTableModel tableModel) throws SQLException {

        tableModel.setRowCount(0); // table modelin için boşaltma
        List<Sale> saleList = saleDao.getAllSales();  // tüm satışlar çekme
        for (Sale saleItem : saleList) {
            Object[] rowData = {
                    saleItem.getId(),
                    saleItem.getProductName(),
                    saleItem.getQuantitySold(),
                    saleItem.getPrice(),
                    saleItem.getDate()
            };
            tableModel.addRow(rowData);
        }
    }

    private void updateProductTableByFilteredData(DefaultTableModel tableModel, List<Product> productList)  {

        tableModel.setRowCount(0); // table modelin için boşaltma
        for (Product productItem : productList) {
            Object[] rowData = {
                    productItem.getId(),
                    productItem.getName(),
                    productItem.getBrand(),
                    productItem.getQuantity(),
                    productItem.getSize()
            };
            tableModel.addRow(rowData);
        }
    }
}

