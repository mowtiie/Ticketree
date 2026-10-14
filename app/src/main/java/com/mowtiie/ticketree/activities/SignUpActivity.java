package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.DbHelper;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import com.mowtiie.ticketree.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnCreate.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Fill in all fields", Snackbar.LENGTH_SHORT).show();
                return;
            }
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("email", email);
            cv.put("password", password);
            cv.put("organizer", binding.swOrganizer.isChecked() ? 1 : 0);
            long id = new DbHelper(this).getWritableDatabase().insert("users", null, cv);
            if (id == -1) {
                Snackbar.make(binding.getRoot(), "Email already registered", Snackbar.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Account created. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
