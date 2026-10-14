package com.mowtiie.ticketree;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
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

        findViewById(R.id.btnSignUp).setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }
}
