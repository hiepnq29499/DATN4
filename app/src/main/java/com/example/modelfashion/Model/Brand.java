package com.example.modelfashion.Model;

public class Brand {
    private String brand_id;
    private String name;
    private String logo;
    private String location;
    private String description;

    public Brand() {
    }

    public Brand(String brand_id, String name, String logo, String location, String description) {
        this.brand_id = brand_id;
        this.name = name;
        this.logo = logo;
        this.location = location;
        this.description = description;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
