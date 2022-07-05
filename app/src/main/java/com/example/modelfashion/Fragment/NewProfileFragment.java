package com.example.modelfashion.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.modelfashion.Activity.SignIn.SignInActivity;
import com.example.modelfashion.Activity.SignIn.SignUpActivity;
import com.example.modelfashion.Adapter.VpSaleMainFmAdapter;
import com.example.modelfashion.Model.ItemSaleMain;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import me.relex.circleindicator.CircleIndicator3;

public class NewProfileFragment extends Fragment {
    CircleImageView ciAvatar;
    TextView tvProfileName1, tvProfileName2, tvProfileLogin, tvProfileRegister, tvProfileEmail, tvProfileFullname, tvProfilePhone, tvProfileGender, tvProfileLogout;
    Boolean isLogin;
    String userId;
    GoogleSignInClient mGoogleSignInClient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_new, container, false);
        ciAvatar = view.findViewById(R.id.ci_avatar);
        tvProfileName1 = view.findViewById(R.id.tv_profile_name1);
        tvProfileName2 = view.findViewById(R.id.tv_profile_name2);
        tvProfileLogin = view.findViewById(R.id.tv_profile_login);
        tvProfileRegister = view.findViewById(R.id.tv_profile_register);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        tvProfileFullname = view.findViewById(R.id.tv_profile_fullname);
        tvProfilePhone = view.findViewById(R.id.tv_profile_phone);
        tvProfileGender = view.findViewById(R.id.tv_profile_gender);
        tvProfileLogout = view.findViewById(R.id.tv_profile_logout);
        Bundle info = getArguments();
        userId = info.getString("user_id");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        LoadDetail();
        SetListener();
        return view;
    }

    private void LoadDetail(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, Context.MODE_MULTI_PROCESS);
        isLogin = sharedPreferences.getBoolean(Constants.KEY_CHECK_LOGIN, false);
        if (isLogin == false) {
            User user = new User("", "", "", "", "", "", "");
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putString("user", user.toString());
            prefsEditor.apply();
            tvProfileName1.setVisibility(View.GONE);
            tvProfileName2.setVisibility(View.GONE);
            tvProfileFullname.setVisibility(View.GONE);
            tvProfileEmail.setVisibility(View.GONE);
            tvProfilePhone.setVisibility(View.GONE);
            tvProfileGender.setVisibility(View.GONE);
            tvProfileLogin.setVisibility(View.VISIBLE);
            tvProfileRegister.setVisibility(View.VISIBLE);
            tvProfileLogout.setVisibility(View.GONE);
        } else {
            if (sharedPreferences.contains(Constants.KEY_GET_USER)) {
                String userData = sharedPreferences.getString(Constants.KEY_GET_USER, "");

                try {
                    JSONObject obj = new JSONObject(userData);
                    tvProfileName1.setVisibility(View.VISIBLE);
                    tvProfileName2.setVisibility(View.VISIBLE);
                    tvProfileEmail.setVisibility(View.VISIBLE);
                    tvProfilePhone.setVisibility(View.VISIBLE);
                    tvProfileGender.setVisibility(View.VISIBLE);
                    tvProfileLogin.setVisibility(View.GONE);
                    tvProfileRegister.setVisibility(View.GONE);
                    tvProfileLogout.setVisibility(View.VISIBLE);

                    tvProfileName1.setText(obj.getString(Constants.KEY_TAI_KHOAN));
                    tvProfileName2.setText(obj.getString(Constants.KEY_TAI_KHOAN));
                    tvProfileFullname.setText(obj.getString(Constants.KEY_FULL_NAME));
                    tvProfileEmail.setText(obj.getString(Constants.KEY_EMAIL));
                    tvProfilePhone.setText(obj.getString(Constants.KEY_PHONE));
                    tvProfileGender.setText(obj.getString(Constants.KEY_SEX));
                    Glide.with(getActivity())
                            .load(obj.get(Constants.KEY_AVARTAR.replace("localhost",Constants.KEY_IP)))
                            .into(ciAvatar);


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
                //progressLoadingCommon.showProgressLoading(getActivity());
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.KEY_SAVE_USER, Context.MODE_MULTI_PROCESS);
                sharedPreferences.edit().remove(Constants.KEY_GET_USER).commit();
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                prefsEditor.putBoolean(Constants.KEY_CHECK_LOGIN, false);
                prefsEditor.apply();
                ciAvatar.setImageResource(R.drawable.ic_avatar2);
                LoadDetail();
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
