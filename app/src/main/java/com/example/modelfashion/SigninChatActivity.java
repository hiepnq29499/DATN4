package com.example.modelfashion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfashion.databinding.ActivitySigninchatBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninChatActivity extends AppCompatActivity {
    private ActivitySigninchatBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySigninchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getboolean(Constants.Key_SignEDname)){
            Intent intent = new Intent(getApplicationContext(), MainMainActivity.class);
            startActivity(intent);
            finish();
        }
        setListeners();
    }


    private void setListeners() {
        binding.BtnSignUp.setOnClickListener(v -> startActivities(new Intent[]{new Intent(getApplicationContext(), SignupChatActivity.class)}));
        binding.Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidUpDetail()){
                    signiN();
                }
            }
        });
    }
    private void showTOAST(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signiN() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
       database.collection(Constants.Key_collection_user)
               .whereEqualTo(Constants.Key_email, binding.edName.getText().toString())
               .whereEqualTo(Constants.Key_pass, binding.Edpass.getText().toString())
               .get().addOnCompleteListener(task -> {
                  if (task.isSuccessful() && task.getResult() != null
                          && task.getResult().getDocuments().size()>0 ){
                      DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                      preferenceManager.putBoolean(Constants.Key_SignEDname,true);
                      preferenceManager.putString(Constants.Key_id_user,  documentSnapshot.getId());
                      preferenceManager.putString(Constants.Key_id_user,  documentSnapshot.getString(Constants.Key_name));

                      Intent intent = new Intent(getApplicationContext(), MainMainActivity.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      startActivity(intent);


                  }else {
                      showTOAST("err in activity signin");
                  }
               });
    }

    private Boolean isValidUpDetail() {
        if (binding.edName.getText().toString().trim().isEmpty()) {
            showTOAST("Enter Name");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edName.getText().toString()).matches()) {
            showTOAST("Enter valid Email");
            return false;
        } else if (binding.Edpass.getText().toString().trim().isEmpty()) {
            showTOAST("select image");
            return false;
        } else {
            return true;
        }
    }
}