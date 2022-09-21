package com.example.modelfashion.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.modelfashion.Fragment.CartFragment;
import com.example.modelfashion.Fragment.CategoryFragment;
import com.example.modelfashion.Fragment.FragmentProfile;
import com.example.modelfashion.Fragment.MainFragment;
import com.example.modelfashion.Fragment.NewCartFragment;
import com.example.modelfashion.Fragment.NewProductFragment;
import com.example.modelfashion.Fragment.NewProfileFragment;
import com.example.modelfashion.Model.response.User.User;
import com.example.modelfashion.R;
import com.example.modelfashion.Utility.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "100";
    String user_id;
    //String uxser_id;
    String account_type;
    Boolean isLogin;
    Bundle info;
    public static BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = new Bundle();
        getUserData();
        info.putString("user_id",user_id);
        info.putString("account_type",account_type);
//        replaceFragment(new MainFragment());
        replaceFragment(new NewProductFragment());
        navigationView=findViewById(R.id.bottom_navigation_view_linear);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_item_home:
//                        MainFragment mainFragment = new MainFragment();
//                        mainFragment.setArguments(info);
//                        replaceFragment(mainFragment);
                        NewProductFragment newProductFragment = new NewProductFragment();
                        newProductFragment.setArguments(info);
                        replaceFragment(newProductFragment);
                        break;
                    case R.id.main_item_cart:
//                        CartFragment cartFragment = new CartFragment();
//                        cartFragment.setArguments(info);
//                        replaceFragment(cartFragment);
                        NewCartFragment newCartFragment = new NewCartFragment();
                        newCartFragment.setArguments(info);
                        replaceFragment(newCartFragment);
                        break;
//                    case R.id.main_item_category:
//                        CategoryFragment categoryFragment = new CategoryFragment();
//                        categoryFragment.setArguments(info);
//                        replaceFragment(categoryFragment);
//                        break;
                    case R.id.main_item_profile:
//                        FragmentProfile fragmentProfile = new FragmentProfile();
//                        fragmentProfile.setArguments(info);
//                        replaceFragment(fragmentProfile);
                        NewProfileFragment newProfileFragment = new NewProfileFragment();
                        newProfileFragment.setArguments(info);
                        replaceFragment(newProfileFragment);
                        break;
                }
                return true;
            }
        });
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//            Toast.makeText(this, ""+personName+" "+personEmail, Toast.LENGTH_LONG).show();
//        }
        createNotificationChannel();
        getToken();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.e("fcm","Fail");
                }
                Log.e("fcm",task.getResult());
            }
        });
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotification";
            String description = "Receive firebase notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void getUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.KEY_SAVE_USER, Context.MODE_MULTI_PROCESS);
        isLogin = sharedPreferences.getBoolean(Constants.KEY_CHECK_LOGIN, false);
        if (isLogin == false) {
//            User user = new User("", "", "", "", "", "", "");
//            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//            prefsEditor.putString("user", user.toString());
//            prefsEditor.apply();
            user_id = "null";
        } else {
            if (sharedPreferences.contains(Constants.KEY_GET_USER)) {
                String userData = sharedPreferences.getString(Constants.KEY_GET_USER, "");
                try {
                    JSONObject obj = new JSONObject(userData);
                    user_id = obj.getString(Constants.KEY_ID);
                    account_type = obj.getString(Constants.KEY_ACCOUNT_TYPE);
                    Log.d("My App", obj.toString()+user_id);

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + userData + "\"");
                }
            }
        }
    }

    private void replaceFragment(Fragment fm){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fm.setArguments(info);
        fragmentTransaction.replace(R.id.frmlayout,fm);
        fragmentTransaction.commit();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void showBottomNavigation(){
        navigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigation(){
        navigationView.setVisibility(View.GONE);
    }

    public void moveToFragmentProfile(){
        FragmentProfile fragmentProfile = new FragmentProfile();
        replaceFragment(fragmentProfile);
        navigationView.setSelectedItemId(R.id.main_item_profile);
    }
}
