package com.ecommerce.aryan.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ecommerce.aryan.Authentication.SessionManager;
import com.ecommerce.aryan.Database.AppDatabase;
import com.ecommerce.aryan.Models.CategoryModel;
import com.ecommerce.aryan.Models.ItemModel;
import com.ecommerce.aryan.databinding.ActivityVerifyPhoneBinding;


import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    ActivityVerifyPhoneBinding binding;
    ProgressDialog progressDialog;
    String verificationId, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(VerifyPhoneActivity.this);
        progressDialog.setTitle("Please wait.");
        phone = getIntent().getStringExtra("phone");

        if(phone==null){
            Toast.makeText(this, "Invalid phone.", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.message.setText("We sent the code to "+phone);

        sendVerificationCode("+91"+phone);

        binding.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = binding.otpView.getText().toString();
                if (otp.length() == 6) {
                    verifyCode(otp);
                } else {
                    Toast.makeText(VerifyPhoneActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            binding.otpView.setText(code);
                            verifyCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyPhoneActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        progressDialog.dismiss();
                        Toast.makeText(VerifyPhoneActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyCode(String codeByUser) {
        progressDialog.dismiss();
        Toast.makeText(VerifyPhoneActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();


        createDatabase();
        SessionManager sessionManager = new SessionManager(VerifyPhoneActivity.this);
        sessionManager.createLoginSession(FirebaseAuth.getInstance().getUid(),phone);
        Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, codeByUser);
//        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(VerifyPhoneActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();


                        createDatabase();
                        SessionManager sessionManager = new SessionManager(VerifyPhoneActivity.this);
                        sessionManager.createLoginSession(FirebaseAuth.getInstance().getUid(),phone);
                        Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(VerifyPhoneActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(VerifyPhoneActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createDatabase() {
        progressDialog.show();


        AppDatabase db = AppDatabase.getInstance(VerifyPhoneActivity.this);

        Random random = new Random();

        String[] categories = {"Nike", "Puma", "Rebook", "Adidas"};
        int[] prices = {500,600,300,200,700,800,900,1100};
        String[] types = {"Sneaker", "Sport", "Casual", "Formal"};
        String[] links = {"https://freepngimg.com/png/9299-adidas-shoes-picture",
                "https://freepngimg.com/thumb/adidas_shoes/3-2-adidas-shoes-png-clipart-thumb.png",
                "https://freepngimg.com/thumb/adidas_shoes/6-2-adidas-shoes-png-image-thumb.png",
                "https://freepngimg.com/thumb/adidas_shoes/7-2-adidas-shoes-free-download-png-thumb.png",
                "https://freepngimg.com/thumb/men%20shoes/16-men-shoes-png-image-thumb.png"};


        for (String s : categories) {
            db.categoryDao().insertCategory(new CategoryModel(s));
            for (int i = 0; i < 10; i++) {
                ItemModel randomShoe = new ItemModel();
                randomShoe.name = s + String.valueOf(i);
                randomShoe.price = prices[random.nextInt(prices.length)];
                randomShoe.image = links[random.nextInt(links.length)];
                randomShoe.type = types[random.nextInt(types.length)];
                randomShoe.category = categories[random.nextInt(categories.length)];
                db.itemDao().insertItem(randomShoe);
            }
        }

    }
}
