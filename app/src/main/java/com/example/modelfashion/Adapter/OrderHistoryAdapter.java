package com.example.modelfashion.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfashion.Model.response.bill.Bill;
import com.example.modelfashion.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Bill> arrBill = new ArrayList<>();
    public OrderHistoryAdapter(Context context, ArrayList<Bill> arrBill){
        this.context = context;
        this.arrBill = arrBill;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String money_format = formatter.format(Integer.parseInt(arrBill.get(position).getAmount().split("\\.")[0]));
        holder.tv_bill_id.setText("Đơn hàng: "+arrBill.get(position).getBillId());
        holder.tv_date_created.setText("Ngày tạo: "+arrBill.get(position).getDateCreated());
        holder.tv_bill_total.setText("Tổng: "+money_format+" VNĐ");
        holder.tv_status.setText(arrBill.get(position).getStatus());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.OnViewClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBill.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bill_id, tv_date_created, tv_bill_total, tv_status;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_bill_id = itemView.findViewById(R.id.tv_bill_id);
            tv_date_created = itemView.findViewById(R.id.tv_date_created);
            tv_bill_total = itemView.findViewById(R.id.tv_bill_total);
            tv_status = itemView.findViewById(R.id.tv_status_order);
        }
    }
    public OnClickItemListener onItemClick;
    public void OnItemClickListener(OnClickItemListener onItemClick){
        this.onItemClick = onItemClick;
    }
    public interface OnClickItemListener{
        void OnViewClick(int position);
    }
}
