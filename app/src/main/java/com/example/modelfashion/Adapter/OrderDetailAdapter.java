package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.bill.BillDetail;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    Context context;
    ArrayList<BillDetail> arrBillDetail = new ArrayList<>();
    String status;
    public OrderDetailAdapter(Context context, ArrayList<BillDetail> arrBillDetail, String status){
        this.context = context;
        this.arrBillDetail = arrBillDetail;
        this.status = status;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_order_detail_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ApiRetrofit.apiRetrofit.GetProductById(arrBillDetail.get(position).getProductId()).enqueue(new Callback<MyProduct>() {
            @Override
            public void onResponse(Call<MyProduct> call, Response<MyProduct> response) {
                Glide.with(context).load(response.body().getPhotos().get(0).replace("localhost", Constants.KEY_IP)).into(holder.img_product);
            }

            @Override
            public void onFailure(Call<MyProduct> call, Throwable t) {

            }
        });
        if(!status.equalsIgnoreCase("Đã giao")){
            holder.img_rate.setVisibility(View.INVISIBLE);
        }else {
            holder.img_rate.setVisibility(View.VISIBLE);
            holder.img_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.OnRateClick(position);
                }
            });
        }
        holder.tv_product_name.setText(arrBillDetail.get(position).getProductName());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String money_format = formatter.format(Integer.parseInt(arrBillDetail.get(position).getPrice().split("\\.")[0]));
        holder.tv_price.setText("Tổng: "+money_format+" VND");
        if(Float.parseFloat(arrBillDetail.get(position).getDiscountRate())>0){
            holder.tv_discount.setText(arrBillDetail.get(position).getDiscountRate()+"%");
            holder.tv_discount.setVisibility(View.VISIBLE);
        }else {
            holder.tv_discount.setVisibility(View.INVISIBLE);
        }
        holder.tv_qty.setText("Qty: "+arrBillDetail.get(position).getQuantity());
        holder.tv_size.setText("Size: "+arrBillDetail.get(position).getSize());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBillDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView tv_product_name, tv_price, tv_discount, tv_qty, tv_size;
        ImageView img_rate;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            img_product = itemView.findViewById(R.id.img_rv_order_detail);
            tv_product_name = itemView.findViewById(R.id.tv_product_name_order);
            tv_price = itemView.findViewById(R.id.tv_product_price_order);
            tv_discount = itemView.findViewById(R.id.tv_product_discount_order);
            tv_qty = itemView.findViewById(R.id.tv_qty_order);
            tv_size = itemView.findViewById(R.id.tv_size_order);
            img_rate = itemView.findViewById(R.id.img_thumb_up);
        }
    }
    public OnClickItemListener onItemClick;
    public void OnItemClickListener(OnClickItemListener onItemClick){
        this.onItemClick = onItemClick;
    }
    public interface OnClickItemListener{
        void OnRateClick(int position);
        void OnItemClick(int position);
    }
}
