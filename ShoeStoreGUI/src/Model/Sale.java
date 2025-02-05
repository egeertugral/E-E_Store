package Model;

public class Sale {
    private Integer id;
    private String productName;
    private int quantitySold;
    private double price;
    private String date;

    public Sale(String productName, int quantitySold, double price, String date) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.price = price;
        this.date = date;
    }

    public Sale(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
