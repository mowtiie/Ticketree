package com.mowtiie.ticketree;

import android.content.Intent;
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
            if (!Data.USER_EMAIL.isEmpty()
                    && (!email.equals(Data.USER_EMAIL) || !password.equals(Data.USER_PASSWORD))) {
                Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Data.USER_EMAIL.isEmpty()) {
                Data.USER_EMAIL = email;
                Data.USER_PASSWORD = password;
                Data.USER_NAME = email;
            }
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        findViewById(R.id.btnSignUp).setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }
}
