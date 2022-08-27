package com.example.modelfashion.Fragment;

import static android.content.Context.MODE_MULTI_PROCESS;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.modelfashion.Activity.MainActivity;
import com.example.modelfashion.Activity.SignIn.SignInActivity;
import com.example.modelfashion.Activity.SignIn.SignUpActivity;
import com.example.modelfashion.Activity.TransactionsAct;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProfileFragment extends Fragment {
    TextView tvProfileName1, tvProfileName2, tvProfileLogin, tvProfileRegister, tvProfileEmail, tvProfileFullname, tvProfilePhone, tvProfileGender, tvProfileLogout;
    Boolean isLogin;
    TextView tvChangeFullName, tvChangePhone, tvChangePw;
    String userId;
    GoogleSignInClient mGoogleSignInClient;
    Button btnTrans;
    String token;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_new, container, false);
        tvProfileName1 = view.findViewById(R.id.tv_profile_name1);
        tvProfileName2 = view.findViewById(R.id.tv_profile_name2);
        tvProfileLogin = view.findViewById(R.id.tv_profile_login);
        tvProfileRegister = view.findViewById(R.id.tv_profile_register);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        tvProfileFullname = view.findViewById(R.id.tv_profile_fullname);
        tvProfilePhone = view.findViewById(R.id.tv_profile_phone);
        tvProfileGender = view.findViewById(R.id.tv_profile_gender);
        tvProfileLogout = view.findViewById(R.id.tv_profile_logout);
        btnTrans = view.findViewById(R.id.btn_trans_profile);
        tvChangeFullName = view.findViewById(R.id.tv_profile_change_fullname);
        tvChangePhone = view.findViewById(R.id.tv_profile_change_phone);
        tvChangePw = view.findViewById(R.id.tv_profile_change_pw);
        Bundle info = getArguments();
        userId = info.getString("user_id");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                token = task.getResult();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        LoadDetail();
        SetListener();
        return view;
    }

    private void LoadDetail(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, MODE_MULTI_PROCESS);
        isLogin = sharedPreferences.getBoolean(Constants.KEY_CHECK_LOGIN, false);
        if (isLogin == false) {
            User user = new User("", "", "", "", "", "", "");
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString("user", user.toString());
            prefsEditor.apply();
            btnTrans.setVisibility(View.GONE);
            tvProfileName1.setVisibility(View.GONE);
            tvProfileName2.setVisibility(View.GONE);
            tvProfileFullname.setVisibility(View.GONE);
            tvProfileEmail.setVisibility(View.GONE);
            tvProfilePhone.setVisibility(View.GONE);
            tvProfileGender.setVisibility(View.GONE);
            tvChangePw.setVisibility(View.GONE);
            tvChangePhone.setVisibility(View.GONE);
            tvChangeFullName.setVisibility(View.GONE);
            tvProfileLogin.setVisibility(View.VISIBLE);
            tvProfileRegister.setVisibility(View.VISIBLE);
            tvProfileLogout.setVisibility(View.GONE);
        } else {
            if (sharedPreferences.contains(Constants.KEY_GET_USER)) {
                String userData = sharedPreferences.getString(Constants.KEY_GET_USER, "");
                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Constants.KEY_PHONE,Context.MODE_PRIVATE);
                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(Constants.KEY_FULL_NAME,Context.MODE_PRIVATE);
                try {
                    JSONObject obj = new JSONObject(userData);
                    if(obj.getString(Constants.KEY_ACCOUNT_TYPE).equals("google")){
                        tvChangePw.setVisibility(View.INVISIBLE);
                    }else {
                        tvChangePw.setVisibility(View.VISIBLE);
                    }
                    tvProfileName1.setVisibility(View.VISIBLE);
                    tvProfileName2.setVisibility(View.VISIBLE);
                    tvProfileEmail.setVisibility(View.VISIBLE);
                    tvProfilePhone.setVisibility(View.VISIBLE);
                    tvProfileGender.setVisibility(View.VISIBLE);
                    tvChangePhone.setVisibility(View.VISIBLE);
                    tvChangeFullName.setVisibility(View.VISIBLE);
                    tvProfileLogin.setVisibility(View.GONE);
                    tvProfileRegister.setVisibility(View.GONE);
                    tvProfileLogout.setVisibility(View.VISIBLE);
                    btnTrans.setVisibility(View.VISIBLE);
                    tvProfileName1.setText(obj.getString(Constants.KEY_TAI_KHOAN));
                    tvProfileName2.setText(obj.getString(Constants.KEY_TAI_KHOAN));
                    tvProfileFullname.setText(obj.getString(Constants.KEY_FULL_NAME));
                    tvProfileEmail.setText(obj.getString(Constants.KEY_EMAIL));
                    tvProfilePhone.setText(obj.getString(Constants.KEY_PHONE));
                    tvProfileGender.setText(obj.getString(Constants.KEY_SEX));
                    Log.e("sharepref",obj.getString(Constants.KEY_FULL_NAME));
                    Log.d("My App", obj.toString()+obj.get(Constants.KEY_AVARTAR));
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + userData + "\"");
                }
            }
        }
    }
    private void SetListener(){
        tvProfileLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);
        });
        tvProfileRegister.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SignUpActivity.class);
            startActivity(intent);
        });
        tvProfileLogout.setOnClickListener(view -> {
            OpenDialog();
        });
        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TransactionsAct.class);
                intent.putExtra("user_id",userId);
                startActivity(intent);
            }
        });
        tvChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                View view1 = getLayoutInflater().inflate(R.layout.change_pw_layout,null,false);
                builder.setView(view1);
                EditText edtPw = view1.findViewById(R.id.edt_change_pw_old);
                EditText edtNewPw = view1.findViewById(R.id.edt_change_pw_new);
                EditText edtRePw = view1.findViewById(R.id.edt_change_pw_new2);
                Button btnConfirmPw = view1.findViewById(R.id.btn_change_pw1);
                btnConfirmPw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edtNewPw.getText().toString().length()<8 || edtNewPw.getText().toString().length()>16 ){
                            Toast.makeText(getContext(), "Mật khẩu phải có 8-16 kí tự", Toast.LENGTH_SHORT).show();
                        }else if(!edtNewPw.getText().toString().equals(edtRePw.getText().toString())){
                            Toast.makeText(getContext(), "Mật khẩu mới chưa trùng khớp", Toast.LENGTH_SHORT).show();
                        }else {
                            ApiRetrofit.apiRetrofit.ChangeUserPassword(edtPw.getText().toString(),edtNewPw.getText().toString(),userId).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.body().equals("ok")){
                                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    }else if(response.body().equals("fail")) {
                                        Toast.makeText(getContext(), "Mật khẩu cũ chưa chính xác", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                builder.create().show();
            }
        });
        tvChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern special = Pattern.compile("[!#$%&*^()_+=|<>?{}\\[\\]~-]");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                View view1 = getLayoutInflater().inflate(R.layout.change_phone_layout,null,false);
                builder.setView(view1);
                EditText edtNewPhone = view1.findViewById(R.id.edt_change_phone);
                Button btnConfirmPhone = view1.findViewById(R.id.btn_change_phone);
                btnConfirmPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edtNewPhone.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Không để trống phần này", Toast.LENGTH_SHORT).show();
                        }else if(special.matcher(edtNewPhone.getText().toString()).find() || edtNewPhone.getText().toString().contains("N")){
                            Toast.makeText(getContext(), "Số dt không sử dụng kí tự đặc biệt", Toast.LENGTH_SHORT).show();
                        }else if(edtNewPhone.getText().toString().length()!=10){
                            Toast.makeText(getContext(), "Số điện thoại chỉ gồm 10 chữ số", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ApiRetrofit.apiRetrofit.ChangePhone(edtNewPhone.getText().toString(),userId).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_PHONE, MODE_MULTI_PROCESS);
                                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                        prefsEditor.putString(Constants.KEY_PHONE,edtNewPhone.getText().toString());
                                        prefsEditor.commit();
                                        tvProfilePhone.setText(edtNewPhone.getText().toString());
                                        Toast.makeText(getContext(), "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER,MODE_MULTI_PROCESS);
                                        SharedPreferences.Editor prefsEditor1 = sharedPreferences1.edit();
                                        prefsEditor1.putString(Constants.KEY_GET_USER, response.body().toString());
                                        prefsEditor1.putBoolean(Constants.KEY_CHECK_LOGIN, true);
                                        prefsEditor1.apply();
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                builder.create().show();
            }
        });
        tvChangeFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                View view1 = getLayoutInflater().inflate(R.layout.change_fullname_layout,null,false);
                builder.setView(view1);
                EditText edtNewFullname = view1.findViewById(R.id.edt_change_fullname);
                Button btnConfirmFullname = view1.findViewById(R.id.btn_change_fullname);
                btnConfirmFullname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edtNewFullname.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Không để trống phần này", Toast.LENGTH_SHORT).show();
                        }else {
                            ApiRetrofit.apiRetrofit.ChangeFullname(edtNewFullname.getText().toString(),userId).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_FULL_NAME,Context.MODE_PRIVATE);
                                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                        prefsEditor.putString(Constants.KEY_FULL_NAME,edtNewFullname.getText().toString());
                                        prefsEditor.commit();
                                        tvProfileFullname.setText(edtNewFullname.getText().toString());
                                        Toast.makeText(getContext(), "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER,MODE_MULTI_PROCESS);
                                        SharedPreferences.Editor prefsEditor1 = sharedPreferences1.edit();
                                        prefsEditor1.putString(Constants.KEY_GET_USER, response.body().toString());
                                        prefsEditor1.putBoolean(Constants.KEY_CHECK_LOGIN, true);
                                        prefsEditor1.apply();
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                builder.create().show();
            }
        });
    }
    private void SignOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Logout successfull", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void OpenDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set Title and Message:
        builder.setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất không?");

        //
        builder.setCancelable(true);
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().dismiss();
            }
        });
        // Create "Positive" button with OnClickListener.
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SignOut();
                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ApiRetrofit.apiRetrofit.DeleteFcmToken(userId,token).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equals("ok")){
                                    Log.e("fcm","ok");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                });
                //progressLoadingCommon.showProgressLoading(getActivity());
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, MODE_MULTI_PROCESS);
                sharedPreferences.edit().remove(Constants.KEY_GET_USER).commit();
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                prefsEditor.putBoolean(Constants.KEY_CHECK_LOGIN, false);
                prefsEditor.apply();
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
//                ciAvatar.setImageResource(R.drawable.ic_avatar2);
//                LoadDetail();
            }
        });
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
