package com.example.modelfashion.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Activity.SignIn.SignInActivity;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.R;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPasswordAct extends AppCompatActivity {
    String user_name = "";
    EditText edt_confirm_code, edt_new_pw, edt_re_pw;
    Button btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        edt_confirm_code = findViewById(R.id.edt_confirm_code1);
        edt_new_pw = findViewById(R.id.edt_new_pw);
        edt_re_pw = findViewById(R.id.edt_re_pw);
        btn_confirm = findViewById(R.id.btn_confirm_get_pw);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validate()){
                    if(edt_new_pw.getText().toString().equals(edt_re_pw.getText().toString())){
                        ApiRetrofit.apiRetrofit.UpdateNewPw(edt_confirm_code.getText().toString(),edt_new_pw.getText().toString(),user_name).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e("get_pw", response.body());
                                if(response.body().equals("ok")){
                                    Toast.makeText(GetPasswordAct.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(GetPasswordAct.this, SignInActivity.class);
                                    startActivity(intent1);
                                }else {
                                    Toast.makeText(GetPasswordAct.this, "Mã xác nhận chưa chính xác", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }else {
                        Toast.makeText(GetPasswordAct.this, "Xác nhận mật khẩu chưa trùng khớp", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private Boolean Validate(){
        if(edt_re_pw.getText().toString().length() < 8){
            Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(edt_new_pw.getText().toString().length() < 8){
            Toast.makeText(this, "Mật khẩu phải có ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}