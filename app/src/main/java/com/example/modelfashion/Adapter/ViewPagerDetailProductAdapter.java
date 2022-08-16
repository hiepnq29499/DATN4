package com.example.modelfashion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Model.ItemSaleMain;
import com.example.modelfashion.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerDetailProductAdapter extends RecyclerView.Adapter<ViewPagerDetailProductAdapter.ViewHolder> {

    private ArrayList<String> arrItem = new ArrayList<>();

    public void setArrItem(ArrayList<String> arrItem) {
        this.arrItem.clear();
        this.arrItem = arrItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_detail_image_view_pager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = arrItem.get(position);
//        Picasso.get().load(url).into(holder.img);
        Glide.with(holder.img.getContext()).load(url).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_product_detail_view_pager_item);
        }
    }
}
