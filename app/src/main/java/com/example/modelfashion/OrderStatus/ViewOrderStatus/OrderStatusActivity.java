package com.example.modelfashion.OrderStatus.ViewOrderStatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.modelfashion.Model.MHistory.ModelHistory;
import com.example.modelfashion.Model.MHistory.ProductHistory;
import com.example.modelfashion.History.ViewHistory.DetailHistoryActivity;
import com.example.modelfashion.OrderStatus.AdapterOrderStatus.OrderStatusAdapter;
import com.example.modelfashion.R;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {
    public static List<ModelHistory> listModelOrderStatus ;
    private List<ProductHistory> listProduct;
    private ListView lv_orderstatus;
    private ImageView img_orderstatus_back;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        img_orderstatus_back = findViewById(R.id.img_orderstatus_back);
        img_orderstatus_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listModelOrderStatus = new ArrayList<>();
        listProduct = new ArrayList<>();
        lv_orderstatus = findViewById(R.id.lv_orderstatus);
        fakeData();
        OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(OrderStatusActivity.this,listModelOrderStatus);
        lv_orderstatus.setAdapter(orderStatusAdapter);


    }


    private void fakeData(){

        listProduct.add(new ProductHistory("1","a1","10000","L","https://i.pinimg.com/originals/ce/a7/5e/cea75e472d431e283a4a622ed1d7b155.png","10"));
        listProduct.add(new ProductHistory("2","a2","10000","L","https://i.pinimg.com/originals/ce/a7/5e/cea75e472d431e283a4a622ed1d7b155.png","10"));
        listProduct.add(new ProductHistory("3","a3","10000","L","https://i.pinimg.com/originals/ce/a7/5e/cea75e472d431e283a4a622ed1d7b155.png","10"));

        listModelOrderStatus.add(new ModelHistory("HD1","0987563","ha noi","12/2 0:0",
                "","Đang Chờ","100000",listProduct));
        listModelOrderStatus.add(new ModelHistory("HD2","0987563","ha noi","12/2 0:0",
                "","Đang Giao","100000",listProduct));
        listModelOrderStatus.add(new ModelHistory("HD3","0987563","ha noi","12/2 0:0",
                "","Đang Giao","100000",listProduct));


    }
}