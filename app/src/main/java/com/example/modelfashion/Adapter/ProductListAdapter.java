package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;
import com.example.modelfashion.customview.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    Context context;
    ArrayList<String> arrProductType;
    ArrayList<MyProduct> arrProduct;

    public ProductListAdapter() {
    }

    public ProductListAdapter(Context context, ArrayList<String> arrProductType, ArrayList<MyProduct> arrProduct){
        this.context = context;
        this.arrProductType = arrProductType;
        this.arrProduct = arrProduct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_product_items_layout,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        ArrayList<MyProduct> productsFiltered = new ArrayList<>();
        for(int j = 0; j < arrProduct.size(); j++){
            if(arrProduct.get(j).getType().equals(arrProductType.get(i))){
                productsFiltered.add(arrProduct.get(j));
            }
        }
        ProductAdapter productAdapter = new ProductAdapter(context,productsFiltered);
        viewHolder.recyclerView.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        viewHolder.tv_product_item.setText(arrProductType.get(i));
        viewHolder.recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));
        viewHolder.tv_xem_tat_ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.seeAll(i, arrProductType.get(i));
            }
        });
        productAdapter.onItemClickListener(new ProductAdapter.OnItemClick() {
            @Override
            public void imgClick(int position, MyProduct product) {
                onItemClick.imgClick(position, product);
            }

            @Override
            public void imgAddToFavoriteClick(int position, MyProduct product) {
                onItemClick.imgAddToCartClick(position, product);
            }

            @Override
            public void imgRemoveFavorite(int position, MyProduct product) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrProductType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        View view;
        TextView tv_product_item, tv_xem_tat_ca;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.recyclerView = itemView.findViewById(R.id.rv_product_item);
            tv_product_item = itemView.findViewById(R.id.tv_product_item);
            tv_xem_tat_ca = itemView.findViewById(R.id.tv_xem_tat_ca);
        }
    }

    private OnItemClickListener onItemClick;

    public void onItemClickListener(OnItemClickListener listener){
        this.onItemClick = listener;
    }

    public interface OnItemClickListener{
        void imgClick(int position, MyProduct product);
        void imgAddToCartClick(int position, MyProduct product);
        void seeAll(int position, String type);
    }
}
