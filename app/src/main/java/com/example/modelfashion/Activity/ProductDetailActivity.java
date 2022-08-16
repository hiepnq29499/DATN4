package com.example.modelfashion.Activity;

import static com.example.modelfashion.Utility.Constants.KEY_PRODUCT_ID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Adapter.ViewPagerDetailProductAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.Model.response.my_product.Sizes;
import com.example.modelfashion.R;
import com.example.modelfashion.network.Repository;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView img_back, img_cart, img_prev, img_next, img_product,
            img_size_s, img_size_m, img_size_l, img_size_xl;
    private TextView tv_price, tv_product_name, tv_product_category, tv_product_availability,
            btn_mua_ngay, btn_them_vao_gio_hang,dialog_info_btn_huy;
    private ViewPager2 viewPager;
    private ViewPagerDetailProductAdapter adapter;
    private Repository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ProgressBar progressBar;

    ArrayList<Sizes> arr_size = new ArrayList<>();
    String size_id, price;
    String user_id = "";
    String productId = "";
    String productName = "";
    private MyProduct myProduct = new MyProduct();
    private ArrayList<ImageView> listImgSize = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_product_detail);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        productId = intent.getStringExtra(KEY_PRODUCT_ID);
        Log.e("t1", user_id + " ");
        // TODO use id to call detail product api
//        ApiRetrofit.apiRetrofit.GetProductsSize(productName).enqueue(new Callback<ArrayList<Sizes>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Sizes>> call, Response<ArrayList<Sizes>> response) {
//                arr_size = response.body();
//                size_id = arr_size.get(0).getId();
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Sizes>> call, Throwable t) {
//
//            }
//        });
        repository = new Repository(getApplicationContext());

//        initView();
//        initData();
//        initListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        disposable.add(
                repository.getProductById(productId, productName).doOnSubscribe(disposable -> {
                    progressBar.setVisibility(View.VISIBLE);
                }).subscribe(myProduct -> {
                    this.myProduct = myProduct;
                    progressBar.setVisibility(View.GONE);
                    setData(myProduct);

                }, throwable -> {

                }));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setData(MyProduct myProduct) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        adapter.setArrItem(myProduct.getPhotos());
        price = myProduct.getPrice();
        tv_product_name.setText(myProduct.getProduct_name());
        tv_price.setText(format.format(Integer.parseInt(myProduct.getPrice())) + " VNĐ");
        tv_product_category.setText("Loại sản phẩm: " + myProduct.getType());

        Glide.with(this).load(myProduct.getPhotos().get(0)).placeholder(R.drawable.test_img2).into(img_product);
