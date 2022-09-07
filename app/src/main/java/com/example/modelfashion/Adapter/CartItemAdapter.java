package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Activity.NewProductDetailAct;
import com.example.modelfashion.Fragment.NewCartFragment;
import com.example.modelfashion.Fragment.NewProfileFragment;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.my_product.CartProduct;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    ArrayList<MyProduct> arrProduct = new ArrayList<>();
    ArrayList<CartProduct> arrCart = new ArrayList<>();
    String userId;
    Context context;

    public CartItemAdapter(Context context, ArrayList<MyProduct> arrProduct, ArrayList<CartProduct> arrCart, String userId) {
        this.context = context;
        this.arrProduct = arrProduct;
        this.arrCart = arrCart;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String money_format = formatter.format(Integer.parseInt(arrProduct.get(position).getPrice().split("\\.")[0]) * (100 - Float.parseFloat(arrProduct.get(position).getDiscount_rate())) / 100);
        Glide.with(context).load(arrProduct.get(position).getPhotos().get(0).replace("localhost", Constants.KEY_IP)).into(holder.img_prod);
        holder.tv_name_prod.setText(arrProduct.get(position).getProduct_name());
        holder.tv_price.setText("Price: " + money_format + " VNĐ");
        holder.edt_qty.setText(arrCart.get(position).getQuantity());
        changeTotal(holder, position, formatter);
        holder.edt_qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                arrCart.get(position).setQuantity(holder.edt_qty.getText().toString());
                holder.edt_qty.setText(arrCart.get(position).getQuantity());
                changeQuantityItem(holder, position, formatter, arrCart.get(position).getUserId(), arrCart.get(position).getProductId(), arrCart.get(position).getQuantity());
            }
        });
        for (int i = 0; i < arrProduct.get(position).getSizes().size(); i++) {
            if (arrCart.get(position).getSizeId().equalsIgnoreCase(arrProduct.get(position).getSizes().get(i).getSize_id())) {
                holder.tv_size.setText("Size: " + arrProduct.get(position).getSizes().get(i).getSize());
            }
        }
        holder.tv_brand.setText("Brand: " + arrProduct.get(position).getBrand());
        holder.tv_location.setText("Location: " + arrProduct.get(position).getLocation());
        holder.tv_down_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (holder.edt_qty.getText().toString().isEmpty())
                        holder.edt_qty.setText("0");
                    int num = Integer.parseInt(holder.edt_qty.getText().toString()) - 1;
                    if (num <= 0 || num > 9999) return;
                    arrCart.get(position).setQuantity(Integer.toString(num));
                    holder.edt_qty.setText(arrCart.get(position).getQuantity());
                    changeQuantityItem(holder, position, formatter, arrCart.get(position).getUserId(), arrCart.get(position).getProductId(), arrCart.get(position).getQuantity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.tv_up_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (holder.edt_qty.getText().toString().isEmpty())
                        holder.edt_qty.setText("1");
                    int num = Integer.parseInt(holder.edt_qty.getText().toString()) + 1;
                    if (num <= 0 || num > 9999) return;
                    arrCart.get(position).setQuantity(Integer.toString(num));
                    holder.edt_qty.setText(arrCart.get(position).getQuantity());
                    changeQuantityItem(holder, position, formatter, arrCart.get(position).getUserId(), arrCart.get(position).getProductId(), arrCart.get(position).getQuantity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.removeCartItem(position, arrCart.get(position).getCartId());
//                ApiRetrofit.apiRetrofit.RemoveCartItem2(arrCart.get(position).getCartId()).enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if(response.body().equalsIgnoreCase("ok")){
//                            arrCart.remove(position);
//                            arrProduct.remove(position);
//                            notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });
            }
        });
        holder.ll_see_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewProductDetailAct.class);
                intent.putExtra("user_id", userId);
                intent.putExtra(Constants.KEY_PRODUCT_ID, arrProduct.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.cb_item.setChecked(arrCart.get(position).getIsCheck());
        holder.cb_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                arrCart.get(position).setIsCheck(b);
                if (b) {
                    onItemClick.itemCheckedTrue(position, arrCart.get(position).getCartId());
                } else {
                    onItemClick.itemCheckedFalse(position, arrCart.get(position).getCartId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_prod;
        TextView tv_name_prod, tv_price, tv_down_qty, tv_up_qty, tv_size, tv_brand, tv_location, tv_total_amount, tv_remove;
        EditText edt_qty;
        LinearLayout ll_see_detail;
        CheckBox cb_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_prod = itemView.findViewById(R.id.img_product_cart);
            tv_name_prod = itemView.findViewById(R.id.tv_product_name_cart);
            tv_price = itemView.findViewById(R.id.tv_price_cart);
            tv_down_qty = itemView.findViewById(R.id.tv_down_qty);
            tv_up_qty = itemView.findViewById(R.id.tv_up_qty);
            edt_qty = itemView.findViewById(R.id.edt_qty);
            tv_size = itemView.findViewById(R.id.tv_size_cart);
            tv_brand = itemView.findViewById(R.id.tv_brand_cart);
            tv_location = itemView.findViewById(R.id.tv_location_cart);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount_cart);
            tv_remove = itemView.findViewById(R.id.tv_remove_item_cart);
            ll_see_detail = itemView.findViewById(R.id.ll_see_detail_product);
            cb_item = itemView.findViewById(R.id.cb_cart_item);
        }
    }

    private void changeQuantityItem(ViewHolder holder, int position, DecimalFormat format, String userID, String id, String quantity) {
        changeTotal(holder, position, format);
        onItemClick.onChangeQuantity();
        ApiRetrofit.apiRetrofit.changeQuantityInCart(userID, id, quantity).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void changeTotal(ViewHolder holder, int position, DecimalFormat format) {
        String money_format_2 = format.format(Integer.parseInt(arrProduct.get(position).getPrice().split("\\.")[0]) * Integer.parseInt(arrCart.get(position).getQuantity()) * (100 - Float.parseFloat(arrProduct.get(position).getDiscount_rate())) / 100);
        holder.tv_total_amount.setText(money_format_2 + " VNĐ");
    }

    public OnItemClickListener onItemClick;

    public void OnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClick = onItemClickListener;
    }

    public interface OnItemClickListener {
        void removeCartItem(int position, String cartId);

        void itemCheckedTrue(int position, String cartId);

        void itemCheckedFalse(int position, String cartId);

        void onChangeQuantity();
    }
}
