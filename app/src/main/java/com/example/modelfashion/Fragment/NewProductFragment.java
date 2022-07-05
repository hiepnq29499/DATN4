package com.example.modelfashion.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Activity.ProductDetailActivity;
import com.example.modelfashion.Activity.SignIn.SignUpActivity;
import com.example.modelfashion.Adapter.ProductListAdapter;
import com.example.modelfashion.Adapter.VpSaleMainFmAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.Model.ItemSaleMain;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProductFragment extends Fragment {
    private ViewPager2 vpSaleMain;
    private CircleIndicator3 ciSale;
    private RecyclerView rvProductFrag;
    private ProductListAdapter productListAdapter;
    private ArrayList<MyProduct> arrMyProduct = new ArrayList<>();
    private ArrayList<String> arrType = new ArrayList<>();
    private ArrayList<Brand> arrBrand = new ArrayList<>();
    private ImageView imgBrand1, imgBrand2, imgBrand3, imgBrand4, imgBrand5, imgBrand6;
    ArrayList<ItemSaleMain> arrItem = new ArrayList<>();
    private String user_id;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = vpSaleMain.getCurrentItem();
            if (currentPosition == arrItem.size() - 1) {
                vpSaleMain.setCurrentItem(0);
            } else
                vpSaleMain.setCurrentItem(currentPosition + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_product, container, false);
        vpSaleMain = view.findViewById(R.id.vp_sale_main_fm);
        ciSale = view.findViewById(R.id.ci_sale_main_fm);
        rvProductFrag = view.findViewById(R.id.rv_men_page_fm);
        imgBrand1 = view.findViewById(R.id.brand_logo1);
        imgBrand2 = view.findViewById(R.id.brand_logo2);
        imgBrand3 = view.findViewById(R.id.brand_logo3);
        imgBrand4 = view.findViewById(R.id.brand_logo4);
        imgBrand5 = view.findViewById(R.id.brand_logo5);
        imgBrand6 = view.findViewById(R.id.brand_logo6);

        Bundle info = getArguments();
        user_id = info.getString("user_id");
        arrItem.add(new ItemSaleMain(R.drawable.test_img));
        arrItem.add(new ItemSaleMain(R.drawable.test_img));
        arrItem.add(new ItemSaleMain(R.drawable.test_img));
        VpSaleMainFmAdapter vpSaleMainFmAdapter = new VpSaleMainFmAdapter(arrItem);
        vpSaleMain.setAdapter(vpSaleMainFmAdapter);
        GetProductData();
        ciSale.setViewPager(vpSaleMain);
        vpSaleMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunable);
                mHandler.postDelayed(mRunable, 2000);
            }
        });
        return view;
    }
    private void GetProductData(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải sản phẩm");
        progressDialog.show();
//        progressDialog.setCancelable(false);
        ApiRetrofit.apiRetrofit.GetAllProduct().enqueue(new Callback<ArrayList<MyProduct>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                GetBrandData();
                arrMyProduct = response.body();
                ApiRetrofit.apiRetrofit.GetRecentType().enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                        progressDialog.hide();
                        arrType = response.body();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rvProductFrag.setLayoutManager(linearLayoutManager);
                        productListAdapter = new ProductListAdapter(getContext(),arrType,arrMyProduct);
                        rvProductFrag.setAdapter(productListAdapter);
                        productListAdapter.onItemClickListener(new ProductListAdapter.OnItemClickListener() {
                            @Override
                            public void imgClick(int position, MyProduct product) {
                                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                intent.putExtra(Constants.KEY_PRODUCT_ID,product.getId());
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                            }

                            @Override
                            public void imgAddToCartClick(int position, MyProduct product) {

                            }
                        });
                        Log.e("loading_prod",arrMyProduct.size()+" "+arrType);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }
    private void GetBrandData(){
            ApiRetrofit.apiRetrofit.Get6RdBrand().enqueue(new Callback<ArrayList<Brand>>() {
                @Override
                public void onResponse(Call<ArrayList<Brand>> call, Response<ArrayList<Brand>> response) {
                    arrBrand = response.body();
                    Glide.with(getContext()).load(arrBrand.get(0).getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrand1);
                    Glide.with(getContext()).load(arrBrand.get(1).getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrand2);
                    Glide.with(getContext()).load(arrBrand.get(2).getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrand3);
                    Glide.with(getContext()).load(arrBrand.get(3).getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrand4);
                    Glide.with(getContext()).load(arrBrand.get(4).getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrand5);
                    Glide.with(getContext()).load(arrBrand.get(5).getLogo().replace("localhost", Constants.KEY_IP)).into(imgBrand6);
                }

                @Override
                public void onFailure(Call<ArrayList<Brand>> call, Throwable t) {

                }
            });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunable);
        mRunable = null;
        mHandler = null;
    }
}
