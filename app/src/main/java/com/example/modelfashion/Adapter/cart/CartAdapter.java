package com.example.modelfashion.Adapter.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.Model.response.my_product.Sizes;
import com.example.modelfashion.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHoder> {
    private ArrayList<MyProduct> productArrayList;
    private ArrayList<Sizes> arrSize;
    private Context context;
    private CartOnClick cartOnClick;
    public CartAdapter(ArrayList<MyProduct> productArrayList, ArrayList<Sizes> arrSize, Context context) {
        this.productArrayList = productArrayList;
        this.arrSize = arrSize;
        this.context =context;
    }

    public void setOnClick(CartOnClick cartOnClick){
        this.cartOnClick = cartOnClick;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        private TextView nameProduct, priceProduct, sizeProduct, amount, delete;
        private Button btnIncrease, btnDecrease;
        private ImageView imgCart;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgCart = itemView.findViewById(R.id.img_cart);
            nameProduct = itemView.findViewById(R.id.tv_name_product);
            priceProduct = itemView.findViewById(R.id.tv_price_product);
            sizeProduct = itemView.findViewById(R.id.tv_size_product);
            amount = itemView.findViewById(R.id.tv_amount);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            delete = itemView.findViewById(R.id.tv_delete);
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHoder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        AtomicInteger minteger= new AtomicInteger(Integer.parseInt(arrSize.get(position).getRemain_product()));
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String money_format = formatter.format(Integer.parseInt(productArrayList.get(position).getPrice()));
        holder.nameProduct.setText("Sản phẩm: "+productArrayList.get(position).getProduct_name());
        holder.priceProduct.setText("Giá: "+money_format+" VNĐ");
        holder.sizeProduct.setText("Size: "+arrSize.get(position).getSize());
        Glide.with(context).load(productArrayList.get(position).getPhotos().get(2)).into(holder.imgCart);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartOnClick.OnClick(position, arrSize.get(position).getSize_id());
            }
        });
        holder.btnIncrease.setOnClickListener(view -> {
            minteger.set(minteger.get() + 1);
                holder.amount.setText(""+minteger);
                cartOnClick.ChangeQuantity(arrSize.get(position).getSize_id(), holder.amount.getText().toString(), holder.btnIncrease);
        });
        holder.btnDecrease.setOnClickListener(view -> {
            if(minteger.get() > 1){
                minteger.set(minteger.get() - 1);
                holder.amount.setText(""+minteger);
                cartOnClick.ChangeQuantity(arrSize.get(position).getSize_id(), holder.amount.getText().toString(), holder.btnDecrease);
            }
        });
        holder.amount.setText(""+minteger);
    }



    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public interface CartOnClick{
        void OnClick(int position, String size_id);
        void ChangeQuantity(String size_id, String quantity, Button btn);
    }
}
