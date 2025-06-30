/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dto;

import java.sql.Date;

public class Promotion {
    private int promoID;
    private String name;
    private int discountPercent;
    private Date startDate;
    private Date endDate;
    private boolean status;

    public Promotion(int promoID, String name, int discountPercent, Date startDate, Date endDate, boolean status) {
        this.promoID = promoID;
        this.name = name;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getPromoID() {
        return promoID;
    }

    public String getName() {
        return name;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isStatus() {
        return status;
    }
}
