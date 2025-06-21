package dto;

/**
 * CartItem DTO - Represents a cart item for display in JSP
 */
public class Cart {
    private String productID;
    private String productName;
    private float price;
    private int quantity;
    private float total;
    
    public Cart() {
    }
    
    public Cart(String productID, String productName, float price, int quantity, float total) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }
    
    // Getters and Setters
    public String getProductID() {
        return productID;
    }
    
    public void setProductID(String productID) {
        this.productID = productID;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public float getTotal() {
        return total;
    }
    
    public void setTotal(float total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "productID='" + productID + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }
}