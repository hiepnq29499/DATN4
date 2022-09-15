package com.example.modelfashion.Interface;

import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.Model.DeliveryInfo;
import com.example.modelfashion.Model.MHistory.BillModel;
import com.example.modelfashion.Model.Product;
import com.example.modelfashion.Model.response.Rating;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.Model.response.bill.Bill;
import com.example.modelfashion.Model.response.bill.BillDetail;
import com.example.modelfashion.Model.response.my_product.CartProduct;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.Model.response.my_product.Sizes;
import com.example.modelfashion.Utility.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiRetrofit {
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build();
    Gson gson = new GsonBuilder().setLenient().create();
    ApiRetrofit apiRetrofit = new Retrofit.Builder().baseUrl("http://" + Constants.KEY_IP + "/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiRetrofit.class);

    @Multipart
    @POST("upload_avatar.php")
    Call<String> uploadAvatar(@Part MultipartBody.Part avatar);

    @FormUrlEncoded
    @POST("insert_user.php")
    Call<String> InsertUser(@Field("taikhoan") String taikhoan, @Field("matkhau") String matkhau);

    @FormUrlEncoded
    @POST("get_products_size.php")
    Call<ArrayList<Sizes>> GetProductsSize(@Field("product_name") String product_name);

    @FormUrlEncoded
    @POST("check_quantity_left.php")
    Call<String> CheckSizeLeft(@Field("size_id") String size_id, @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("insert_bill_buy_now.php")
    Call<String> InsertBillBuyNow(@Field("user_id") String user_id, @Field("date") String date,
                                  @Field("price") String price, @Field("size_id") String size_id);

    @FormUrlEncoded
    @POST("insert_cart.php")
    Call<String> InsertCart(@Field("user_id") String user_id, @Field("size_id") String size_id,
                            @Field("product_name") String product_name, @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("insert_cart.php")
    Call<String> InsertCart2(@Field("user_id") String user_id, @Field("size_id") String size_id,
                             @Field("product_id") String product_id, @Field("quantity") String quantity);

    @GET("get_user_cart.php")
    Call<ArrayList<CartProduct>> GetCartProduct(@Query("user_id") String user_id);

    @GET("get_product_in_cart.php")
    Call<ArrayList<MyProduct>> GetProductInCart(@Query("user_id") String user_id);

    @GET("get_cart_info.php")
    Call<ArrayList<CartProduct>> GetCart(@Query("user_id") String user_id);

    @GET("get_product_by_name.php")
    Call<ArrayList<MyProduct>> GetProductByName(@Query("product_name") JSONArray product_name, @Query("user_id") String user_id);

    @GET("get_sizes_by_id.php")
    Call<ArrayList<Sizes>> GetSizeById(@Query("sizes_id") JSONArray sizes_id, @Query("user_id") String user_id);

    @GET("get_amount_cart.php")
    Call<String> GetAmountCart(@Query("product_name") JSONArray product_name, @Query("user_id") String user_id);

    @POST("insert_user.php")
    Call<User> editUser(@Body User user);

    @FormUrlEncoded
    @POST("insert_payment.php")
    Call<String> InsertPayment(@Field("user_id") String user_id, @Field("amount") String amount,
                               @Field("date_created") String date_created, @Field("arr_sizes") String arr_sizes);

    @GET("get_all_product_by_detail_id.php")
    Call<ArrayList<MyProduct>> GetProductByDetailId(@Query("detail_id") JSONArray detail_id);

    @GET("get_user_by_id.php")
    Call<User> GetUserById(@Query("user_id") String user_id);

    @GET("get_bill_detail_in_bill.php")
    Call<ArrayList<BillDetail>> GetBillDetailInBill(@Query("bill_id") String bill_id);

    @GET("get_all_products.php")
    Call<ArrayList<MyProduct>> GetAllProduct();

    @GET("get_recent_clothes_type.php")
    Call<ArrayList<String>> GetRecentType();

    @GET("get_6_random_brand.php")
    Call<ArrayList<Brand>> Get6RdBrand();

    @GET("get_product_by_id.php")
    Call<MyProduct> GetProductById(@Query("product_id") String product_id);

    @GET("get_product_by_type.php")
    Call<ArrayList<MyProduct>> GetProductByType(@Query("product_type") String product_type);

    @GET("get_delivery_info.php")
    Call<DeliveryInfo> GetDeliveryInfo(@Query("user_id") String user_id);

    @GET("get_bill_by_user_id.php")
    Call<ArrayList<Bill>> GetBillByUserId(@Query("user_id") String user_id);

    @GET("get_rating_by_product.php")
    Call<ArrayList<Rating>> GetProductRating(@Query("product_id") String product_id);

    @GET("get_bill_by_user_id.php")
    Call<ArrayList<Bill>> GetBillFilted(@Query("user_id") String user_id, @Query("date_created") String date_created, @Query("status") String status,
                                        @Query("order") String order);

    @GET("get_bill_by_user_id.php")
    Call<ArrayList<Bill>> GetBillByUserId(@Query("user_id") String user_id, @Query("date_created") String date_created, @Query("status") String status);


    @GET("get_all_brand.php")
    Call<ArrayList<Brand>> GetAllBrand();

    @GET("get_brands_location.php")
    Call<ArrayList<String>> GetBrandsLocation();

    @GET("get_brands_by_location.php")
    Call<ArrayList<Brand>> GetBrandByLocation(@Query("locations") JSONArray arr_location);

    @GET("get_brand_by_id.php")
    Call<Brand> GetBrandById(@Query("brand_id") String brand_id);

    @GET("get_brands_product.php")
    Call<ArrayList<MyProduct>> GetBrandsProduct(@Query("brand_id") String brand_id);

    @GET("get_brands_product_by_filter1.php")
    Call<ArrayList<MyProduct>> GetFilterBrandsProduct(@Query("brand_id") String brand_id, @Query("type") String type,
                                                      @Query("value") String value);

    @GET("get_fav_product_by_user.php")
    Call<ArrayList<MyProduct>> GetFavProduct(@Query("user_id") String user_id);

    @GET("get_fav_product_by_filter.php")
    Call<ArrayList<MyProduct>> GetFilterFavProduct(@Query("user_id") String user_id, @Query("type") String type,
                                                   @Query("value") String value);

    @GET("get_product_by_filter1.php")
    Call<ArrayList<MyProduct>> GetFilterProduct(@Query("brand_id") String brand_id, @Query("type") String type,
                                                @Query("value") String value, @Query("price_range") String price_range);

    @GET("search_product_by_name.php")
    Call<ArrayList<MyProduct>> GetSearchProduct(@Query("search_text") String search_text);


    @FormUrlEncoded
    @POST("delete_product_from_cart_by_size_id.php")
    Call<String> DeleteProductFromCart(@Field("user_id") String user_id, @Field("size_id") String size_id);

    @FormUrlEncoded
    @POST("update_avatar_user.php")
    Call<String> UpdateAvatar(@Field("user_id") String user_id, @Field("new_avatar") String new_avatar, @Field("old_avatar") String old_avatar);

    @FormUrlEncoded
    @POST("change_cart_quantity.php")
    Call<String> ChangeCartQuantity(@Field("user_id") String user_id, @Field("quantity") String quantity, @Field("size_id") String size_id);

    @FormUrlEncoded
    @POST("Web/insert_fcm_token.php")
    Call<String> InsertFcmToken(@Field("user_id") String user_id, @Field("token") String token);

    @FormUrlEncoded
    @POST("Web/delete_fcm_token.php")
    Call<String> DeleteFcmToken(@Field("user_id") String user_id, @Field("token") String token);

    @FormUrlEncoded
    @POST("send_mail_confirm.php")
    Call<String> SendConfirmEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("get_confirm_mail_code.php")
    Call<String> GetConfirmMail(@Field("email") String email, @Field("code") String code);

    @FormUrlEncoded
    @POST("login_with_google.php")
    Call<User> LoginWithGoogle(@Field("email") String email);

    @FormUrlEncoded
    @POST("save_delivery_info.php")
    Call<String> SaveDeliveryInfo(@Field("user_id") String user_id, @Field("receiver_name") String receiver_name,
                                  @Field("street_address") String street_address, @Field("city") String city, @Field("contact") String contact);

    @FormUrlEncoded
    @POST("insert_bill.php")
    Call<String> InsertBill2(@Field("user_id") String user_id, @Field("receiver_name") String receiver_name, @Field("street_address") String street,
                             @Field("city") String city, @Field("contact") String contact, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("insert_bill_detail.php")
    Call<String> InsertBillDetail2(@Field("size_id") String size_id, @Field("bill_id") String bill_id, @Field("product_id") String product_id,
                                   @Field("quantity") String quantity, @Field("cart_id") String cart_id, @Field("discount_rate") String discount_rate);

    @FormUrlEncoded
    @POST("remove_cart_item.php")
    Call<String> RemoveCartItem2(@Field("cart_id") String cart_id);

    @FormUrlEncoded
    @POST("update_rating.php")
    Call<String> UpdateRating(@Field("user_id") String user_id, @Field("product_id") String product_id, @Field("score") String score,
                              @Field("comment") String comment);

    @FormUrlEncoded
    @POST("insert_product_to_favorite.php")
    Call<String> InsertFavorite(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST("send_request_get_pw.php")
    Call<String> SendRequestGetPw(@Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("update_new_password.php")
    Call<String> UpdateNewPw(@Field("confirm_code") String confirm_code, @Field("new_pw") String new_pw, @Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("update_cancel_bill.php")
    Call<String> UpdateCancelBill(@Field("bill_id") String bill_id);

    @FormUrlEncoded
    @POST("change_user_full_name.php")
    Call<User> ChangeFullname(@Field("full_name") String full_name, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("change_user_phone.php")
    Call<User> ChangePhone(@Field("phone") String phone, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("change_user_password.php")
    Call<String> ChangeUserPassword(@Field("password") String password, @Field("new_password") String new_password, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("remove_user_favorite.php")
    Call<String> RemoveUserFavorite(@Field("user_id") String user_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST("insert_user.php")
    Call<String> InsertUser(@Field("taikhoan") String taikhoan, @Field("matkhau") String matkhau, @Field("email") String email, @Field("account_type") String account_type);

    @FormUrlEncoded
    @POST("change_quantity_in_cart.php")
    Call<String> changeQuantityInCart(@Field("user_id") String user_id, @Field("id") String id, @Field("quantity") String quantity);

}
