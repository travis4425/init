/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dto;


public class Product {
    private int productID, categoryID, quantity, promoID;
    private float price, originalPrice;
    private String name, sellerID, status, cateName, sellerFullName;

    public Product() {}

    public Product(int productID, int categoryID, int quantity, float price, String name, String sellerID, String status, int promoID, float originalPrice) {
        this.productID = productID;
        this.categoryID = categoryID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.sellerID = sellerID;
        this.status = status;
        this.promoID = promoID;
        this.originalPrice = originalPrice;
    }

    public Product(int productID, int quantity, float price, String name, String sellerID, String status, String cateName, String sellerFullName, int promoID, float originalPrice) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.sellerID = sellerID;
        this.status = status;
        this.cateName = cateName;
        this.sellerFullName = sellerFullName;
        this.promoID = promoID;
        this.originalPrice = originalPrice;
    }
    
    // Constructor cho getProductById
    public Product(int productID, int quantity, float price, String name, String sellerID, String status, int promoID, float originalPrice) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.sellerID = sellerID;
        this.status = status;
        this.promoID = promoID;
        this.originalPrice = originalPrice;
    }

    public Product(int productID, String name, float price, String cateName) {
        this.productID = productID;
        this.price = price;
        this.name = name;
        this.cateName = cateName;
    }

    public Product(int productID, String name, float price) {
        this.productID = productID;
        this.price = price;
        this.name = name;
    }
    
    

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getPromoID() {
        return promoID;
    }

    public void setPromoID(int promoID) {
        this.promoID = promoID;
    }

    public String getSellerFullName() {
        return sellerFullName;
    }

    public void setSellerFullName(String sellerFullName) {
        this.sellerFullName = sellerFullName;
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
}
