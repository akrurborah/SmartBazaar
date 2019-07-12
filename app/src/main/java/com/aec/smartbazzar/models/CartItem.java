package com.aec.smartbazzar.models;

import java.util.ArrayList;

public class CartItem {

    private Item item;
    private String quantity;

    public CartItem(Item item, String quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public static String getTotalPrice(ArrayList<CartItem> list){
        float total = 0;
        for (CartItem cItem : list) {
            total += Float.parseFloat(cItem.item.getPrice()) * Integer.parseInt(cItem.quantity);
        }
        return String.valueOf(total);
    }
}