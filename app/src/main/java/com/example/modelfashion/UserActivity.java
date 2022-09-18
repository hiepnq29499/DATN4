package com.example.modelfashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfashion.Activity.ChatActivity;
import com.example.modelfashion.Utility.PreferenceManager;
import com.example.modelfashion.databinding.ActivityUserBinding;
import com.example.modelfashion.network.UserListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserListener {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUser();

        setListeners();
    }

    private void setListeners() {
        binding.imageView.setOnClickListener(v -> onBackPressed());

    }
    private void getUser() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.Key_collection_user)
                .get()
                .addOnCompleteListener(task -> {
                    loading(true);
                    String currentUserId = preferenceManager.getString(Constants.Key_id_user);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (currentUserId.equals(documentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = documentSnapshot.getString(Constants.Key_name);
                            user.email = documentSnapshot.getString(Constants.Key_email);
                            user.token = documentSnapshot.getString(Constants.Key_FCM_TOKEN);
                            users.add(user);

                        }
                        if (users.size()>0){
                            UserAdapter userAdapter = new UserAdapter(users, this);
                            binding.rcvUser.setAdapter(userAdapter);
                            binding.rcvUser.setVisibility(View.VISIBLE);
                        }else{
                            showERR();
                        }
                    }else{
                        showERR();
                    }
                });
    }

    private void showERR() {
        binding.edErrMess.setText(String.format("%s",  "No user avalable"));
        binding.edErrMess.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progress.setVisibility(View.VISIBLE);
        } else {
            binding.progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.Key_User, user);
        startActivity(intent);
        finish();
    }
}