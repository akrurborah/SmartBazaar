package com.aec.smartbazzar.models;

public class OrderItem {

    public String itemId;
    public String Quantity;

    public OrderItem() {
        this.itemId = null;
        Quantity = null;
    }

    public OrderItem(String itemId, String quantity) {
        this.itemId = itemId;
        Quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
