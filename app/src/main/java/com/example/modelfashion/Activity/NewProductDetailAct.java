package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Adapter.UserCommentAdapter;
import com.example.modelfashion.Adapter.ViewPagerDetailProductAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.Rating;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProductDetailAct extends AppCompatActivity {
    ImageView img_back, img_fav, img_cart;
    Spinner spn_size;
    ArrayList<String> arr_size = new ArrayList<>();
    MyProduct myProduct = new MyProduct();
    ArrayList<String> arrItem = new ArrayList<>();
    ViewPager2 vp_product_img;
    CircleIndicator3 ci_product_img;
    TextView tv_product_name, tv_price, tv_discount, tv_status, tv_descript, tv_add_to_cart, tv_material, tv_brand, tv_type, tv_location,
    tv_rating;
    RecyclerView rv_comment;
    String size_selected = "S";
    String user_id = "";
    String product_id = "";
    ArrayList<String> arr_size_id = new ArrayList<>();
    String size_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_detail);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        product_id = intent.getStringExtra(Constants.KEY_PRODUCT_ID);
        Log.e("test4",product_id+"");
        InitView();
        InitData();
    }
    private void InitView(){
        tv_rating = findViewById(R.id.tv_rating_detail_product);
        rv_comment = findViewById(R.id.rv_comment);
        img_back = findViewById(R.id.img_back_detail);
        img_fav = findViewById(R.id.img_fav_detail_fm);
        vp_product_img = findViewById(R.id.vp_product_detail);
        ci_product_img = findViewById(R.id.ci_product_detail);
        tv_product_name = findViewById(R.id.tv_product_name_detail);
        tv_price = findViewById(R.id.tv_price_detail);
        tv_discount = findViewById(R.id.tv_discount_detail);
        tv_status = findViewById(R.id.tv_status_detail);
        tv_descript = findViewById(R.id.tv_descript_detail);
        tv_add_to_cart = findViewById(R.id.tv_add_to_cart);
        tv_material = findViewById(R.id.tv_material);
        tv_brand = findViewById(R.id.tv_brand_detail);
        tv_type = findViewById(R.id.tv_type_detail);
        tv_location = findViewById(R.id.tv_location_detail);
        spn_size = findViewById(R.id.spn_size_detail);
        tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equalsIgnoreCase("null")){
                    Toast.makeText(NewProductDetailAct.this, "Bạn chưa thực hiện đăng nhập", Toast.LENGTH_SHORT).show();
                }else {
                    ApiRetrofit.apiRetrofit.CheckSizeLeft(size_id,"1").enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.body().equals("ok")){
                                ApiRetrofit.apiRetrofit.InsertCart2(user_id,size_id,product_id,"1").enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.body().equals("duplicated")) {
                                            Toast.makeText(NewProductDetailAct.this, "Sản phẩm đã nằm trong giỏ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (response.body().equals("ok")) {
                                                Toast.makeText(NewProductDetailAct.this, "Thêm vào giỏ thành công", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(NewProductDetailAct.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                                Log.e("err", response.body());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            }else if(response.body().equals("fail")){
                                Toast.makeText(NewProductDetailAct.this, "Size này đã hết hàng", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(NewProductDetailAct.this, "Lỗi db", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    private void InitData(){
        ApiRetrofit.apiRetrofit.GetProductById(product_id).enqueue(new Callback<MyProduct>() {
            @Override
            public void onResponse(Call<MyProduct> call, Response<MyProduct> response) {
                myProduct = response.body();
                Log.e("test4",myProduct.toString());
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                if(Float.parseFloat(myProduct.getDiscount_rate())>0){
                    tv_discount.setText("Sale "+Float.parseFloat(myProduct.getDiscount_rate())+"%");
                    String money_format = formatter.format(Integer.parseInt(myProduct.getPrice().split("\\.")[0])*(100-Float.parseFloat(myProduct.getDiscount_rate()))/100);
                    tv_price.setText(money_format+" VNĐ");
                }else {
                    String money_format = formatter.format(Integer.parseInt(myProduct.getPrice().split("\\.")[0]));
                    tv_price.setText(money_format+" VNĐ");
                }
                arrItem.add(myProduct.getPhotos().get(0).replace("localhost",Constants.KEY_IP));
                arrItem.add(myProduct.getPhotos().get(1).replace("localhost",Constants.KEY_IP));
                arrItem.add(myProduct.getPhotos().get(2).replace("localhost",Constants.KEY_IP));
                ViewPagerDetailProductAdapter viewPagerDetailProductAdapter = new ViewPagerDetailProductAdapter();
                vp_product_img.setAdapter(viewPagerDetailProductAdapter);
                viewPagerDetailProductAdapter.setArrItem(arrItem);
                ci_product_img.setViewPager(vp_product_img);
                tv_product_name.setText(myProduct.getProduct_name());
                tv_status.setText(myProduct.getStatus());
                tv_descript.setText(myProduct.getDescription());
                tv_material.setText(myProduct.getMaterial());
                tv_brand.setText(myProduct.getBrand());
                tv_type.setText(myProduct.getType());
                tv_location.setText(myProduct.getLocation());
                arr_size_id.add(myProduct.getSizes().get(0).getSize_id());
                arr_size_id.add(myProduct.getSizes().get(1).getSize_id());
                arr_size_id.add(myProduct.getSizes().get(2).getSize_id());
                arr_size_id.add(myProduct.getSizes().get(3).getSize_id());
                size_id = myProduct.getSizes().get(0).getSize_id();
                arr_size.add("S");
                arr_size.add("M");
                arr_size.add("L");
                arr_size.add("XL");
                tv_rating.setText(myProduct.getRating());
                ArrayAdapter spnAdapter = new ArrayAdapter(NewProductDetailAct.this, R.layout.support_simple_spinner_dropdown_item,arr_size);
                spn_size.setAdapter(spnAdapter);
                ApiRetrofit.apiRetrofit.GetProductRating(product_id).enqueue(new Callback<ArrayList<Rating>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Rating>> call, Response<ArrayList<Rating>> response) {
                        UserCommentAdapter userCommentAdapter = new UserCommentAdapter(NewProductDetailAct.this, response.body());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewProductDetailAct.this, LinearLayoutManager.VERTICAL, false);
                        rv_comment.setLayoutManager(linearLayoutManager);
                        rv_comment.setAdapter(userCommentAdapter);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Rating>> call, Throwable t) {

                    }
                });
                spn_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        size_selected = arr_size.get(i);
                        size_id = arr_size_id.get(i);
                        Log.e("size_id", size_id+" "+arr_size_id);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        size_selected = "S";
                    }
                });
            }

            @Override
            public void onFailure(Call<MyProduct> call, Throwable t) {
                Log.e("test4",t.toString());
            }
        });
    }
}