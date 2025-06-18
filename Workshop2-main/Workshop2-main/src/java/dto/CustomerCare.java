/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dto;


public class CustomerCare {
    private int ticketID;
    private String userID, subject, content, status, rely;

    public CustomerCare(int ticketID, String userID, String subject, String content, String status, String rely) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.rely = rely;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRely() {
        return rely;
    }

    public void setRely(String rely) {
        this.rely = rely;
    }
    
    
}
