package com.mowtiie.ticketree.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.data.DbHelper;
import com.mowtiie.ticketree.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tilEmail.setHelperText("Demo: customer@demo.com or organizer@demo.com");
        binding.tilPassword.setHelperText("Demo password: demo1234");
        binding.tilPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            if (email.isEmpty()) {
                binding.tilEmail.setError("Enter your email");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Enter a valid email address");
                return;
            }
            if (password.isEmpty()) {
                binding.tilPassword.setError("Enter your password");
                return;
            }

            SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
            Cursor c = db.rawQuery("SELECT name, organizer, phone, about FROM users WHERE email=? AND password=?",
                    new String[]{email, password});
            if (c.moveToFirst()) {
                Data.USER_NAME = c.getString(0);
                Data.USER_IS_ORGANIZER = c.getInt(1) == 1;
                Data.USER_PHONE = c.getString(2);
                Data.USER_ABOUT = c.getString(3);
            } else {
                c.close();
                binding.tilPassword.setError("Wrong email or password");
                return;
            }
            c.close();
            Data.USER_EMAIL = email;
            if (email.equals(Data.DEMO_CUSTOMER_EMAIL)) Data.seedDemoPurchase();
            Data.USER_PASSWORD = password;
            startActivity(new Intent(this, Data.USER_IS_ORGANIZER ? OrganizerHomeActivity.class : HomeActivity.class));
            finish();
        });

        binding.btnForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ResetPasswordActivity.class)));

        binding.btnSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }
}
