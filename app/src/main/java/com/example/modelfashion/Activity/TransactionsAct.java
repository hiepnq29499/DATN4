package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Adapter.OrderHistoryAdapter;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.bill.Bill;
import com.example.modelfashion.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsAct extends AppCompatActivity {
    ImageView img_fav, img_fill_price;
    LinearLayout ll_fill_price;
    RecyclerView rv_order;
    Spinner spn_month, spn_year, spn_status;
    TextView tv_fill_price;
    ArrayList<String> arr_month = new ArrayList<>(Arrays.asList("Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"));
    ArrayList<String> getArr_month_values = new ArrayList<>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12"));
    ArrayList<String> arr_year = new ArrayList<>(Arrays.asList("2022", "2023", "2024"));
    ArrayList<String> arr_status = new ArrayList<>(Arrays.asList("Đã giao", "Chờ duyệt", "Đang giao", "Chờ hủy", "Đã hủy"));
    String fill_price = "increase";
    String user_id = "";
    String month = "01";
    String year = "2022";
    String status = "Đã giao";
    ArrayList<Bill> arr_bill = new ArrayList<>();
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        img_fav = findViewById(R.id.img_fav_trans_act);
        rv_order = findViewById(R.id.rv_order);
        spn_month = findViewById(R.id.spn_fil_month);
        spn_year = findViewById(R.id.spn_fill_year);
        spn_status = findViewById(R.id.spn_fill_status);
        img_fill_price = findViewById(R.id.img_fill_price);
        tv_fill_price = findViewById(R.id.tv_fill_price);
        ll_fill_price = findViewById(R.id.ll_fill_price);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        ArrayAdapter fill_month_adpater = new ArrayAdapter(TransactionsAct.this, R.layout.support_simple_spinner_dropdown_item, arr_month);
        spn_month.setAdapter(fill_month_adpater);
        ArrayAdapter fill_year_adpater = new ArrayAdapter(TransactionsAct.this, R.layout.support_simple_spinner_dropdown_item, arr_year);
        spn_year.setAdapter(fill_year_adpater);
        ArrayAdapter fill_status_adpater = new ArrayAdapter(TransactionsAct.this, R.layout.support_simple_spinner_dropdown_item, arr_status);
        spn_status.setAdapter(fill_status_adpater);
        BillFilter(month, year, status, fill_price);

        orderHistoryAdapter = new OrderHistoryAdapter(TransactionsAct.this, arr_bill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TransactionsAct.this, LinearLayoutManager.VERTICAL, false);
        rv_order.setLayoutManager(linearLayoutManager);
        rv_order.setAdapter(orderHistoryAdapter);
        SetListener();
        orderHistoryAdapter.OnItemClickListener(new OrderHistoryAdapter.OnClickItemListener() {
            @Override
            public void OnViewClick(int position) {
                Intent intent = new Intent(TransactionsAct.this, OrderDetailAct.class);
                intent.putExtra("bill", arr_bill.get(position));
                intent.putExtra("arr_bill_detail", arr_bill.get(position).getBillDetail());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
    }

//    private void SetData() {
//        synchronized (ApiRetrofit.apiRetrofit) {
//            ProgressDialog progressDialog = new ProgressDialog(TransactionsAct.this);
//            progressDialog.setMessage("Đang tải hóa đơn");
//            progressDialog.show();
//            ApiRetrofit.apiRetrofit.GetBillByUserId(user_id, year + "-" + month + "-01", status).enqueue(new Callback<ArrayList<Bill>>() {
//                @Override
//                public void onResponse(Call<ArrayList<Bill>> call, Response<ArrayList<Bill>> response) {
//                    progressDialog.hide();
//                    arr_bill = response.body();
//                    orderHistoryAdapter = new OrderHistoryAdapter(TransactionsAct.this, arr_bill);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TransactionsAct.this, LinearLayoutManager.VERTICAL, false);
//                    rv_order.setLayoutManager(linearLayoutManager);
//                    rv_order.setAdapter(orderHistoryAdapter);
//                    SetListener();
//                    orderHistoryAdapter.OnItemClickListener(new OrderHistoryAdapter.OnClickItemListener() {
//                        @Override
//                        public void OnViewClick(int position) {
//                            Intent intent = new Intent(TransactionsAct.this, OrderDetailAct.class);
//                            intent.putExtra("bill", arr_bill.get(position));
//                            intent.putExtra("arr_bill_detail", arr_bill.get(position).getBillDetail());
//                            intent.putExtra("user_id", user_id);
//                            startActivity(intent);
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<Bill>> call, Throwable t) {
//                    progressDialog.hide();
//                    Toast.makeText(getApplicationContext(), "Get data error", Toast.LENGTH_SHORT).show();
//                    t.printStackTrace();
//                    try {
//                        Log.i("GetBillByUserId", String.valueOf(call));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    }

    private void SetListener() {
        ll_fill_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fill_price.equalsIgnoreCase("increase")) {
                    img_fill_price.setImageResource(R.drawable.ic_sort_decrease);
                    tv_fill_price.setText("Giá trị giảm dần");
                    fill_price = "decrease";
                    Log.e("fill_price", fill_price);
                    BillFilter(month, year, status, fill_price);
                } else {
                    img_fill_price.setImageResource(R.drawable.ic_sort_increase);
                    tv_fill_price.setText("Giá trị tăng dần");
                    fill_price = "increase";
                    Log.e("fill_price", fill_price);
                    BillFilter(month, year, status, fill_price);
                }
            }
        });
        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = getArr_month_values.get(i);
                BillFilter(month, year, status, fill_price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month = "01";
            }
        });
        spn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = arr_year.get(i);
                BillFilter(month, year, status, fill_price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year = "2022";
            }
        });
        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = arr_status.get(i);
                BillFilter(month, year, status, fill_price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                status = "Đã giao";
            }
        });
    }

    private void BillFilter(String month, String year, String status, String orderby) {
        ProgressDialog progressDialog = new ProgressDialog(TransactionsAct.this);
        progressDialog.setMessage("Đang tải hóa đơn");
        progressDialog.show();
        ApiRetrofit.apiRetrofit.GetBillFilted(user_id, year + "-" + month + "-01", status, orderby).enqueue(new Callback<ArrayList<Bill>>() {
            @Override
            public void onResponse(Call<ArrayList<Bill>> call, Response<ArrayList<Bill>> response) {
                progressDialog.hide();
                arr_bill.removeAll(arr_bill);
                arr_bill.addAll(response.body());
                orderHistoryAdapter.notifyDataSetChanged();
                Log.e("filter", response.body().size() + "");
            }

            @Override
            public void onFailure(Call<ArrayList<Bill>> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "Get data error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                try {
                    Log.i("GetBillByUserId", String.valueOf(call));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
