package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.modelfashion.Adapter.BrandListAdapter;
import com.example.modelfashion.Adapter.FilterBrandAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.R;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandListAct extends AppCompatActivity {
    String user_id;
    TextView tvFilter;
    RecyclerView rvBrand;
    Boolean checkAll = false;
    ArrayList<String> arrLocation = new ArrayList<>();
    ArrayList<Brand> arrBrand = new ArrayList<>();
    BrandListAdapter brandListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        tvFilter = findViewById(R.id.tv_filter_nation);
        rvBrand = findViewById(R.id.rv_brand);
        SetData();
    }
    private void SetData(){
        ApiRetrofit.apiRetrofit.GetAllBrand().enqueue(new Callback<ArrayList<Brand>>() {
            @Override
            public void onResponse(Call<ArrayList<Brand>> call, Response<ArrayList<Brand>> response) {
                arrBrand = response.body();
                brandListAdapter = new BrandListAdapter(BrandListAct.this, arrBrand);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BrandListAct.this,LinearLayoutManager.VERTICAL,false);
                rvBrand.setLayoutManager(linearLayoutManager);
                rvBrand.setAdapter(brandListAdapter);
                brandListAdapter.SetOnItemClickListener(new BrandListAdapter.OnClickItemListener() {
                    @Override
                    public void OnClickItem(int position) {
                        Intent intent = new Intent(BrandListAct.this,BrandsProductAct.class);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("brand_id",arrBrand.get(position).getBrand_id());
                        startActivity(intent);
                    }
                });
                tvFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiRetrofit.apiRetrofit.GetBrandsLocation().enqueue(new Callback<ArrayList<String>>() {
                            @Override
                            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                                Dialog dialog = new Dialog(BrandListAct.this);
                                dialog.setContentView(R.layout.brand_filter_layout);
                                dialog.setCancelable(true);
                                RecyclerView rvFilter = dialog.findViewById(R.id.rv_nation);
                                CheckBox checkBox = dialog.findViewById(R.id.cb_check_all);
                                Button btnConfirm = dialog.findViewById(R.id.btn_confirm_nation_filter);
                                FilterBrandAdapter filterBrandAdapter = new FilterBrandAdapter(BrandListAct.this, response.body(),checkAll);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(BrandListAct.this,3,LinearLayoutManager.VERTICAL,false);
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(b){
                                            checkAll = true;
                                            filterBrandAdapter.OnCheckChange(checkAll);
                                            arrLocation.remove(arrLocation);
                                            filterBrandAdapter.notifyDataSetChanged();
                                            Log.e("location",arrLocation.toString());
                                        }else {
                                            checkAll = false;
                                            filterBrandAdapter.OnCheckChange(checkAll);
                                            arrLocation.remove(arrLocation);
                                            filterBrandAdapter.notifyDataSetChanged();
                                            Log.e("location",arrLocation.toString());
                                        }
                                    }
                                });
                                rvFilter.setLayoutManager(gridLayoutManager);
                                rvFilter.setAdapter(filterBrandAdapter);
                                filterBrandAdapter.OnItemClickListener(new FilterBrandAdapter.OnClickItemListener() {
                                    @Override
                                    public void onCheckTrue(int position) {
                                        arrLocation.add(response.body().get(position));
                                        Log.e("location",arrLocation.toString());
                                    }

                                    @Override
                                    public void onCheckFalse(int position) {
                                        arrLocation.remove(response.body().get(position));
                                        Log.e("location",arrLocation.toString());
                                    }
                                });
                                btnConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        JSONArray json_arr_location = new JSONArray(arrLocation);
                                        GetBrandsByLocation(json_arr_location);
                                        arrLocation.removeAll(arrLocation);
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }

                            @Override
                            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Brand>> call, Throwable t) {

            }
        });
    }

    public void GetBrandsByLocation(JSONArray jsonArray){
        ApiRetrofit.apiRetrofit.GetBrandByLocation(jsonArray).enqueue(new Callback<ArrayList<Brand>>() {
            @Override
            public void onResponse(Call<ArrayList<Brand>> call, Response<ArrayList<Brand>> response) {
                arrBrand.removeAll(arrBrand);
                arrBrand.addAll(response.body());
                brandListAdapter.notifyDataSetChanged();
                Log.e("brand",response.body().size()+"");
            }

            @Override
            public void onFailure(Call<ArrayList<Brand>> call, Throwable t) {
                Log.e("brand",t.toString()+"");
            }
        });
    }
}