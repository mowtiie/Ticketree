package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.mowtiie.ticketree.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResetPasswordBinding binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.etEmail.setText(Data.USER_EMAIL);

        binding.btnSend.setOnClickListener(v -> {
            if (binding.etEmail.getText().toString().trim().isEmpty()) {
                Snackbar.make(binding.getRoot(), "Enter your email", Snackbar.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Reset link sent", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
