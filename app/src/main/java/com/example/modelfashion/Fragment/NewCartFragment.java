package com.example.modelfashion.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfashion.Activity.DeliveryAddressAct;
import com.example.modelfashion.Activity.MainActivity;
import com.example.modelfashion.Adapter.CartItemAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.DeliveryInfo;
import com.example.modelfashion.Model.Product;
import com.example.modelfashion.Model.response.my_product.CartProduct;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

public class NewCartFragment extends Fragment {
    TextView tv_add_address, tv_total_amount;
    Button btn_payment;
    RecyclerView rv_cart;
    CheckBox cb_choice_all_cart_item;
    String user_id;
    ArrayList<MyProduct> arrProduct = new ArrayList<>();
    ArrayList<CartProduct> arrCartProduct = new ArrayList<>();
    ArrayList<MyProduct> arrProductBuy = new ArrayList<>();
    int size_item_buy = 0;
    Boolean check_product_status = true;
    ArrayList<CartProduct> arrCartItemBuy = new ArrayList<>();
    CartItemAdapter cartItemAdapter;
    DecimalFormat formatter = new DecimalFormat("###,###,###");
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "FREE STYLE MEN";
    private String merchantCode = "MOMOJDFR20220731";
    private String merchantNameLabel = "BacNguyen";
    private String description = "Mua Quan Ao";

