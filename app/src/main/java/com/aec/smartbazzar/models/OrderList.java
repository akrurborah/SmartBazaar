package com.aec.smartbazzar.models;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderList implements Serializable{

    public String userId;
    public ArrayList<OrderItem> itemList;

    public OrderList() {
        this.userId = null;
        this.itemList = new ArrayList<>();
    }

    public OrderList(String userId, ArrayList<OrderItem> itemList) {
        this.userId = userId;
        this.itemList = itemList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<OrderItem> itemList) {
        this.itemList = itemList;
    }
}
