package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Adapter.ProductAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.Model.Product;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandsProductAct extends AppCompatActivity {
    LinearLayout llFillPrice;
    TextView tvBrandTitle, tvBrandLocation, tvBrandDesc, tvFilterBrand;
    ImageView imgBrandLogo, imgFilterBrand;
    Spinner spnFilter;
    RecyclerView rvProduct;
    String user_id = "";
    String brand_id = "";
    ProductAdapter productAdapter;
    ArrayList<String> arrType = new ArrayList<>();
    ArrayList<MyProduct> arrProduct = new ArrayList<>();
    String type = "";
    String value = "ASC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands_product);
        llFillPrice = findViewById(R.id.ll_fill_price_brand_product);
        tvBrandTitle = findViewById(R.id.tv_brand_title);
        tvBrandLocation = findViewById(R.id.tv_brand_location);
        tvBrandDesc = findViewById(R.id.tv_brand_description);
        tvFilterBrand = findViewById(R.id.tv_fill_price_brand_product);
        imgBrandLogo = findViewById(R.id.img_brand_logo);
        imgFilterBrand = findViewById(R.id.img_fill_price_brand_product);
        spnFilter = findViewById(R.id.spn_fill_product);
        rvProduct = findViewById(R.id.rv_brand_product);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        brand_id = intent.getStringExtra("brand_id");
        SetData();
    }
    private void SetData(){
        ApiRetrofit.apiRetrofit.GetBrandById(brand_id).enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, Response<Brand> response) {
                tvBrandTitle.setText(response.body().getName());
                Glide.with(BrandsProductAct.this).load(response.body().getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrandLogo);
                tvBrandLocation.setText("Nhãn hiệu thời trang đến từ "+response.body().getLocation());
                tvBrandDesc.setText(response.body().getDescription());
                GetBrandsProduct();
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable t) {

            }
        });
    }

    private void GetBrandsProduct(){
        ApiRetrofit.apiRetrofit.GetBrandsProduct(brand_id).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                arrProduct = response.body();
                productAdapter = new ProductAdapter(BrandsProductAct.this,arrProduct);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(BrandsProductAct.this,2,RecyclerView.VERTICAL,false);
                rvProduct.setLayoutManager(gridLayoutManager);
                rvProduct.setAdapter(productAdapter);
                for (int i=0; i<arrProduct.size(); i++){
                    arrType.add(arrProduct.get(i).getType());
                }
                Set<String> filter_type = new LinkedHashSet<>();
                filter_type.addAll(arrType);
                arrType.clear();
                arrType.addAll(filter_type);
                arrType.add("Tất cả");
                ArrayAdapter fillTypeAdapter = new ArrayAdapter(BrandsProductAct.this,R.layout.support_simple_spinner_dropdown_item,arrType);
                spnFilter.setAdapter(fillTypeAdapter);
                type = "Tất cả";
                GetFilterItem();
                spnFilter.setSelection(arrType.size()-1);
                llFillPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(value.equalsIgnoreCase("ASC")){
                            tvFilterBrand.setText("Giá trị giảm dần");
                            value = "DESC";
                            imgFilterBrand.setImageResource(R.drawable.ic_sort_decrease);
                            GetFilterItem();
                        }else {
                            tvFilterBrand.setText("Giá trị tăng dần");
                            value = "ASC";
                            imgFilterBrand.setImageResource(R.drawable.ic_sort_increase);
                            GetFilterItem();
                        }
                    }
                });
                spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        type = arrType.get(i);
                        GetFilterItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                productAdapter.onItemClickListener(new ProductAdapter.OnItemClick() {
                    @Override
                    public void imgClick(int position, MyProduct product) {
                        Intent intent = new Intent(BrandsProductAct.this,NewProductDetailAct.class);
                        intent.putExtra(Constants.KEY_PRODUCT_ID,product.getId());
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    }

                    @Override
                    public void imgAddToFavoriteClick(int position, MyProduct product) {
                        if(user_id.equalsIgnoreCase("null")){
                            Toast.makeText(BrandsProductAct.this, "Thực hiện đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(BrandsProductAct.this, "Thêm vào 'Theo dõi' thành công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BrandsProductAct.this, "Sản phẩm đang theo dõi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void GetFilterItem(){
        ApiRetrofit.apiRetrofit.GetFilterBrandsProduct(brand_id,type,value).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                arrProduct.removeAll(arrProduct);
                arrProduct.addAll(response.body());
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {

            }
        });
    }
}