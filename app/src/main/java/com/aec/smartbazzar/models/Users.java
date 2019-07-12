package com.aec.smartbazzar.models;

public class Users {

    private String u_id;
    private String name;
    private String address;
    private String ph_number;
    private String fcm;

    public Users() {
        this.u_id = "";
        this.name = "";
        this.address = "";
        this.ph_number = "";
        this.fcm = "";
    }

    public Users(String u_id, String name, String address, String ph_number, String fcm) {
        this.u_id = u_id;
        this.name = name;
        this.address = address;
        this.ph_number = ph_number;
        this.fcm = fcm;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    @Override
    public String toString() {
        return "Users{" +
                "u_id='" + u_id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ph_number='" + ph_number + '\'' +
                ", fcm='" + fcm + '\'' +
                '}';
    }
}
