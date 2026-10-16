package com.mowtiie.ticketree.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.data.DbHelper;
import com.mowtiie.ticketree.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String[] roleHints = {
                "Buy tickets, pick seats and keep them in your wallet.",
                "Create events, set prices and track sales."
        };
        binding.tvRoleHint.setText(roleHints[0]);
        binding.tabRole.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tvRoleHint.setText(roleHints[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.tilName.setHelperText("As shown on your tickets");
        binding.tilEmail.setHelperText("Tickets and receipts are sent here");
        binding.tilPassword.setHelperText("At least 6 characters");
        binding.tilBirthdate.setHelperText("Used to check event age restrictions");
        binding.etBirthdate.setOnClickListener(v -> Data.pickBirthdate(this, binding.etBirthdate));
        binding.tilAddress.setHelperText("Street, city");
        binding.tilPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        binding.tilConfirm.setHelperText("Type the same password again");
        binding.tilConfirm.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        binding.tilPhone.setHelperText("11 digits, for example 09171234567");

        binding.btnCreate.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            binding.tilName.setError(null);
            binding.tilEmail.setError(null);
            String phone = binding.etPhone.getText().toString().trim();
            String birthdate = binding.etBirthdate.getText().toString().trim();
            String address = binding.etAddress.getText().toString().trim();
            binding.tilPassword.setError(null);
            binding.tilConfirm.setError(null);
            binding.tilPhone.setError(null);
            binding.tilBirthdate.setError(null);
            binding.tilAddress.setError(null);
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
            if (!binding.etConfirm.getText().toString().trim().equals(password)) {
                binding.tilConfirm.setError("Passwords do not match");
                return;
            }
            if (!phone.matches("^09\\d{9}$")) {
                binding.tilPhone.setError("Enter an 11-digit number starting with 09");
                return;
            }
            if (birthdate.isEmpty()) {
                binding.tilBirthdate.setError("Pick your birth date");
                return;
            }
            if (address.length() < 5) {
                binding.tilAddress.setError("Enter your street and city");
                return;
            }

            ContentValues cv = new ContentValues();
            cv.put("phone", phone);
            cv.put("birthdate", birthdate);
            cv.put("address", address);
            cv.put("name", name);
            cv.put("email", email);
            cv.put("password", password);
            cv.put("organizer", binding.tabRole.getSelectedTabPosition());
            long id = new DbHelper(this).getWritableDatabase().insert("users", null, cv);
            if (id == -1) {
                binding.tilEmail.setError("This email is already registered");
                return;
            }
            Toast.makeText(this, "Account created. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
