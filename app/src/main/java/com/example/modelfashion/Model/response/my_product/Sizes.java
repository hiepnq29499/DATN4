package com.example.modelfashion.Model.response.my_product;

public class Sizes {
    private String size_id;
    private String size;
    private String remain_product;
    private String total_product;

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getRemain_product() {
        return remain_product;
    }

    public void setRemain_product(String remain_product) {
        this.remain_product = remain_product;
    }

    public String getTotal_product() {
        return total_product;
    }

    public void setTotal_product(String total_product) {
        this.total_product = total_product;
    }

    @Override
    public String toString() {
        return "Sizes{" +
                "size_id='" + size_id + '\'' +
                "size='" + size + '\'' +
                ", remain_product='" + remain_product + '\''+
                ", total_product='" + total_product + '\''+
                '}';
    }
}
