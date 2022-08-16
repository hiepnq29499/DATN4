package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.DeliveryInfo;
import com.example.modelfashion.R;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddressAct extends AppCompatActivity {
    LinearLayout ll_open_map;
    TextView tv_save;
    EditText edt_receiver_name, edt_contact, edt_street, edt_city;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        ll_open_map = findViewById(R.id.ll_open_map);
        tv_save = findViewById(R.id.tv_save);
        edt_receiver_name = findViewById(R.id.edt_receiver_name);
        edt_contact = findViewById(R.id.edt_contact);
        edt_street = findViewById(R.id.edt_street_address);
        edt_city = findViewById(R.id.edt_city);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        if(user_id!=null){
            SetData(user_id);
        }
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id!=null){
                    SaveDeliveryAddress();
                }else {
                    Toast.makeText(DeliveryAddressAct.this, "Chưa tiến hành đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void SetData(String user_id){
        ApiRetrofit.apiRetrofit.GetDeliveryInfo(user_id).enqueue(new Callback<DeliveryInfo>() {
            @Override
            public void onResponse(Call<DeliveryInfo> call, Response<DeliveryInfo> response) {
                DeliveryInfo deliveryInfo = response.body();
                if(!deliveryInfo.getDelivery_id().equalsIgnoreCase("null")){
                    edt_receiver_name.setText(deliveryInfo.getReceiver_name());
                    edt_contact.setText(deliveryInfo.getContact());
                    edt_street.setText(deliveryInfo.getStreet_address());
                    edt_city.setText(deliveryInfo.getCity());
                }
            }

            @Override
            public void onFailure(Call<DeliveryInfo> call, Throwable t) {

            }
        });
    }
    private void SaveDeliveryAddress(){
            if(Validate() == true){
                ApiRetrofit.apiRetrofit.SaveDeliveryInfo(user_id, edt_receiver_name.getText().toString(), edt_street.getText().toString(), edt_city.getText().toString(), edt_contact.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().equalsIgnoreCase("insert ok")||response.body().equalsIgnoreCase("update ok")){
                            Toast.makeText(DeliveryAddressAct.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DeliveryAddressAct.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                            Log.e("delivery", response.body()+"");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }else {
                Toast.makeText(this, "Không để trống thông tin", Toast.LENGTH_SHORT).show();
            }

    }
    private boolean Validate(){
        if(edt_receiver_name.getText().toString().isEmpty()||edt_contact.getText().toString().isEmpty()||edt_street.getText().toString().isEmpty()||edt_city.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}