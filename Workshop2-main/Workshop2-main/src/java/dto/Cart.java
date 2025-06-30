/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dto;

import java.time.LocalDate;
import java.util.List;


public class Cart {
    private int cartID;
    private String userID;
    private LocalDate createdDate;
    private List<CartDetail> cartDetails;

    public Cart() {
    }
    
    public Cart(int cartID, String userID, LocalDate createdDate, List<CartDetail> cartDetails) {
        this.cartID = cartID;
        this.userID = userID;
        this.createdDate = createdDate;
        this.cartDetails = cartDetails;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    
}
