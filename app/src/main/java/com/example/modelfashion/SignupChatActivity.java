package com.example.modelfashion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.modelfashion.databinding.ActivitySignupchatBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupChatActivity extends AppCompatActivity {
    private ActivitySignupchatBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        setListeners();
    }

    private void setListeners() {
        binding.Btnlogin.setOnClickListener(v -> onBackPressed());
        binding.BtnSignUp.setOnClickListener(v -> {
            if (isValidUpDetail()) {
                signUp();
            }
            ;
        });
    }

    private void showTOAST(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, String> user = new HashMap<>();
        user.put(Constants.Key_name, binding.edname.getText().toString());
        user.put(Constants.Key_email, binding.edEmail.getText().toString());
        user.put(Constants.Key_pass, binding.edPss.getText().toString());
        database.collection(Constants.Key_collection_user)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                preferenceManager.putBoolean(Constants.Key_SignEDname,true);
                preferenceManager.putString(Constants.Key_id_user,documentReference.getId());
                preferenceManager.putString(Constants.Key_name,documentReference.getId());

                    Intent intent = new Intent(getApplicationContext(), MainMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(exception -> {
                              showTOAST(exception.getMessage());
                });

    }

    private Boolean isValidUpDetail() {
       if (binding.edname.getText().toString().trim().isEmpty()) {
            showTOAST("Enter Name");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.getText().toString()).matches()) {
            showTOAST("Enter Email");
            return false;
        } else if (binding.edPss.getText().toString().trim().isEmpty()) {
            showTOAST("select image");
            return false;
        } else if (binding.edCfpass.getText().toString().trim().isEmpty()) {
            showTOAST("select image");
            return false;
        } else {
            return true;
        }
    }

}