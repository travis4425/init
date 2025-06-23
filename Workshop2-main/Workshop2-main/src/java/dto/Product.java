/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dto;


public class Product {
    private int productID, categoryID, quantity;
    private float price;
    private String name, sellerID, status, cateName, sellerIDFullName;

    

    public Product(int productID, int categoryID, int quantity, float price, String name, String sellerID, String status) {
        this.productID = productID;
        this.categoryID = categoryID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.sellerID = sellerID;
        this.status = status;
    }

    public Product(int productID, int quantity, float price, String name, String sellerID, String status, String cateName, String sellerIDFullName) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.sellerID = sellerID;
        this.status = status;
        this.cateName = cateName;
        this.sellerIDFullName = sellerIDFullName;
    }
    
    
    
    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public int getProductID() {
        return productID;
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

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSellerFullName() {
        return sellerIDFullName;
    }

    public void setSellerFullName(String sellerIDFullName) {
        this.sellerIDFullName = sellerIDFullName;
    }
}