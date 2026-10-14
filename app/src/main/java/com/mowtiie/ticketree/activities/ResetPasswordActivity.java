package com.mowtiie.ticketree.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityResetPasswordBinding binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.tilEmail.setHelperText("We will send a reset link to this email");
        binding.etEmail.setText(Data.USER_EMAIL);

        binding.btnSend.setOnClickListener(v -> {
            binding.tilEmail.setError(null);
            String email = binding.etEmail.getText().toString().trim();
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Enter a valid email address");
                return;
            }
            Toast.makeText(this, "Reset link sent", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
