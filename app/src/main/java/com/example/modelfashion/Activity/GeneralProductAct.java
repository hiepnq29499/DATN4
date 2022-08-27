package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Activity.SignIn.SignInActivity;
import com.example.modelfashion.Adapter.ProductAdapter;
import com.example.modelfashion.Adapter.ProductListAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralProductAct extends AppCompatActivity {
    RecyclerView rv_product;
    TextView tv_filter;
    ImageView imgFav, imgFilterPrice;
    Spinner spnFilterBrand, spnFilterPrice;
    LinearLayout llFilterPrice;
    ArrayList<MyProduct> arrProduct = new ArrayList<>();
    ProductAdapter productAdapter;
    String user_id = "";
    String product_type = "";
    String brand_id = "";
    String price_rance = "";
    ArrayList<String> arrSpnPrice = new ArrayList<>(Arrays.asList("Tất cả","Dưới 500.000 vnđ","500.000-1.000.000 vnđ","1.000.000-2.000.000 vnđ","Trên 2.000.000 vnđ"));
    ArrayList<String> arrPriceTier = new ArrayList<>(Arrays.asList("Tất cả","Tier 1","Tier 2","Tier 3","Tier 4"));
    ArrayList<String> arrBrand = new ArrayList<>();
    ArrayList<String> arrBrandId = new ArrayList<>();
    String value = "ASC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_product);
        rv_product = findViewById(R.id.rv_general_product);
        tv_filter = findViewById(R.id.tv_fill_price_general);
        imgFav = findViewById(R.id.img_fav_general);
        imgFilterPrice = findViewById(R.id.img_fill_price_general);
        spnFilterBrand = findViewById(R.id.spn_fill_brand_general);
        spnFilterPrice = findViewById(R.id.spn_fill_price_general);
        llFilterPrice = findViewById(R.id.ll_fill_price_general);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        product_type = intent.getStringExtra("type");
        SetData();
    }

    private void SetData(){
        ProgressDialog progressDialog = new ProgressDialog(GeneralProductAct.this);
        progressDialog.setMessage("Pending");
        progressDialog.show();
        brand_id = "Tất cả";
        price_rance = "Tất cả";
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equalsIgnoreCase("null")){
                    Toast.makeText(GeneralProductAct.this, "Bạn phải đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(GeneralProductAct.this, MyFavoriteAct.class);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }
            }
        });
        ApiRetrofit.apiRetrofit.GetAllBrand().enqueue(new Callback<ArrayList<Brand>>() {
            @Override
            public void onResponse(Call<ArrayList<Brand>> call, Response<ArrayList<Brand>> response) {
                for (int i =0; i<response.body().size();i++){
                    arrBrandId.add(response.body().get(i).getBrand_id());
                    arrBrand.add(response.body().get(i).getName());
                }
                ArrayAdapter arrBrandAdapter = new ArrayAdapter(GeneralProductAct.this,R.layout.support_simple_spinner_dropdown_item,arrBrand);
                spnFilterBrand.setAdapter(arrBrandAdapter);
                ArrayAdapter arrPriceAdapter = new ArrayAdapter(GeneralProductAct.this, R.layout.support_simple_spinner_dropdown_item, arrSpnPrice);
                spnFilterPrice.setAdapter(arrPriceAdapter);
                arrBrand.add("Tất cả");
                arrBrandId.add("Tất cả");
                spnFilterBrand.setSelection(arrBrand.size()-1);
                spnFilterBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                        brand_id = arrBrandId.get(i);
                        GetFilterItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                spnFilterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                        price_rance = arrPriceTier.get(i);
                        GetFilterItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                llFilterPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(value.equalsIgnoreCase("ASC")){
                            tv_filter.setText("Giá trị giảm dần");
                            value = "DESC";
                            imgFilterPrice.setImageResource(R.drawable.ic_sort_decrease);
                            GetFilterItem();
                        }else {
                            tv_filter.setText("Giá trị tăng dần");
                            value = "ASC";
                            imgFilterPrice.setImageResource(R.drawable.ic_sort_increase);
                            GetFilterItem();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Brand>> call, Throwable t) {

            }
        });
        ApiRetrofit.apiRetrofit.GetProductByType(product_type).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                progressDialog.hide();
                arrProduct = response.body();
                GridLayoutManager gridLayoutManager = new GridLayoutManager(GeneralProductAct.this,2);
                rv_product.setLayoutManager(gridLayoutManager);
                productAdapter = new ProductAdapter(GeneralProductAct.this, arrProduct);
                rv_product.setAdapter(productAdapter);
                productAdapter.onItemClickListener(new ProductAdapter.OnItemClick() {
                    @Override
                    public void imgClick(int position, MyProduct product) {
                        Log.e("test_size",product.getSizes()+"");
                        Intent intent = new Intent(GeneralProductAct.this, NewProductDetailAct.class);
                        intent.putExtra(Constants.KEY_PRODUCT_ID,product.getId());
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    }

                    @Override
                    public void imgAddToFavoriteClick(int position, MyProduct product) {
                        if(user_id.equalsIgnoreCase("null")){
                            Toast.makeText(GeneralProductAct.this, "Thực hiện đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
                        }else {
                            AddToFavorite(product.getId());
                        }
                    }

                    @Override
                    public void imgRemoveFavorite(int position, MyProduct product) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {

            }
        });
    }
    private void AddToFavorite(String product_id){
        ApiRetrofit.apiRetrofit.InsertFavorite(user_id, product_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().equalsIgnoreCase("true")){
                    Toast.makeText(GeneralProductAct.this, "Thêm vào 'Theo dõi' thành công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(GeneralProductAct.this, "Sản phẩm đang theo dõi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void GetFilterItem() {
        ApiRetrofit.apiRetrofit.GetFilterProduct(brand_id,product_type,value,price_rance).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                arrProduct.removeAll(arrProduct);
                arrProduct.addAll(response.body());
                productAdapter.notifyDataSetChanged();
                Log.e("filter2",response.body().size()+"");
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {
                Log.e("filter2",t.toString());
            }
        });
    }

}