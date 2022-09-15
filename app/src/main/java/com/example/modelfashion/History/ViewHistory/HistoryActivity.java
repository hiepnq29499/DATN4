package com.example.modelfashion.History.ViewHistory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.History.ApdapterHistory.HistoryAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.bill.Bill;
import com.example.modelfashion.Model.response.bill.BillDetail;
import com.example.modelfashion.Model.response.my_product.MyProduct;

import com.example.modelfashion.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
//    public static List<ModelHistory> listModelHistory ;
//    private List<ModelHistory> listModelHistoryNew;
//    private List<ProductHistory> listProduct;
    private ArrayList<Bill> arr_bill = new ArrayList<>();
    private ArrayList<BillDetail> arr_bill_detail = new ArrayList<>();
    private ArrayList<String> arr_detail_id = new ArrayList<>();
    private ArrayList<MyProduct> arr_my_product = new ArrayList<>();
    private ListView lv_history;
    private ImageView img_history_back;
    private RelativeLayout rl_filter_history;
    private TextView tv_status_history;
    private int numberStatus = 4;
    private String user_id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lv_history = findViewById(R.id.lv_history);
        img_history_back = findViewById(R.id.img_history_back);
        rl_filter_history = findViewById(R.id.rl_filter_history);
        tv_status_history = findViewById(R.id.tv_status_history);
        Intent intent = getIntent();
        numberStatus = intent.getIntExtra("numberStatus",4);
        user_id = intent.getStringExtra("user_id");
        loadTitleStatus(numberStatus);
       // Toast.makeText(this, ""+user_id, Toast.LENGTH_SHORT).show();
        img_history_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_filter_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFilter();
            }
        });

