package com.example.modelfashion.Model.response.my_product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CartProduct {
    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("size_id")
    @Expose
    private String sizeId;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
