package com.example.modelfashion.Model.response.bill;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Parcelable {
    @SerializedName("bill_id")
    @Expose
    private String billId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("date_shipped")
    @Expose
    private String dateShipped;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("street_address")
    @Expose
    private String streetAddress;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("bill_detail")
    @Expose
    private ArrayList<BillDetail> billDetail = null;

    protected Bill(Parcel in) {
        billId = in.readString();
        userId = in.readString();
        dateCreated = in.readString();
        dateShipped = in.readString();
        receiverName = in.readString();
        streetAddress = in.readString();
        city = in.readString();
        contact = in.readString();
        status = in.readString();
        amount = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(String dateShipped) {
        this.dateShipped = dateShipped;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ArrayList<BillDetail> getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(ArrayList<BillDetail> billDetail) {
        this.billDetail = billDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(billId);
        parcel.writeString(userId);
        parcel.writeString(dateCreated);
        parcel.writeString(dateShipped);
        parcel.writeString(receiverName);
        parcel.writeString(streetAddress);
        parcel.writeString(city);
        parcel.writeString(contact);
        parcel.writeString(status);
        parcel.writeString(amount);
    }
}
