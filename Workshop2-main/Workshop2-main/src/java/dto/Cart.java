package dto;

/**
 * Cart DTO - Represents a cart item for display in JSP
 */
public class Cart {

    private String userID;
    private String createdDate;

    public Cart() {
    }

    public Cart(String userID, String createdDate) {
        this.userID = userID;
        this.createdDate = createdDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    

}
