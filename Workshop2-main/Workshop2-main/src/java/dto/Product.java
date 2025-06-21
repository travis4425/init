package dto;

import java.util.Objects;

public class Product {
    private int productID, categoryID, quantity;
    private float price;
    private String name, seller, status;
    
    // Constructor chính
    public Product(int productID, int categoryID, int quantity, float price, String name, String seller, String status) {
        this.productID = productID;
        this.categoryID = categoryID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.seller = seller;
        this.status = status;
    }
    
    // Constructor cho CartDetailDAO.getCartWithProducts() - FIX LỖI
    public Product(String productID, String productName, float price, int stockQuantity, boolean status) {
        this.productID = Integer.parseInt(productID);
        this.categoryID = 0; // Default
        this.quantity = stockQuantity;
        this.price = price;
        this.name = productName;
        this.seller = ""; // Default
        this.status = status ? "active" : "inactive";
    }
    
    // Getters and Setters
    public int getProductID() {
        return productID;
    }
    
    public String getProductIDAsString() {
        return String.valueOf(productID);
    }
    
    public void setProductID(int productID) {
        this.productID = productID;
    }
    
    public int getCategoryID() {
        return categoryID;
    }
    
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSeller() {
        return seller;
    }
    
    public void setSeller(String seller) {
        this.seller = seller;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productID == product.productID;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productID);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", categoryID=" + categoryID +
                ", quantity=" + quantity +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", seller='" + seller + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}