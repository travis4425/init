package dto;

public class Inventory {
    private int warehouseID;
    private int productID;
    private int stockQuantity;
    public Inventory() {
    }

    public Inventory(int warehouseID, int productID, int stockQuantity) {
        this.warehouseID = warehouseID;
        this.productID = productID;
        this.stockQuantity = stockQuantity;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

}