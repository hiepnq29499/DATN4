package com.example.modelfashion.Model;

public class DeliveryInfo {
    private String delivery_id;
    private String user_id;
    private String receiver_name;
    private String street_address;
    private String city;
    private String contact;

    public DeliveryInfo() {
    }

    public DeliveryInfo(String delivery_id, String user_id, String receiver_name, String street_address, String city, String contact) {
        this.delivery_id = delivery_id;
        this.user_id = user_id;
        this.receiver_name = receiver_name;
        this.street_address = street_address;
        this.city = city;
        this.contact = contact;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
