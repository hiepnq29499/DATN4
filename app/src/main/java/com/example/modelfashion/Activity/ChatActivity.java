package com.example.modelfashion.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfashion.Constants;
import com.example.modelfashion.User;
import com.example.modelfashion.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
 private ActivityChatBinding binding;
 private User receiUser;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
        loadReceiDetail();

    }

    private void loadReceiDetail(){
     receiUser = (User) getIntent().getSerializableExtra(Constants.Key_User);
     binding.tvName.setText(receiUser.name);
    }
    private  void setListeners(){
     binding.imageback.setOnClickListener(v-> onBackPressed());
    }
}