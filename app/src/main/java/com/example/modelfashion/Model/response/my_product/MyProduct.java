package com.example.modelfashion.Model.response.my_product;

import java.util.ArrayList;

public class MyProduct {
    private String id;
    private String type;
    private String brand;
    private String product_name;
    private String description;
    private String price;
    private String cost;
    private String date_added;
    private String rating;
    private String discount_rate;
    private String location;
    private String material;
    private String status;
    private ArrayList<Sizes> sizes;
    private ArrayList<String> photos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(String discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Sizes> sizes) {
        this.sizes = sizes;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public MyProduct() {
    }

    public MyProduct(String id, String type, String brand, String product_name, String description, String price, String cost, String date_added, String rating, String discount_rate, String location, String material, String status, ArrayList<Sizes> sizes, ArrayList<String> photos) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.cost = cost;
        this.date_added = date_added;
        this.rating = rating;
        this.discount_rate = discount_rate;
        this.location = location;
        this.material = material;
        this.status = status;
        this.sizes = sizes;
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "MyProduct{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", product_name='" + product_name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", cost='" + cost + '\'' +
                ", date_added='" + date_added + '\'' +
                ", rating='" + rating + '\'' +
                ", discount_rate='" + discount_rate + '\'' +
                ", location='" + location + '\'' +
                ", material='" + material + '\'' +
                ", status='" + status + '\'' +
                ", sizes=" + sizes +
                ", photos=" + photos +
                '}';
    }
}

