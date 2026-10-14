package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.data.DbHelper;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import com.mowtiie.ticketree.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.tvRole.setText(Data.USER_IS_ORGANIZER ? "Organizer account" : "Customer account");
        binding.etName.setText(Data.USER_NAME);
        binding.etEmail.setText(Data.USER_EMAIL);
        binding.etPassword.setText(Data.USER_PASSWORD);

        binding.tilName.setHelperText("As shown on your tickets");
        binding.tilEmail.setHelperText("Tickets and receipts are sent here");
        binding.tilPassword.setHelperText("At least 6 characters");
        binding.tilPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            binding.tilName.setError(null);
            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            if (name.isEmpty()) {
                binding.tilName.setError("Enter your full name");
                return;
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Enter a valid email address");
                return;
            }
            if (password.length() < 6) {
                binding.tilPassword.setError("Use at least 6 characters");
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("email", email);
            cv.put("password", password);
            int rows = new DbHelper(this).getWritableDatabase()
                    .updateWithOnConflict("users", cv, "email=?", new String[]{Data.USER_EMAIL},
                            android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE);
            if (rows == 0) {
                binding.tilEmail.setError("This email is already registered");
                return;
            }
            Data.USER_NAME = name;
            Data.USER_EMAIL = email;
            Data.USER_PASSWORD = password;
            Snackbar.make(binding.getRoot(), "Profile updated", Snackbar.LENGTH_SHORT).show();
        });

        binding.btnReset.setOnClickListener(v ->
                startActivity(new Intent(this, ResetPasswordActivity.class)));

        binding.btnLogout.setOnClickListener(v -> {
            Data.USER_NAME = "";
            Data.USER_EMAIL = "";
            Data.USER_PASSWORD = "";
            Data.USER_IS_ORGANIZER = false;
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
