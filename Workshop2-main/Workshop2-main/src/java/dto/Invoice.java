/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dto;

import java.time.LocalDate;


public class Invoice {
    
    private String userID;
    private String status;
    private float totalAmount;
    private LocalDate createdDate;

    public Invoice(String userID, String status, float totalAmount, LocalDate createdDate) {
        this.userID = userID;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdDate = createdDate;
    }

    public Invoice() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
}