//        listModelHistory = new ArrayList<>();
//        listModelHistoryNew = new ArrayList<>();
//        listProduct = new ArrayList<>();
//        fakeData();
        loadData(numberStatus);

    }


    private void loadData(int i){

        String status = "";
        switch (i) {
            case 1: status = "Đang chờ";
                    break;
            case 2: status = "Hoàn thành";
                    break;
            case 3: status = "Đang giao";
                    break;
            case 4: status = "Đã giao";
                    break;
        }
        ApiRetrofit.apiRetrofit.GetBillByUserId(user_id,"1900-01-01", status).enqueue(new Callback<ArrayList<Bill>>() {
            @Override
            public void onResponse(Call<ArrayList<Bill>> call, Response<ArrayList<Bill>> response) {
                arr_bill = response.body();
                for(int i = 0; i < arr_bill.size(); i++){
                    arr_detail_id.add(arr_bill.get(i).getBillDetail().get(0).getDetailId());
                    Log.e("check2",arr_bill.get(i).getBillDetail().get(0).getDetailId());
                }
                SetData(arr_detail_id);
            }

            @Override
            public void onFailure(Call<ArrayList<Bill>> call, Throwable t) {

            }
        });
//        getData(i);
//        if(i == 4){
//            HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this,listModelHistoryNew);
//            lv_history.setAdapter(historyAdapter);
//        }else {
//            OrderStatusAdapter historyAdapterOrder = new OrderStatusAdapter(HistoryActivity.this,listModelHistoryNew);
//            lv_history.setAdapter(historyAdapterOrder);
//        }
    }
    private void SetData(ArrayList<String> arr_detail_id){
        JSONArray jsonArray = new JSONArray(arr_detail_id);
        ApiRetrofit.apiRetrofit.GetProductByDetailId(jsonArray).enqueue(new Callback<ArrayList<MyProduct>>() {
            @Override
            public void onResponse(Call<ArrayList<MyProduct>> call, Response<ArrayList<MyProduct>> response) {
                arr_my_product = response.body();
                Log.e("Checkresponse",arr_my_product.size()+""+arr_bill.size());
                for (int i = 0; i< arr_my_product.size(); i++){
                    Log.e("check3",i+""+arr_my_product.get(i).getId());
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this, arr_bill, arr_my_product, user_id);
                lv_history.setAdapter(historyAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<MyProduct>> call, Throwable t) {

            }
        });
    }
//    private void getData(int index){
//        listModelHistoryNew.clear();
//        String newStatus = "";
//        if(index == 1){
//            newStatus = "Chờ xác nhận";
//        }else if(index == 2){
//            newStatus = "Hoàn thành";
//        }else if(index == 3){
//            newStatus = "Đang giao";
//        }else if(index == 4){
//            newStatus = "Lịch sử";
//        }
//        for (int i = 0;i<listModelHistory.size();i++){
//            if(listModelHistory.get(i).getmStatus().matches(newStatus)){
//                listModelHistoryNew.add(listModelHistory.get(i));
//            }
//        }
//
//    }

//    private void fakeData(){
//        listProduct.add(new ProductHistory("1","a1","10000","L","https://i.pinimg.com/originals/ce/a7/5e/cea75e472d431e283a4a622ed1d7b155.png","10"));
//        listProduct.add(new ProductHistory("2","a2","10000","L","https://i.pinimg.com/originals/ce/a7/5e/cea75e472d431e283a4a622ed1d7b155.png","10"));
//        listProduct.add(new ProductHistory("3","a3","10000","L","https://i.pinimg.com/originals/ce/a7/5e/cea75e472d431e283a4a622ed1d7b155.png","10"));
//
//        listModelHistory.add(new ModelHistory("HD1","0987563","ha noi","12/2 0:0",
//                "13/2 0:0","Chờ xác nhận","100000",listProduct));
//        listModelHistory.add(new ModelHistory("HD2","0987563","ha noi","12/2 0:0",
//                "13/2 0:0","Chờ lấy hàng","100000",listProduct));
//        listModelHistory.add(new ModelHistory("HD3","0987563","ha noi","12/2 0:0",
//                "13/2 0:0","Đang giao","100000",listProduct));
//        listModelHistory.add(new ModelHistory("HD4","0987563","ha noi","12/2 0:0",
//                "13/2 0:0","Đã giao","100000",listProduct));
//    }
    private int filter_number = 0;
    private void showDialogFilter(){
        Dialog dialog = new Dialog(HistoryActivity.this);
        dialog.setContentView(R.layout.dialog_filter_history);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView status1,status2,status3,status4;
        status1 = dialog.findViewById(R.id.status1);
        status2 = dialog.findViewById(R.id.status2);
        status3 = dialog.findViewById(R.id.status3);
        status4 = dialog.findViewById(R.id.status4);
        TextView filter_history_cancel,filter_history_ok;
        filter_history_cancel = dialog.findViewById(R.id.filter_history_cancel);
        filter_history_ok = dialog.findViewById(R.id.filter_history_ok);
        loadThemeFilterStatus(numberStatus,status1,status2,status3,status4);
        status1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_number = 1;
                loadThemeFilterStatus(1,status1,status2,status3,status4);
            }
        });
        status2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_number = 2;
                loadThemeFilterStatus(2,status1,status2,status3,status4);
            }
        });
        status3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_number = 3;
                loadThemeFilterStatus(3,status1,status2,status3,status4);
            }
        });
        status4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_number = 4;
                loadThemeFilterStatus(4,status1,status2,status3,status4);
            }
        });
        filter_history_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_number = 0;
                dialog.dismiss();
            }
        });
        filter_history_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberStatus = filter_number;
                loadThemeFilterStatus(numberStatus,status1,status2,status3,status4);
                loadTitleStatus(numberStatus);
                loadData(numberStatus);
                dialog.dismiss();

            }
        });

        dialog.show();
    }
    private void loadTitleStatus(int i){
        if (i == 1){
            tv_status_history.setText("Chờ xác nhận");
            tv_status_history.setTextColor(Color.parseColor("#FF0000"));
        }else if (i == 2){
            tv_status_history.setText("Hoàn thành");
            tv_status_history.setTextColor(Color.parseColor("#ff9800"));
        }
        else if (i == 3){
            tv_status_history.setText("Đang giao");
            tv_status_history.setTextColor(Color.parseColor("#4caf50"));
        }
        else if (i == 4){
            tv_status_history.setText("Đã giao");
            tv_status_history.setTextColor(Color.parseColor("#4caf50"));
        }
    }
    private void loadThemeFilterStatus(int i,TextView st1,TextView st2,TextView st3,TextView st4){
        if (i == 1){
            st1.setTextColor(Color.parseColor("#FFFFFF"));
            st1.setBackground(getDrawable(R.drawable.bt_filter_history_select));
            st2.setTextColor(Color.parseColor("#000000"));
            st2.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st3.setTextColor(Color.parseColor("#000000"));
            st3.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st4.setTextColor(Color.parseColor("#000000"));
            st4.setBackground(getDrawable(R.drawable.bt_item_filter_history));
        }else if (i == 2){
            st2.setTextColor(Color.parseColor("#FFFFFF"));
            st2.setBackground(getDrawable(R.drawable.bt_filter_history_select));
            st1.setTextColor(Color.parseColor("#000000"));
            st1.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st3.setTextColor(Color.parseColor("#000000"));
            st3.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st4.setTextColor(Color.parseColor("#000000"));
            st4.setBackground(getDrawable(R.drawable.bt_item_filter_history));
        }
        else if (i == 3){
            st3.setTextColor(Color.parseColor("#FFFFFF"));
            st3.setBackground(getDrawable(R.drawable.bt_filter_history_select));
            st1.setTextColor(Color.parseColor("#000000"));
            st1.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st2.setTextColor(Color.parseColor("#000000"));
            st2.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st4.setTextColor(Color.parseColor("#000000"));
            st4.setBackground(getDrawable(R.drawable.bt_item_filter_history));
        }
        else if (i == 4){
            st4.setTextColor(Color.parseColor("#FFFFFF"));
            st4.setBackground(getDrawable(R.drawable.bt_filter_history_select));
            st1.setTextColor(Color.parseColor("#000000"));
            st1.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st2.setTextColor(Color.parseColor("#000000"));
            st2.setBackground(getDrawable(R.drawable.bt_item_filter_history));
            st3.setTextColor(Color.parseColor("#000000"));
            st3.setBackground(getDrawable(R.drawable.bt_item_filter_history));
        }


    }
}
