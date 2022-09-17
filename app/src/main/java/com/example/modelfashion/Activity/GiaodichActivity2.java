package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.modelfashion.Model.response.bill.BillDetail;
import com.example.modelfashion.Model.response.my_product.MyProduct;
import com.example.modelfashion.R;

import java.util.ArrayList;

public class GiaodichActivity2 extends AppCompatActivity {
    TextView tv_dh_detail_history,phoneNumber_detail_history,address_detail_history,
            date_detail_history,summoney_detail_history,title_date_detail_history;
    //    List<ProductHistory> list;
    ListView lv_detail_history;
    ImageView back_detail_history;
    String bill_id, user_id, date_shipped, amount,status;
    ArrayList<BillDetail> arr_bill_detail = new ArrayList<>();
    ArrayList<MyProduct> arr_my_product = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giaodich2);
        tv_dh_detail_history = findViewById(R.id.tv_dh_detail_history);
        phoneNumber_detail_history = findViewById(R.id.phoneNumber_detail_history);
        address_detail_history = findViewById(R.id.address_detail_history);
        date_detail_history = findViewById(R.id.date_detail_history);
        summoney_detail_history = findViewById(R.id.summoney_detail_history);
        title_date_detail_history = findViewById(R.id.title_date_detail_history);
        lv_detail_history = findViewById(R.id.lv_detail_history);
        back_detail_history = findViewById(R.id.back_detail_history);
    }
}