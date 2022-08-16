package com.example.modelfashion.Activity.SignIn;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modelfashion.Activity.GetPasswordAct;
import com.example.modelfashion.Activity.MainActivity;
import com.example.modelfashion.Common.ProgressLoadingCommon;
import com.example.modelfashion.Fragment.FragmentProfile;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;
import com.example.modelfashion.network.ApiClient;
import com.example.modelfashion.network.ApiInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    int RC_SIGN_IN = 0;
//    ImageView btn_back;
//    EditText edtAccount, edtPassword;
//    Button btnLogin;
//    TextView tvSignUp;
    EditText edtUname, edtPw;
    TextView tvGetPw, tvSignUp, tvLogin;
    CheckBox cbSaveValue;
    SharedPreferences sharedPreferences;
    ApiInterface apiInterface;
//    ProgressLoadingCommon progressLoadingCommon;
    String fcmToken;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_2);
        viewHolder();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        setListener();
//        getPreferencesData();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            String email = account.getEmail();
            ApiRetrofit.apiRetrofit.LoginWithGoogle(email).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            fcmToken = task.getResult();
                            Log.e("token", fcmToken);
                            Log.e("active_status",response.body().getActiveStatus()+email);
                            if (response.body().getActiveStatus().equals("active")) {
                                InsertToken(response.body().getId(),fcmToken);
                                if (cbSaveValue.isChecked()) {
                                    Boolean aBoolean = cbSaveValue.isChecked();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("account", edtUname.getText().toString());
                                    editor.putString("password", edtPw.getText().toString());
                                    editor.putBoolean("checkbox", aBoolean);
                                    editor.apply();
                                } else {
                                    sharedPreferences.edit().clear().apply();
                                }
                                SharedPreferences sharedPreferences = getSharedPreferences(Constants.KEY_SAVE_USER,MODE_MULTI_PROCESS);
                                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                prefsEditor.putString(Constants.KEY_GET_USER, response.body().toString());
                                prefsEditor.putBoolean(Constants.KEY_CHECK_LOGIN, true);
                                prefsEditor.apply();
                                Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                            onBackPressed();
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                GoogleSignOut();
                            }
                        }
                    });
