package com.example.modelfashion.Fragment;

import static com.example.modelfashion.Utility.Constants.KEY_PRODUCT_ID;
import static com.example.modelfashion.Utility.Constants.KEY_PRODUCT_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Activity.ProductDetailActivity;
import com.example.modelfashion.Adapter.ProductListAdapter;
import com.example.modelfashion.Adapter.VpSaleMainFmAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.MainMainActivity;
import com.example.modelfashion.Model.ItemSaleMain;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;
import com.example.modelfashion.network.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {
    private ViewPager2 vpSaleMain;
    private CircleIndicator3 ciSale;
    private RecyclerView rcvProduct;
    Repository repository;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private ImageView avatar;
    private String user_id;
    ArrayList<ItemSaleMain> arrItem = new ArrayList<>();
//    private TextView tvCurrentDate, tvGreeting;
//    private ImageView imgUserAvatar;
//    private PreferenceManager preferenceManager;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    ProductListAdapter productListAdapter = new ProductListAdapter();

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

    public MainFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        vpSaleMain = view.findViewById(R.id.vp_sale_main_fm);
        ciSale = view.findViewById(R.id.ci_sale_main_fm);
        rcvProduct = view.findViewById(R.id.rv_men_page_fm);
        progressBar = view.findViewById(R.id.progress_bar);
        avatar = view.findViewById(R.id.img_user_avatar);

        Bundle info = getArguments();
        user_id = info.getString("user_id");
        try {
            setUserAvatar(user_id);
        }catch (Exception e){}


//        tvCurrentDate = view.findViewById(R.id.tv_current_date);
//        tvGreeting = view.findViewById(R.id.tv_greeting);
//        imgUserAvatar = view.findViewById(R.id.img_user_avatar);
//        preferenceManager = new PreferenceManager(requireContext());

        arrItem.add(new ItemSaleMain(R.drawable.test_img));
        arrItem.add(new ItemSaleMain(R.drawable.test_img));
        arrItem.add(new ItemSaleMain(R.drawable.test_img));
        VpSaleMainFmAdapter vpSaleMainFmAdapter = new VpSaleMainFmAdapter(arrItem);
        vpSaleMain.setAdapter(vpSaleMainFmAdapter);

        ciSale.setViewPager(vpSaleMain);

        vpSaleMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunable);
                mHandler.postDelayed(mRunable, 2000);
            }
        });
        initData();

//        initHeader();
//        initClickProfileAvatar();

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.VISIBLE);
            getAllProduct(repository);
        });
        return view;
    }

//    private void initClickProfileAvatar() {
//        imgUserAvatar.setOnClickListener(view -> {
//            if (preferenceManager.getBoolean(Constants.KEY_CHECK_LOGIN)) {
//                ((MainActivity) requireActivity()).moveToFragmentProfile();  // dang nhap roi thi vao profile
//            } else {
//                startActivity(new Intent(requireContext(), SignInActivity.class)); // chua dang nhap thi vao dang nhap
//            }
//        });
//
//    }

//    private void initHeader() {
//        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
//        Calendar cal = Calendar.getInstance(Locale.US);
//        tvCurrentDate.setText(dateFormat.format(cal.getTime()));
//
//        if (cal.get(Calendar.AM_PM) == Calendar.AM) {
//            tvGreeting.setText("Chào buổi sáng");
//        } else {
//            tvGreeting.setText("Chào buổi tối");
//        }
//        Glide.with(requireContext()).load("").placeholder(R.drawable.ic_profile).into(imgUserAvatar);
//    }

    private void initData() {
        repository = new Repository(requireContext());
        getAllProduct(repository);

    }

    private void initListener() {
        productListAdapter.onItemClickListener(new ProductListAdapter.OnItemClickListener() {
            @Override
            public void imgClick(int position, MyProduct product) {
                Intent intent = new Intent(requireActivity(), ProductDetailActivity.class);
                intent.putExtra(KEY_PRODUCT_NAME, product.getProduct_name());
                intent.putExtra(KEY_PRODUCT_ID, product.getId());
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }

            @Override
            public void imgAddToCartClick(int position, MyProduct product) {
                // TODO add to cart
            }

            @Override
            public void seeAll(int position, String type) {

            }
        });
    }

    private void setUserAvatar(String user_id){
        if(!user_id.equalsIgnoreCase("null")){
            ApiRetrofit.apiRetrofit.GetUserById(user_id).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    Glide.with(getActivity()).load(user.getAvatar()).into(avatar);
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }
    private void getAllProduct(Repository repository) {
        Single<ArrayList<MyProduct>> products = repository.getAllProduct();
        compositeDisposable.add(products.doOnSubscribe(disposable -> {
            // show loading
            progressBar.setVisibility(View.VISIBLE);
        })
                .doFinally(() -> {
                    // hide loading
                    progressBar.setVisibility(View.GONE);
                })
                .subscribe(it -> {

                    HashMap<String, String> hmType = new HashMap<>();
                    for (int i = 0; i < it.size(); i++) {
                        hmType.put(it.get(i).getType(), it.get(i).getType());
                    }
                    Set<String> key = hmType.keySet();
                    ArrayList<String> arrProductType = new ArrayList<>(key);

                    productListAdapter = new ProductListAdapter(requireContext(), arrProductType, it);
                    rcvProduct.setAdapter(productListAdapter);
                    initListener();
                }, throwable -> {
                    Log.d(Constants.ERROR_MESSAGE, throwable.toString());
                }));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunable);
        compositeDisposable.dispose();
        mRunable = null;
        mHandler = null;
    }

//    public void onclick(View view) {
//        Toast.makeText(getContext(), "abc", Toast.LENGTH_SHORT).show();
//    }
}