    public NewCartFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_2, container, false);
        tv_add_address = view.findViewById(R.id.tv_add_delivery_address);
        tv_total_amount = view.findViewById(R.id.tv_total_new_cart);
        btn_payment = view.findViewById(R.id.btn_payment_new_cart);
        rv_cart = view.findViewById(R.id.rv_new_cart);
        cb_choice_all_cart_item = view.findViewById(R.id.cb_choice_all_cart_item);
        Bundle info = getArguments();
        user_id = info.getString("user_id");
        tv_total_amount.setText("0 VND");
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        if (user_id != "null") {
            SetCartData(user_id);
        }
        tv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAddress();
            }
        });
        cb_choice_all_cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CartProduct item : arrCartProduct) {
                    item.setIsCheck(cb_choice_all_cart_item.isChecked());
                }

                rv_cart.getAdapter().notifyDataSetChanged();
            }
        });
        return view;
    }

    private void SetCartData(String user_id) {
        ApiRetrofit.apiRetrofit.GetProductInCart(user_id).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                arrProduct = response.body();
                ApiRetrofit.apiRetrofit.GetCart(user_id).enqueue(new Callback<ArrayList<CartProduct>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CartProduct>> call, Response<ArrayList<CartProduct>> response) {
                        arrCartProduct = response.body();
                        Log.e("remove_check2", arrCartProduct.size() + "");
                        cartItemAdapter = new CartItemAdapter(getContext(), arrProduct, arrCartProduct, user_id);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rv_cart.setLayoutManager(linearLayoutManager);
                        cartItemAdapter.OnItemClickListener(new CartItemAdapter.OnItemClickListener() {
                            @Override
                            public void removeCartItem(int position, String cartId) {
                                ApiRetrofit.apiRetrofit.RemoveCartItem2(cartId).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.body().equalsIgnoreCase("ok")) {
                                            if (!arrProductBuy.isEmpty() && !arrCartItemBuy.isEmpty()) {
                                                Toast.makeText(getContext(), "Vui lòng bỏ tick tất cả sản phẩm", Toast.LENGTH_SHORT).show();
                                            } else {
                                                arrProduct.remove(position);
                                                arrCartProduct.remove(position);
                                                Log.e("remove_check", arrProduct.size() + " " + arrCartProduct.size());
                                                cartItemAdapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Log.e("remove_err", response.body());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void itemCheckedTrue(int position, String cartId) {
                                tv_total_amount.setText(formatter.format(sumTotalCart()) + " VND");
                                arrCartItemBuy.add(arrCartProduct.get(position));
                                arrProductBuy.add(arrProduct.get(position));

                                if (arrCartProduct.stream().allMatch(CartProduct::getIsCheck)) {
                                    cb_choice_all_cart_item.setChecked(true);
                                }
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void itemCheckedFalse(int position, String cartId) {
                                tv_total_amount.setText(formatter.format(sumTotalCart()) + " VND");
                                arrCartItemBuy.remove(arrCartProduct.get(position));
                                arrProductBuy.remove(arrProduct.get(position));

                                if (arrCartProduct.stream().anyMatch(cartProduct -> !cartProduct.getIsCheck())) {
                                    cb_choice_all_cart_item.setChecked(false);
                                }
                            }

                            @Override
                            public void onChangeQuantity() {
                                tv_total_amount.setText(formatter.format(sumTotalCart()) + " VND");
                            }
                        });
                        btn_payment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SetPayment();
                            }
                        });
                        rv_cart.setAdapter(cartItemAdapter);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CartProduct>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {

            }
        });
    }

    private int sumTotalCart() {
        int result = 0;

        for (int i = 0; i < arrCartProduct.size(); i++) {
            if (!arrCartProduct.get(i).getIsCheck()) continue;

            result += Integer.parseInt(arrCartProduct.get(i).getQuantity()) * Integer.parseInt(arrProduct.get(i).getPrice().split("\\.")[0]);
        }

        return result;
    }

    private void AddAddress() {
        if (user_id != "null") {
            Intent intent = new Intent(getContext(), DeliveryAddressAct.class);
            intent.putExtra("user_id", user_id);
            getContext().startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Bạn chưa thực hiện đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void SetPayment() {
        if (sumTotalCart() != 0) {
            ApiRetrofit.apiRetrofit.GetDeliveryInfo(user_id).enqueue(new Callback<DeliveryInfo>() {
                @Override
                public void onResponse(Call<DeliveryInfo> call, Response<DeliveryInfo> response) {
                    DeliveryInfo deliveryInfo = response.body();
                    if (!deliveryInfo.getDelivery_id().equalsIgnoreCase("null")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Sử dụng địa chỉ giao hàng hiện tại ?");
                        builder.setCancelable(true);
                        builder.setNegativeButton("Địa chỉ mới", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getContext(), DeliveryAddressAct.class);
                                intent.putExtra("user_id", user_id);
                                getContext().startActivity(intent);
                            }
                        });
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                InsertBill(deliveryInfo.getReceiver_name(), deliveryInfo.getStreet_address(), deliveryInfo.getCity(),
                                        deliveryInfo.getContact(), Integer.toString(sumTotalCart()), arrCartItemBuy);

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Bạn chưa có địa chỉ nhận hàng, hãy điền thông tin của bạn");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getContext(), DeliveryAddressAct.class);
                                intent.putExtra("user_id", user_id);
                                getContext().startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.create().dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

                @Override
                public void onFailure(Call<DeliveryInfo> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getContext(), "Bạn chưa chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
        }
    }

    private void InsertBill(String receiver_name, String street, String city, String contact, String amount, ArrayList<CartProduct> arrCartProduct) {
        ApiRetrofit.apiRetrofit.InsertBill2(user_id, receiver_name, street, city, contact, amount).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Check11", response.body());
                for (int i = 0; i < arrProductBuy.size(); i++) {
                    if (arrProductBuy.get(i).getStatus().equals("Hết hàng")) {
                        check_product_status = false;
                        Toast.makeText(getContext(), "Sản phẩm " + arrProductBuy.get(i).getProduct_name() + " đã hết hàng", Toast.LENGTH_SHORT).show();
                    }
                }
                if (check_product_status == true) {
                    cb_choice_all_cart_item.setChecked(false);
                    for (int i = 0; i < arrCartItemBuy.size(); i++) {
                        InsertBillDetail(arrCartItemBuy.get(i).getSizeId(), response.body(), arrCartItemBuy.get(i).getProductId(), arrCartProduct.get(i).getQuantity(),
                                arrCartItemBuy.get(i).getCartId(), arrProductBuy.get(i).getDiscount_rate(), arrCartItemBuy.size());
                    }
                    requestPayment();
                    Toast.makeText(getContext(), "Đặt hàng thành công, kiểm tra lại trong mục hóa đơn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void InsertBillDetail(String size_id, String bill_id, String product_id, String quantity, String cart_id, String discount_rate, int size_check) {
        ApiRetrofit.apiRetrofit.InsertBillDetail2(size_id, bill_id, product_id, quantity, cart_id, discount_rate).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                size_item_buy++;
                if (size_item_buy == size_check) {
                    size_item_buy = 0;
                    arrProduct.removeAll(arrProductBuy);
                    arrCartProduct.removeAll(arrCartItemBuy);
                    arrProductBuy.removeAll(arrProductBuy);
                    arrCartItemBuy.removeAll(arrCartItemBuy);
                    cartItemAdapter = new CartItemAdapter(getContext(), arrProduct, arrCartProduct, user_id);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_cart.setLayoutManager(linearLayoutManager);
                    cartItemAdapter.OnItemClickListener(new CartItemAdapter.OnItemClickListener() {
                        @Override
                        public void removeCartItem(int position, String cartId) {
                            ApiRetrofit.apiRetrofit.RemoveCartItem2(cartId).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equalsIgnoreCase("ok")) {
                                        if (!arrProductBuy.isEmpty() && !arrCartItemBuy.isEmpty()) {
                                            Toast.makeText(getContext(), "Vui lòng bỏ tick tất cả sản phẩm", Toast.LENGTH_SHORT).show();
                                        } else {
                                            arrProduct.remove(position);
                                            arrCartProduct.remove(position);
                                            Log.e("remove_check", arrProduct.size() + " " + arrCartProduct.size());
                                            cartItemAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Log.e("remove_err", response.body());
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void itemCheckedTrue(int position, String cartId) {
                            tv_total_amount.setText(formatter.format(sumTotalCart()) + " VND");
                            arrCartItemBuy.add(arrCartProduct.get(position));
                            arrProductBuy.add(arrProduct.get(position));
                        }

                        @Override
                        public void itemCheckedFalse(int position, String cartId) {
                            tv_total_amount.setText(formatter.format(sumTotalCart()) + " VND");
                            arrCartItemBuy.remove(arrCartProduct.get(position));
                            arrProductBuy.remove(arrProduct.get(position));
                        }

                        @Override
                        public void onChangeQuantity() {
                            tv_total_amount.setText(formatter.format(sumTotalCart()) + " VND");
                        }
                    });
                    btn_payment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SetPayment();
                        }
                    });
                    rv_cart.setAdapter(cartItemAdapter);
                    tv_total_amount.setText("0 VND");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //Get token through MoMo app
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", sumTotalCart()); //Kiểu integer
        eventValue.put("orderId", "orderId123456789"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", 0); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(getActivity(), eventValue);

    }
    //Get token callback from MoMo app an submit to server side

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }
                    Toast.makeText(getContext(), "Ok", Toast.LENGTH_SHORT).show();
                    if (token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
//                    tvMessage.setText("message: " + message);
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));

                } else {
                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                }
            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }
}