//                    Intent intent = new Intent(SignInActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
            Log.e("google_login","Success");

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("google_login", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    // tham chiếu
    private void viewHolder() {
//        btn_back = findViewById(R.id.btn_signIn_back);
//        edtAccount = findViewById(R.id.edtAccount);
//        edtPassword = findViewById(R.id.edtPassword);
//        btnLogin = findViewById(R.id.btnLogin);
//        tvSignUp = findViewById(R.id.tvSignUp);
        edtUname = findViewById(R.id.edt_uname);
        edtPw = findViewById(R.id.edt_pw);
        tvGetPw = findViewById(R.id.tv_get_pw);
        tvLogin = findViewById(R.id.tv_login);
        tvSignUp = findViewById(R.id.tv_signup);
        cbSaveValue = findViewById(R.id.cbSaveValue);
        signInButton = findViewById(R.id.sign_in_button);
        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        apiInterface = ApiClient.provideApiInterface(SignInActivity.this);
//        progressLoadingCommon = new ProgressLoadingCommon();
    }

    private Boolean validate() {
        Pattern special = Pattern.compile("[!#$%&*^()_+=|<>?{}\\[\\]~-]");
        if (edtUname.getText().toString().isEmpty() || edtPw.getText().toString().isEmpty()) {
            Toast.makeText(SignInActivity.this, "Không để trống email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (special.matcher(edtUname.getText().toString()).find() || special.matcher(edtPw.getText().toString()).find()) {
            Toast.makeText(SignInActivity.this, "Không được viết kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtPw.getText().toString().length() < 8) {
            Toast.makeText(SignInActivity.this, "Mật khẩu ít nhất 8 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //thêm chức năng vào các nút bấm
    private void setListener() {
//        btn_back.setOnClickListener(v -> {
//            onBackPressed();
//        });
        tvGetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setCancelable(true);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_get_pw,null,false);
                builder.setView(view1);
                EditText edtUserName = view1.findViewById(R.id.edt_username_get_pw);
                Button btnSendRequest = view1.findViewById(R.id.btn_send_request_pw);
                btnSendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApiRetrofit.apiRetrofit.SendRequestGetPw(edtUserName.getText().toString()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equalsIgnoreCase("ok")){
                                    Intent intent = new Intent(SignInActivity.this, GetPasswordAct.class);
                                    intent.putExtra("user_name",edtUserName.getText().toString());
                                    startActivity(intent);
                                }
                                if(response.body().equalsIgnoreCase("fail")){
                                    Toast.makeText(SignInActivity.this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                    }
                });
                builder.create().show();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
//                    progressLoadingCommon.showProgressLoading(SignInActivity.this);
                    checkLogin();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void checkLogin() {
        ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setMessage("Pending");
        progressDialog.show();
        apiInterface.checkLogin(edtUname.getText().toString(), edtPw.getText().toString())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        progressDialog.hide();
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                fcmToken = task.getResult();
                                Log.e("token", fcmToken);
                                if (response.body().getActiveStatus().equals("active")) {
                                    InsertToken(response.body().getId(),fcmToken);
                                    if (cbSaveValue.isChecked()) {
                                        Boolean aBoolean = cbSaveValue.isChecked();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("account", edtUname.getText().toString());
                                        editor.putString("password", edtPw.getText().toString());
                                        editor.putBoolean("checkbox", aBoolean);
                                        editor.apply();
                                    } else {
                                        sharedPreferences.edit().clear().apply();
                                    }

                                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.KEY_SAVE_USER,MODE_MULTI_PROCESS);
                                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                    prefsEditor.putString(Constants.KEY_GET_USER, response.body().toString());
                                    prefsEditor.putBoolean(Constants.KEY_CHECK_LOGIN, true);
                                    prefsEditor.apply();
                                    Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                            onBackPressed();
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignInActivity.this, response.body()+"", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressDialog.hide();
                        Log.e("DangNhap",t.toString());
                        Toast.makeText(SignInActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //chặn back
    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs
    }

    public void getPreferencesData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
//        if (sharedPreferences.contains("account")) {
//            String user = sharedPreferences.getString("account", "not found.");
//            edtAccount.setText(user);
//        }
//        if (sharedPreferences.contains("password")) {
//            String pass = sharedPreferences.getString("password", "not found.");
//            edtPassword.setText(pass);
//        }
//        if (sharedPreferences.contains("checkbox")) {
//            Boolean check = sharedPreferences.getBoolean("checkbox", false);
//            cbSaveValue.setChecked(check);
//        }
    }

    private void InsertToken(String user_id, String token){
        ApiRetrofit.apiRetrofit.InsertFcmToken(user_id,token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("fcm","Insert token status : "+response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void GoogleSignOut(){
        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SignInActivity.this, "Tài khoản bị đình chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void DangKy(View view) {
//        Intent intent = new Intent(SignInActivity.this, SignUpTestAct.class);
//        startActivity(intent);
//    }

//    public void DangNhap(View view) {
//        tk = edt_tk.getText().toString();
//        mk = edt_mk.getText().toString();
//        ApiRetrofit.apiRetrofit.GetUser(tk,mk).enqueue(new Callback<ArrayList<User>>() {
//            @Override
//            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//                ArrayList<User> arrUser = response.body();
//                Toast.makeText(SignInActivity.this, response.body()+"", Toast.LENGTH_LONG).show();
//                if(arrUser.size()>0){
//                    Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
//                    Glide.with(SignInActivity.this).load(arrUser.get(0).getAvatar()).into(user_avatar);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
//
//            }
//        });
//    }
}