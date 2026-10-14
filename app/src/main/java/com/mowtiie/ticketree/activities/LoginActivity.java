package com.mowtiie.ticketree.activities;

import com.mowtiie.ticketree.data.Data;
import com.mowtiie.ticketree.data.DbHelper;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mowtiie.ticketree.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
            Cursor c = db.rawQuery("SELECT name, organizer FROM users WHERE email=? AND password=?",
                    new String[]{email, password});
            if (c.moveToFirst()) {
                Data.USER_NAME = c.getString(0);
                Data.USER_IS_ORGANIZER = c.getInt(1) == 1;
            } else if (DatabaseUtils.queryNumEntries(db, "users") == 0) {
                ContentValues cv = new ContentValues();
                cv.put("name", email);
                cv.put("email", email);
                cv.put("password", password);
                cv.put("organizer", 0);
                db.insert("users", null, cv);
                Data.USER_NAME = email;
                Data.USER_IS_ORGANIZER = false;
            } else {
                c.close();
                Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                return;
            }
            c.close();
            Data.USER_EMAIL = email;
            Data.USER_PASSWORD = password;
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        binding.btnForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ResetPasswordActivity.class)));

        binding.btnSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }
}
