package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    ArrayList<MyProduct> arrProduct;

    public ProductAdapter(Context context, ArrayList<MyProduct> arrProduct) {
        this.context = context;
        this.arrProduct = arrProduct;
    }

    public MyProduct getProduct(int position) {
        return arrProduct.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {

        viewHolder.img.setOnClickListener(view -> {
            onItemClick.imgClick(i, arrProduct.get(i));
        });
        viewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClick.imgRemoveFavorite(i,arrProduct.get(i));
                return false;
            }
        });
        viewHolder.imgAddToCart.setOnClickListener(view -> {
            onItemClick.imgAddToFavoriteClick(i, arrProduct.get(i));
        });
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        if(Float.parseFloat(arrProduct.get(i).getDiscount_rate())>0){
            String money_format = formatter.format(Integer.parseInt(arrProduct.get(i).getPrice().split("\\.")[0])*(100-Float.parseFloat(arrProduct.get(i).getDiscount_rate()))/100);
            viewHolder.tvPrice.setText(money_format+" VNĐ");
        }else {
            String money_format = formatter.format(Integer.parseInt(arrProduct.get(i).getPrice().split("\\.")[0]));
            viewHolder.tvPrice.setText(money_format+" VNĐ");
        }
        String money_format = formatter.format(Integer.parseInt(arrProduct.get(i).getPrice().split("\\.")[0]));
        Log.e("load_img",arrProduct.get(i).getPhotos().get(0));
        Glide.with(context).load(arrProduct.get(i).getPhotos().get(0).replace("localhost", Constants.KEY_IP)).into(viewHolder.img);
        viewHolder.tvProductName.setText(arrProduct.get(i).getProduct_name());
    }

    @Override
    public int getItemCount() {
        return arrProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageButton imgAddToCart;
        TextView tvProductName, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_product_main_avatar);
            tvProductName = itemView.findViewById(R.id.tv_my_product_name);
            tvPrice = itemView.findViewById(R.id.tv_my_product_price);
            imgAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }

    private OnItemClick onItemClick;

    public void onItemClickListener(OnItemClick listener) {
        this.onItemClick = listener;
    }

    public interface OnItemClick {
        void imgClick(int position, MyProduct product);
        void imgAddToFavoriteClick(int position, MyProduct product);
        void imgRemoveFavorite(int position, MyProduct product);
    }
}
