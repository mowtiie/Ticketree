package com.mowtiie.ticketree.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

        binding.tilEmail.setHelperText("Use the email you signed up with");
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
            } else if (DatabaseUtils.queryNumEntries(db, "users") == 0) {
                ContentValues cv = new ContentValues();
                cv.put("name", email);
                cv.put("email", email);
                cv.put("password", password);
                cv.put("organizer", 0);
                db.insert("users", null, cv);
                Data.USER_NAME = email;
                Data.USER_IS_ORGANIZER = false;
                Data.USER_PHONE = "";
                Data.USER_ABOUT = "";
            } else {
                c.close();
                binding.tilPassword.setError("Wrong email or password");
                return;
            }
            c.close();
            Data.USER_EMAIL = email;
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
