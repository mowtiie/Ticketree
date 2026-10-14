package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.data.DbHelper;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvRole.setText(Data.USER_IS_ORGANIZER ? "Organizer account" : "Customer account");
        binding.etName.setText(Data.USER_NAME);
        binding.etEmail.setText(Data.USER_EMAIL);
        binding.etPassword.setText(Data.USER_PASSWORD);

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }
            Data.USER_NAME = name;
            Data.USER_EMAIL = email;
            Data.USER_PASSWORD = password;
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
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
