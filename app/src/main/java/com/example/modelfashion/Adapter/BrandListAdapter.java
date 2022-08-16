package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.util.ArrayList;

public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.ViewHolder> {
    ArrayList<Brand> arrBrand = new ArrayList<>();
    Context context;
    public BrandListAdapter(Context context, ArrayList<Brand> arrBrand){
        this.context = context;
        this.arrBrand = arrBrand;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(arrBrand.get(position).getLogo().replace("localhost", Constants.KEY_IP)).into(holder.imgBrand);
        holder.imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItemListener.OnClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBrand.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBrand;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBrand = itemView.findViewById(R.id.img_brand);
        }
    }
    public OnClickItemListener onClickItemListener;
    public void SetOnItemClickListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
    public interface OnClickItemListener{
        void OnClickItem(int position);
    }
}
