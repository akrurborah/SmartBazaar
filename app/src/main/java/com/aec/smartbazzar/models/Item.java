package com.aec.smartbazzar.models;

import java.io.Serializable;

public class Item  implements Serializable{

    private String i_id;
    private String name;
    private String price;
    private String img_url;
    private String cat_id;
    private String details;

    public Item(String i_id, String name, String price, String img_url, String cat_id, String details) {
        this.i_id = i_id;
        this.name = name;
        this.price = price;
        this.img_url = img_url;
        this.cat_id = cat_id;
        this.details = details;
    }

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Item{" +
                "i_id='" + i_id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", img_url='" + img_url + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