//        if (Integer.parseInt(myProduct.getSizes().get(0).getQuantity()) == 0)
//            img_size_s.setVisibility(View.GONE);
//        if (Integer.parseInt(myProduct.getSizes().get(1).getQuantity()) == 0)
//            img_size_m.setVisibility(View.GONE);
//        if (Integer.parseInt(myProduct.getSizes().get(2).getQuantity()) == 0)
//            img_size_l.setVisibility(View.GONE);
//        if (Integer.parseInt(myProduct.getSizes().get(3).getQuantity()) == 0)
//            img_size_xl.setVisibility(View.GONE);
    }

    private int currentCoverImage = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListener() {
        img_back.setOnClickListener(view -> {
            finish();
        });
        img_cart.setOnClickListener(view -> {
            // TODO CART
        });
        img_prev.setOnClickListener(view -> {
            // TODO PREV
            if (viewPager.getCurrentItem() == 0) {
                currentCoverImage = adapter.getItemCount() - 1;
            } else {
                currentCoverImage = currentCoverImage - 1;
            }
            viewPager.setCurrentItem(currentCoverImage);
        });
        img_next.setOnClickListener(view -> {
            // TODO NEXT
            if (viewPager.getCurrentItem() == (adapter.getItemCount() - 1)) {
                currentCoverImage = 0;
            } else {
                currentCoverImage = currentCoverImage + 1;
            }
            viewPager.setCurrentItem(currentCoverImage);
        });
        loadImageUrl("https://cf.shopee.vn/file/7624d506af9460c8f4e8e5a80c30b514", img_product);
        img_size_s.setOnClickListener(view -> {
            // TODO SIZE S
            size_id = arr_size.get(0).getSize_id();
            checkSize(1);
        });
        img_size_m.setOnClickListener(view -> {
            // TODO SIZE M
            size_id = arr_size.get(1).getSize_id();
            checkSize(2);
        });
        img_size_l.setOnClickListener(view -> {
            // TODO SIZE L
            size_id = arr_size.get(2).getSize_id();
            checkSize(3);
        });
        img_size_xl.setOnClickListener(view -> {
            // TODO SIZE XL
            size_id = arr_size.get(3).getSize_id();
            checkSize(4);
        });

        tv_price.setText("650,000 VND");
        tv_product_name.setText("MARU BLAZER (WHITE)");
        tv_product_category.setText("Loại sản phẩm: MARU BLAZER");
        tv_product_availability.setText("Tình trạng: còn hàng");
        btn_mua_ngay.setOnClickListener(view -> {
            // TODO BUY
            if (user_id.equalsIgnoreCase("null")) {
                Toast.makeText(this, "Bạn chưa thực hiện đăng nhập", Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_input_info_user);
                dialog_info_btn_huy = dialog.findViewById(R.id.dialog_info_btn_huy);
                dialog_info_btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                String date = LocalDate.now().toString();
                ApiRetrofit.apiRetrofit.CheckSizeLeft(size_id, "1").enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("ok")) {
                            ApiRetrofit.apiRetrofit.InsertBillBuyNow(user_id, date, price, size_id).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(ProductDetailActivity.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        } else if (response.body().equals("fail")) {
                            Toast.makeText(ProductDetailActivity.this, "Size này đã hết hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Lỗi db", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
            }
        });
        btn_them_vao_gio_hang.setOnClickListener(view -> {
            // TODO ADD ON CART
            if (user_id.equalsIgnoreCase("null")) {
                Toast.makeText(this, "Bạn chưa thực hiện đăng nhập", Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_input_info_user);
                dialog_info_btn_huy = dialog.findViewById(R.id.dialog_info_btn_huy);
                dialog_info_btn_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else {
                ApiRetrofit.apiRetrofit.CheckSizeLeft(size_id, "1").enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("ok")) {
                            ApiRetrofit.apiRetrofit.InsertCart(user_id, size_id, productName, "1").enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("duplicated")) {
                                        Toast.makeText(ProductDetailActivity.this, "Sản phẩm đã nằm trong giỏ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (response.body().equals("ok")) {
                                            Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProductDetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                            Log.e("err", response.body());
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        } else if (response.body().equals("fail")) {
                            Toast.makeText(ProductDetailActivity.this, "Size này đã hết hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Lỗi db", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {}
                });
            }
        });
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        viewPager = findViewById(R.id.view_pager);
        img_back = findViewById(R.id.img_back);
        img_cart = findViewById(R.id.img_cart);
        img_prev = findViewById(R.id.img_prev);
        img_next = findViewById(R.id.img_next);
        img_product = findViewById(R.id.img_product);
        img_size_s = findViewById(R.id.img_size_s);
        img_size_m = findViewById(R.id.img_size_m);
        img_size_l = findViewById(R.id.img_size_l);
        img_size_xl = findViewById(R.id.img_size_xl);
        listImgSize.add(img_size_s);
        listImgSize.add(img_size_m);
        listImgSize.add(img_size_l);
        listImgSize.add(img_size_xl);

        tv_price = findViewById(R.id.tv_price);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_product_category = findViewById(R.id.tv_product_category);
        tv_product_availability = findViewById(R.id.tv_product_availability);
        btn_mua_ngay = findViewById(R.id.btn_mua_ngay);
        btn_them_vao_gio_hang = findViewById(R.id.btn_them_vao_gio_hang);


        adapter = new ViewPagerDetailProductAdapter();

        viewPager.setAdapter(adapter);
    }


    private void loadImageUrl(String url, ImageView img) {
        Glide.with(this).load(url).placeholder(R.drawable.ic_logo).into(img);
    }

    private void checkSize(int position) {
        switch (position) {
            case 1: {
                img_size_s.setImageResource(R.drawable.ic_size_s_selected);
                img_size_m.setImageResource(R.drawable.ic_size_m);
                img_size_l.setImageResource(R.drawable.ic_size_l);
                img_size_xl.setImageResource(R.drawable.ic_size_xl);
                break;
            }
            case 2: {
                img_size_s.setImageResource(R.drawable.ic_size_s);
                img_size_m.setImageResource(R.drawable.ic_size_m_selected);
                img_size_l.setImageResource(R.drawable.ic_size_l);
                img_size_xl.setImageResource(R.drawable.ic_size_xl);
                break;
            }
            case 3: {
                img_size_s.setImageResource(R.drawable.ic_size_s);
                img_size_m.setImageResource(R.drawable.ic_size_m);
                img_size_l.setImageResource(R.drawable.ic_size_l_selected);
                img_size_xl.setImageResource(R.drawable.ic_size_xl);
                break;
            }
            case 4: {
                img_size_s.setImageResource(R.drawable.ic_size_s);
                img_size_m.setImageResource(R.drawable.ic_size_m);
                img_size_l.setImageResource(R.drawable.ic_size_l);
                img_size_xl.setImageResource(R.drawable.ic_size_xl_selected);
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}