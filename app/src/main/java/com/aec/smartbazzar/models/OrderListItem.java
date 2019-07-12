package com.aec.smartbazzar.models;

public class OrderListItem {

    private String orderID;
    private String itemName;
    private String img;
    private String Qty;
    private String total;
    private String orderTime;
    private String status;

    public OrderListItem() {
        this.orderID = "";
        this.itemName = "";
        Qty = "";
        img = "";
        this.total = "";
        this.orderTime = "";
        this.status = "";
    }

    public OrderListItem(String orderID, String itemName, String qty, String total, String orderTime, String status, String img) {
        this.orderID = orderID;
        this.itemName = itemName;
        Qty = qty;
        this.total = total;
        this.orderTime = orderTime;
        this.status = status;
        this.img = img;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {

        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "OrderListItem{" +
                "orderID='" + orderID + '\'' +
                ", itemName='" + itemName + '\'' +
                ", Qty='" + Qty + '\'' +
                ", total='" + total + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
