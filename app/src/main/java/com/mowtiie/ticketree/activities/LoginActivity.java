package com.mowtiie.ticketree.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.data.DbHelper;
import com.mowtiie.ticketree.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tilEmail.setHelperText("Use the email you signed up with");
        binding.tilPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            boolean asOrganizer = binding.swOrganizer.isChecked();

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
            Cursor c = db.rawQuery("SELECT name, organizer FROM users WHERE email=? AND password=?",
                    new String[]{email, password});
            if (c.moveToFirst()) {
                boolean isOrganizer = c.getInt(1) == 1;
                if (isOrganizer != asOrganizer) {
                    c.close();
                    Snackbar.make(binding.getRoot(), isOrganizer
                            ? "This is an organizer account. Turn on \"Log in as organizer\"."
                            : "This is a customer account. Turn off \"Log in as organizer\".",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                Data.USER_NAME = c.getString(0);
                Data.USER_IS_ORGANIZER = isOrganizer;
            } else if (DatabaseUtils.queryNumEntries(db, "users") == 0) {
                ContentValues cv = new ContentValues();
                cv.put("name", email);
                cv.put("email", email);
                cv.put("password", password);
                cv.put("organizer", asOrganizer ? 1 : 0);
                db.insert("users", null, cv);
                Data.USER_NAME = email;
                Data.USER_IS_ORGANIZER = asOrganizer;
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
