package com.ecommerce.aryan.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ecommerce.aryan.Authentication.SessionManager;
import com.ecommerce.aryan.R;
import com.ecommerce.aryan.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(LoginActivity.this);
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.phone.getText().toString();
                if (!verifyPhoneNumber(phone)) return;

                startActivity(new Intent(LoginActivity.this, VerifyPhoneActivity.class)
                        .putExtra("phone", phone));
            }
        });

    }


    public boolean verifyPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 10) {
            Toast.makeText(LoginActivity.this, "Phone number should be of 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneNumber.startsWith("+91")) {
            Toast.makeText(LoginActivity.this, "Phone number should not contain country code", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneNumber.contains(" ")) {
            Toast.makeText(LoginActivity.this, "Phone number should not contain spaces", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        if (sessionManager.checkLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            FirebaseAuth.getInstance().signOut();
        }


    }
}