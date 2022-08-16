package com.example.modelfashion.Model.response.bill;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class BillDetail implements Parcelable {
    @SerializedName("detail_id")
    @Expose
    private String detailId;
    @SerializedName("product_size_id")
    @Expose
    private String productSizeId;
    @SerializedName("bill_id")
    @Expose
    private String billId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("discount_rate")
    @Expose
    private String discountRate;
    @SerializedName("price")
    @Expose
    private String price;

    protected BillDetail(Parcel in) {
        detailId = in.readString();
        productSizeId = in.readString();
        billId = in.readString();
        productId = in.readString();
        quantity = in.readString();
        productName = in.readString();
        size = in.readString();
        discountRate = in.readString();
        price = in.readString();
    }

    public static final Creator<BillDetail> CREATOR = new Creator<BillDetail>() {
        @Override
        public BillDetail createFromParcel(Parcel in) {
            return new BillDetail(in);
        }

        @Override
        public BillDetail[] newArray(int size) {
            return new BillDetail[size];
        }
    };

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(String productSizeId) {
        this.productSizeId = productSizeId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(detailId);
        parcel.writeString(productSizeId);
        parcel.writeString(billId);
        parcel.writeString(productId);
        parcel.writeString(quantity);
        parcel.writeString(productName);
        parcel.writeString(size);
        parcel.writeString(discountRate);
        parcel.writeString(price);
    }
}
