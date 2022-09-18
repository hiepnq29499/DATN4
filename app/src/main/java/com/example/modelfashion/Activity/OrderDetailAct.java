package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Adapter.OrderDetailAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.bill.Bill;
import com.example.modelfashion.Model.response.bill.BillDetail;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailAct extends AppCompatActivity {
    String user_id = "";
    int sodt = Integer.parseInt(null);
    Bill bill;
    RecyclerView rv_order;
    TextView tv_receiver, tv_address, tv_contact, tv_total, tv_cancel_order;
    ArrayList<BillDetail> arr_bill_detail = new ArrayList<>();
    String score = "1.0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        arr_bill_detail = intent.getParcelableArrayListExtra("arr_bill_detail");
        bill = intent.getParcelableExtra("bill");
        rv_order = findViewById(R.id.rv_order_detail);
        tv_receiver = findViewById(R.id.tv_receiver_order);
        tv_address = findViewById(R.id.tv_address_order);
        tv_contact = findViewById(R.id.tv_contact_order);
        tv_total = findViewById(R.id.tv_total_order);
        tv_cancel_order = findViewById(R.id.tv_cancel_order);
        SetData();
    }
    private void SetData(){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String money_format = formatter.format(Integer.parseInt(bill.getAmount().split("\\.")[0]));
        tv_receiver.setText("Receiver: "+bill.getReceiverName());
        tv_address.setText("Address: "+bill.getStreetAddress()+","+bill.getCity());
        tv_contact.setText("Contact: "+bill.getContact());
        tv_total.setText("Total: "+money_format+" VNĐ");
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(OrderDetailAct.this, arr_bill_detail, bill.getStatus());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailAct.this, LinearLayoutManager.VERTICAL, false);
        rv_order.setLayoutManager(linearLayoutManager);
        rv_order.setAdapter(orderDetailAdapter);
        orderDetailAdapter.OnItemClickListener(new OrderDetailAdapter.OnClickItemListener() {
            @Override
            public void OnRateClick(int position) {
                Dialog dialog = new Dialog(OrderDetailAct.this);
                dialog.setContentView(R.layout.rating_product_layout);
                dialog.setCancelable(true);
                ImageView img_1_score = dialog.findViewById(R.id.img_1_score);
                ImageView img_2_score = dialog.findViewById(R.id.img_2_score);
                ImageView img_3_score = dialog.findViewById(R.id.img_3_score);
                ImageView img_4_score = dialog.findViewById(R.id.img_4_score);
                ImageView img_5_score = dialog.findViewById(R.id.img_5_score);
                EditText edt_comment = dialog.findViewById(R.id.edt_comment);
                Button btn_confirm = dialog.findViewById(R.id.btn_confirm_rating);
                ArrayList<ImageView> arr_star = new ArrayList<>(Arrays.asList(img_1_score,img_2_score,img_3_score,img_4_score,img_5_score));
                setScore("1.0",arr_star,img_1_score);
                setScore("2.0",arr_star,img_2_score);
                setScore("3.0",arr_star,img_3_score);
                setScore("4.0",arr_star,img_4_score);
                setScore("5.0",arr_star,img_5_score);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiRetrofit.apiRetrofit.UpdateRating(user_id,arr_bill_detail.get(position).getProductId(),score,edt_comment.getText().toString()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e("rating", response.body());
                                Toast.makeText(OrderDetailAct.this, "Cảm ơn bạn đã đánh giá sản phẩm của chúng tôi", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                });
                dialog.show();
            }

            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(OrderDetailAct.this,NewProductDetailAct.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra(Constants.KEY_PRODUCT_ID,arr_bill_detail.get(position).getProductId());
                startActivity(intent);
            }
        });
        if(!bill.getStatus().equals("Chờ duyệt")){
            tv_cancel_order.setVisibility(View.INVISIBLE);
        }else {
            tv_cancel_order.setVisibility(View.VISIBLE);
        }
        tv_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiRetrofit.apiRetrofit.UpdateCancelBill(bill.getBillId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().equals("ok")){
                            Toast.makeText(OrderDetailAct.this, "Đơn hàng của bạn đang được chờ hủy", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(OrderDetailAct.this, "Đơn hàng của bạn hiện không thể hủy", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }
    private void setScore(String score1, ArrayList<ImageView> arrImg, ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score = score1;
                for(int i=0;i<Integer.parseInt(score1.split("\\.")[0]);i++){
                    arrImg.get(i).setImageResource(R.drawable.ic_star_gold);
                }
                for(int i=4;i>Integer.parseInt(score1.split("\\.")[0])-1;i--){
                    arrImg.get(i).setImageResource(R.drawable.ic_star_unrate);
                }
                Log.e("rating",score);
            }
        });
    }
}