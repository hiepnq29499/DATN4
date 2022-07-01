package com.example.modelfashion.Fragment;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Activity.MainActivity;
import com.example.modelfashion.Activity.ProfileActivity;
import com.example.modelfashion.Activity.SignIn.SignInActivity;
import com.example.modelfashion.Activity.SignIn.SignUpActivity;
import com.example.modelfashion.Common.ProgressLoadingCommon;
import com.example.modelfashion.History.ViewHistory.HistoryActivity;
import com.example.modelfashion.Interface.ApiRetrofit;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;
import com.example.modelfashion.Utility.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfile extends Fragment {
    PreferenceManager preferenceManager;
    TextView tv_name, tv_user, tv_login, btn_profile, btn_cart, btn_history, btn_logout, tv_signUp;
    LinearLayout btn_status, btn_status2, btn_status3;
    RoundedImageView img;
    TextView btn_feedback;
    LinearLayout ll_login;
    Boolean isLogin;
    ProgressLoadingCommon progressLoadingCommon;
    String user_id;
    String account_type;
    String token;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        img = view.findViewById(R.id.img_frag_profile_avatar);
        tv_name = view.findViewById(R.id.tv_frag_Profile_Name);
        tv_user = view.findViewById(R.id.tv_frag_Profile_user);
        tv_login = view.findViewById(R.id.tv_frag_Profile_Login);
        tv_signUp = view.findViewById(R.id.tv_frag_Profile_Sign_Up);
        ll_login = view.findViewById(R.id.ll_login);
        btn_profile = view.findViewById(R.id.btn_frag_Profile_Profile);
        btn_cart = view.findViewById(R.id.btn_frag_Profile_cart);
        btn_history = view.findViewById(R.id.btn_frag_Profile_history);
        btn_logout = view.findViewById(R.id.btn_frag_Profile_Logout);
        btn_status = view.findViewById(R.id.btn_frag_Profile_status);
        btn_status2 = view.findViewById(R.id.btn_frag_Profile_status2);
        btn_status3 = view.findViewById(R.id.btn_frag_Profile_status3);
        Bundle info = getArguments();
        user_id = info.getString("user_id");
        account_type = info.getString("account_type");
        preferenceManager = new PreferenceManager(getContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        loadDetails();
        setListener();
//        btn_logout.setEnabled(false);
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                token = task.getResult();
//                btn_logout.setEnabled(true);
//            }
//        });
        return view;
    }

    //load dữ liệu lên màn hình
    private void loadDetails() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, Context.MODE_MULTI_PROCESS);
        isLogin = sharedPreferences.getBoolean(Constants.KEY_CHECK_LOGIN, false);
        if (isLogin == false) {
            User user = new User("", "", "", "", "", "", "");
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString("user", user.toString());
            prefsEditor.apply();

            tv_user.setVisibility(View.GONE);
            tv_name.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.GONE);
        } else {
            if (sharedPreferences.contains(Constants.KEY_GET_USER)) {
                String userData = sharedPreferences.getString(Constants.KEY_GET_USER, "");

                try {
                    JSONObject obj = new JSONObject(userData);

                    tv_user.setVisibility(View.VISIBLE);
                    tv_name.setVisibility(View.GONE);
                    ll_login.setVisibility(View.GONE);
                    btn_logout.setVisibility(View.VISIBLE);

                    tv_user.setText(obj.getString(Constants.KEY_TAI_KHOAN));
                    Glide.with(getActivity())
                            .load(obj.get(Constants.KEY_AVARTAR))
                            .into(img);

                    Log.d("My App", obj.toString()+obj.get(Constants.KEY_AVARTAR));

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + userData + "\"");
                }
            }
        }
    }

    //thêm chức năng vào các nút bấm
    private void setListener() {
        tv_login.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);
        });

        tv_signUp.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SignUpActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(v -> {
            openDialog();
//            switch (account_type) {
//                case "normal":
//                    openDialog();
//                    break;
//                case "google":
//                    signOut();
//                    break;
//            }
//            progressLoadingCommon.showProgressLoading(getActivity());
//            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    DeleteToken(user_id);
//                }
//            });
//            openDialog();
        });
        btn_profile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });
        btn_history.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            intent.putExtra("numberStatus", 4);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });
        btn_status.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            intent.putExtra("numberStatus", 1);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });

        btn_status2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            intent.putExtra("numberStatus", 2);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });
        btn_status3.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            intent.putExtra("numberStatus", 3);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Logout successfull", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void DeleteToken(String user_id){
        ApiRetrofit.apiRetrofit.DeleteFcmToken(user_id,token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("fcm", "Delete token status : "+response.body()+" user_id: "+user_id+" token: "+token);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, Context.MODE_MULTI_PROCESS);
                sharedPreferences.edit().remove(Constants.KEY_GET_USER).commit();
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                prefsEditor.putBoolean(Constants.KEY_CHECK_LOGIN, false);
                prefsEditor.apply();
                img.setImageResource(R.drawable.bg_gradient_blue);
                loadDetails();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void openDialog() {
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
                signOut();
                //progressLoadingCommon.showProgressLoading(getActivity());
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, Context.MODE_MULTI_PROCESS);
                sharedPreferences.edit().remove(Constants.KEY_GET_USER).commit();
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                prefsEditor.putBoolean(Constants.KEY_CHECK_LOGIN, false);
                prefsEditor.apply();
                img.setImageResource(R.drawable.bg_gradient_blue);
                loadDetails();
            }
        });
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }
}


