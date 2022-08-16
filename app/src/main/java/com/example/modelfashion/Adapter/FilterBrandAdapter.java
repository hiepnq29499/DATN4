package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfashion.Model.Brand;
import com.example.modelfashion.R;

import java.util.ArrayList;

public class FilterBrandAdapter extends RecyclerView.Adapter<FilterBrandAdapter.ViewHolder> {
    ArrayList<String> arrBrandLocation = new ArrayList<>();
    Context context;
    Boolean checkAll;
    public FilterBrandAdapter(Context context, ArrayList<String> arrBrandLocation, Boolean checkAll){
        this.arrBrandLocation = arrBrandLocation;
        this.context = context;
        this.checkAll = checkAll;
    }
    public void OnCheckChange(Boolean checkAll){
        this.checkAll = checkAll;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_brand_layout_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.cb.setText(arrBrandLocation.get(position));
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    onItemClick.onCheckTrue(position);
                }else {
                    onItemClick.onCheckFalse(position);
                }
            }
        });
        if(checkAll){
            holder.cb.setChecked(true);
        }else {
            holder.cb.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrBrandLocation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb_filter_brand);
        }
    }
    public OnClickItemListener onItemClick;
    public void OnItemClickListener(OnClickItemListener onItemClick){
        this.onItemClick = onItemClick;
    }
    public interface OnClickItemListener{
        void onCheckTrue(int position);
        void onCheckFalse(int position);
    }
}
